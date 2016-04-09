(ns clj-compfund3.tree-states
  (:require [clojure.string :as string]))

;I thought I'd try to explain how this works this time, so here we go.
;This Binary Search Tree has been implemented as a persistent data structure, consisting of nested maps.
;As it is persistent, calling a function will return a new tree rather than modify the existing one.
;The format for a "node" looks like:

;{:value "value"
; :left <another node or nil>
; :right <also another node or nil>}

;In order to help navigate the tree, I implemented things throughout my program called "paths". They look like this:

;[:left :right :right :left <etc.>]

;A path is just a series of map gets for the tree. For example, the one above would first get the left node,
;then the right node, then the right node again, then the left node of that and so on.
;Clojure's get-in function makes it even easier to obtain the value at these paths.
;Just doing (get-in <tree> <path>) will get you the node you're looking for.

;I actually developed this a while back, and am just adding the commentary and implementation now.

(defn add-value
  "This function adds a node to the tree with a given value.
  It builds a path based on the value, eventually reaching a point where the path points to nil.
  When that happens, there is an empty node there, and it gets filled in with the new node."
  [tree added-value]
  (loop [path []]
    (let [node (get-in tree path)
          value (:value node)
          position (if (< (compare added-value value) 0) :left :right)]
      (if (nil? (get node position))
        (assoc-in tree (conj path position) {:value added-value
                                             :left nil
                                             :right nil})
        (recur (conj path position))))))

(defn delete-node
  "This deletes a node when given its path. It handles the usual edge cases of deletion as well."
  [tree path]
  (let [node (get-in tree path)]
    (if (and (nil? (:right node)) (nil? (:left node)))
      (assoc-in tree path nil)
      (if (and (nil? (:right node)) (:left node))
        (assoc-in tree path (:left node))
        (if (and (nil? (:left node)) (:right node))
          (assoc-in tree path (:right node))
          (recur (assoc-in tree (conj path :value) (:value (:left node))) (conj path :left)))))))

(defn get-path
  "When given a tree and a value to search for, constructs a path to the node containing the value in question if found.
  Otherwise it will return nil."
  [tree search-value]
  (loop [path []]
    (let [node (get-in tree path)
          value (:value node)
          position (if (< (compare search-value value) 0) :left :right)]
      (if (= value search-value)
        path
        (if (nil? (get node position))
          nil
          (recur (conj path position)))))))

(defn delete-value
  "Uses a combination of get-path and delete-node in order to delete a specific value from the tree."
  [tree value]
  (delete-node tree (get-path tree value)))

(defn make-tree
  "Creates a tree from the given values.
  Creates an initial node from the first value, and then passes the rest to add-value using reduce."
  [& values]
  (let [initial-node {:value (first values)
                      :left nil
                      :right nil}]
    (reduce add-value initial-node (rest values))))

(defn in-order-traversal
  "Performs an in-order-traversal on the given tree, returning a vector of the values obtained."
  ([tree]
   (in-order-traversal tree []))
  ([tree result]
   (if tree
     (let [result (in-order-traversal (:left tree) result)
           result (conj result (:value tree))
           result (in-order-traversal (:right tree) result)]
       result)
     result)))

(defn tree-states-main
  "Main function for an implementation using the above to store a list of states and print out the in-order traversal of them."
  [& args]
  (let [states (string/split (slurp "states.txt") #"\r\n")
        state-tree (apply make-tree states)
        traversal (in-order-traversal state-tree)]
    (println traversal)))

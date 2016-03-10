(ns clj-compfund3.linked-lists
  (:require [clj-compfund3.sorting :refer [get-binary-input]]
            [clojure.pprint :refer [pprint]]))

;Technically Clojure lists (aka '()) can be classified as linked lists.
;I'll try reimplementing it here though because I can.

(defn get-count
  ([llist]
   (get-count llist 0))
  ([llist amount]
   (if (not (:next llist))
     (inc amount)
     (recur (:next llist) (inc amount)))))

(defn add-element
  ([llist element index]
   (if (= index 0)
     {:val element
      :next (if (empty? llist)
              nil
              llist)}
     (assoc-in llist (repeat index :next) {:val element
                                           :next (get-in llist (repeat index :next))})))
  ([llist element]
   (add-element llist element 0)))

(defn remove-element [llist element]
  (loop [path []]
    (let [current-node (get-in llist path)
          current-element (:val current-node)]
      (if (not (:next current-node))
        llist
        (if (= element current-element)
          (assoc-in llist path (get-in llist (conj path :next)))
          (recur (conj path :next)))))))

(defn remove-index
  ([llist index]
   (if (= index 0)
     (:next llist)
     (assoc-in llist (repeat index :next) (get-in llist (repeat (inc index) :next)))))
  ([llist]
   (:next llist)))

(defn get-element [llist index]
  (:val (get-in llist (repeat index :next))))

(defn llist-empty? [llist]
  (or (empty? llist) (not (:val llist))))

(defn llist-empty [llist]
  {})

(defn llist->seq [llist]
  (loop [current-node llist
         result []]
    (if (not (:next current-node))
      (conj result (:val current-node))
      (recur (:next current-node) (conj result (:val current-node))))))

(defn llist->str [llist]
  (str (llist->seq llist)))

(defn make-llist [& args]
  (reduce add-element {} (reverse args)))

(defn llist-main [& args]
  (let [data (get-binary-input "./names.dat")
        llist (apply make-llist data)]
    (println "RAW DATA:")
    (pprint llist)
    (println "\nTO STRING:")
    (println (llist->str llist))))

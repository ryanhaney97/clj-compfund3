(ns clj-compfund3.sorting
  (:require [clojure.string :as string])
  (:import [java.io DataInputStream FileInputStream]))

;The find-min and selection-sort functions are part of an implementation of an Selection sort.
;As such, they aren't actually necessary for the problem specified. I just wanted to try implementing it.

(defn find-min [v start-index]
  (let [length (count v)]
    (loop [index (inc start-index)
           smallest start-index]
      (if (= index length)
        smallest
        (recur (inc index) (if (< (compare (get v index) (get v smallest)) 0)
                             index
                             smallest))))))

(defn selection-sort [v]
  (let [indexes (range (dec (count v)))
        per-iteration (fn [current-v index]
                        (let [compare-val (get current-v index)
                              min-index (find-min current-v index)
                              min-val (get current-v min-index)]
                          (if (< (compare min-val compare-val) 0)
                            (assoc current-v
                              index min-val
                              min-index compare-val)
                            current-v)))]
    (reduce per-iteration v indexes)))

;The get-partitions and quicksort functions are an implementation of a Quicksort.
;This is what the program actually uses in order to sort the vector provided.
;Unfortunately, since a quicksort requires you to recursively call on 2 items,
;I was unable to implement tail-end recursion (aka use the recur function) for this implementation.

(defn get-partitions [v]
  (let [pivot (first v)
        per-val (fn [partition-vector current-val]
                  (if (< (compare current-val pivot) 0)
                    (assoc partition-vector 0 (conj (first partition-vector) current-val))
                    (assoc partition-vector 2 (conj (last partition-vector) current-val))))]
    (reduce per-val [[] pivot []] (rest v))))

(defn quicksort [v]
  (if (> 2 (count v))
    v
    (let [partitions (get-partitions v)]
      (apply conj (quicksort (first partitions)) (second partitions) (quicksort (last partitions))))))

;The binary-search function is an implementation of a binary search. Should be self explanatory.

(defn binary-search
  ([v value]
   (binary-search v value 0 (dec (count v))))
  ([v value lower upper]
   (if (<= lower upper)
     (let [index (int (/ (+ lower upper) 2))
           current-val (get v index)]
       (if (= current-val value)
         index
         (recur v value
                (if (< (compare current-val value) 0)
                  (inc index)
                  lower)
                (if (> (compare current-val value) 0)
                  (dec index)
                  upper))))
     -1)))

;The get-binary-input function is what is used to read the binary file provided.
;I'm not proud of how I had to implement this, but I didn't have much of a choice either due to how the file was written.

(defn get-binary-input [file]
  (with-open [in (DataInputStream. (FileInputStream. file))]
    (let [data (atom [])]
      (while (> (.available in) 0)
        (swap! data conj (.readUTF in)))
      @data)))

;The get-val-from-input function reads a value from the console input as a string, with the provided message as a prompt.
;I just copied this from my payroll namespace. I would normally put such a repeated function in a util namespace,
;but I wanted to make it so that you can see all of the functions used in a single namespace for demonstration purposes.

(defn get-val-from-input [message]
  (print message)
  (flush)
  (read-line))

;The data-search function takes the current set of data as a vector. It prompts the user what to search for, searches for it,
;and prints the result if it is found, or says it isn't found if not found.
;In both cases it prompts the user if they want to search again, and calls itself if they do.
;It otherwise stops execution of the program.

(defn data-search [data]
  (let [search-val (string/upper-case (get-val-from-input "Okay. Enter name to search for: "))
        result (binary-search data search-val)]
    (if (= result -1)
      (println (str "The name " (string/capitalize search-val) " was not found in the database."))
      (println (str "The name " (string/capitalize search-val) " was found in the database at position " result ".")))
    (loop []
      (let [answer (get-val-from-input "Would you like to search the database again? (y/n) ")]
        (if (= (first answer) \y)
          (data-search data)
          (if (= (first answer) \n)
            (println "Understood. Terminating program...")
            (do
              (println "ERROR: Invalid entry. Please type in 'y' or 'n' as an answer.")
              (recur))))))))

;The prompt-search function is similar to the data-search function, but is used for the first prompt (I could probably rewrite this, but it works).

(defn prompt-search [data]
  (let [answer (get-val-from-input "Would you like to search for a name in the database? (y/n) ")]
    (if (= (first answer) \y)
      (data-search data)
      (if (= (first answer) \n)
        (println "Understood. Terminating program...")
        (do
          (println "ERROR: Invalid entry. Please type in 'y' or 'n' as an answer.")
          (recur data))))))

;The sorting-main function reads input from a file in the same folder as this program (or jar file) from a file named "names.dat".
;This data is read into a database and quicksorted, before being printed out to the console.
;It then calls prompt-search to ask the user if they want to search the database.

(defn sorting-main []
  (let [data (get-binary-input "./names.dat")
        sorted-data (quicksort data)]
    (println (str "Data:\n\n[" (apply str (interpose ", " (map string/capitalize sorted-data))) "]\n"))
    (prompt-search sorted-data)))

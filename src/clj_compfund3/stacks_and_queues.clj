(ns clj-compfund3.stacks-and-queues)

(defn alt-reverse
  "Could just use the reverse function, but I'll reimplement it to demonstrate how Clojure lists act like stacks"
  [coll]
  (reduce conj '() coll))

(defn convert
  "No need to use a queue pretty much ever in Clojure, because we have map and reduce.
  Remember that in Clojure, strings are also collections, just like vectors and lists."
  [string]
  (apply str (map #(Character/toUpperCase %1) string)))

(defn same?
  "In this case, I could just do an equals (=) check.
  However, for the sake of demonstration, I'll do a check the intended way."
  [data1 data2]
  (empty? (reduce #(if (= (first %1) %2)
                     (rest %1)
                     (reduced [1])) data1 data2)))

(defn get-val-from-input
  "Reads a value from the console input as a string, with the provided message as a prompt.
   I just copied this from my payroll and sorting namespaces. I would normally put such a repeated function in a util namespace,
   but I wanted to make it so that you can see all of the functions used in a single namespace for demonstration purposes."
  [message]
  (print message)
  (flush)
  (read-line))

(def operations
  {"reverse" alt-reverse
   "convert" convert
   "compare" same?})

(defn stacks-and-queues-main [& args]
  (let [operation (get-val-from-input "Enter an operation (reverse, convert, or compare): ")
        operation-fn (get operations operation)]
    (if operation-fn
      (if (= operation "compare")
        (let [file1 (get-val-from-input "Enter the first file name: ")
              file2 (get-val-from-input "Enter the second file name: ")
              data1 (slurp file1)
              data2 (slurp file2)
              result (operation-fn data1 data2)]
          (if result
            (println "The two files were equal.")
            (println "The two files were not equal.")))
        (let [file (get-val-from-input "Enter file name: ")
              data (slurp file)
              result (apply str (operation-fn data))
              new-file (str operation "_result.txt")]
          (spit new-file result)
          (println (str "Result saved to file " new-file))))
      (do
        (println "Invalid operation, please enter a valid operation.")
        (recur args)))))

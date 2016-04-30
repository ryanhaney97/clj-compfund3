(ns clj-compfund3.core
  (:require
   [clj-compfund3.payroll :refer [payroll-main]]
   [clj-compfund3.palindrome :refer [palindrome-main]]
   [clj-compfund3.sorting :refer [sorting-main]]
   [clj-compfund3.num-analyzer :refer [num-main]]
   [clj-compfund3.linked-lists :refer [llist-main]]
   [clj-compfund3.stacks-and-queues :refer [stacks-and-queues-main]]
   [clj-compfund3.tree-states :refer [tree-states-main]]
   [clj-compfund3.employee-map :refer [employee-map-main]])
  (:gen-class))

(defn -main [& args]
  (let [mains [payroll-main
               palindrome-main
               sorting-main
               num-main
               llist-main
               stacks-and-queues-main
               tree-states-main
               employee-map-main]
        chosen-main (if (empty? args)
                      (last mains)
                      (get mains (dec (Integer/parseInt (first args)))))]
    (apply chosen-main (rest args))))

(ns clj-compfund3.core
  (:require
   [clj-compfund3.payroll :refer [payroll-main]]
   [clj-compfund3.palindrome :refer [palindrome-main]]
   [clj-compfund3.sorting :refer [sorting-main]]
   [clj-compfund3.num-analyzer :refer [num-main]]
   [clj-compfund3.linked-lists :refer [llist-main]]
   [clj-compfund3.stacks-and-queues :refer [stacks-and-queues-main]])
  (:gen-class))

(defn -main [& args]
  (let [mains [payroll-main
               palindrome-main
               sorting-main
               num-main
               llist-main
               stacks-and-queues-main]
        choosen-main (if (empty? args)
                       (last mains)
                       (get mains (dec (Integer/parseInt (first args)))))]
    (apply choosen-main (rest args))))

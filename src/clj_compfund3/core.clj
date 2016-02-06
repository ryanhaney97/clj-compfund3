(ns clj-compfund3.core
  (:require
   [clj-compfund3.payroll :refer [payroll-main]]
   [clj-compfund3.palindrome :refer [palindrome-main]]
   [clj-compfund3.sorting :refer [sorting-main]])
  (:gen-class))

(defn -main [& args]
  ;(payroll-main)
  ;(apply palindrome-main args)
  (sorting-main))

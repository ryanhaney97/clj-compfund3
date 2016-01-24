(ns clj-compfund3.core
  (:require
   [clj-compfund3.payroll :refer [payroll-main]]
   [clj-compfund3.palindrome :refer [palindrome-main]])
  (:gen-class))

(defn -main [& args]
  ;(payroll-main)
  (apply palindrome-main args)
  )

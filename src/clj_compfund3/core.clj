(ns clj-compfund3.core
  (:require
   [clj-compfund3.payroll :refer [payroll-main]]
   )
  (:gen-class))

(defn -main [& args]
  (payroll-main)
  )

(ns clj-compfund3.payroll)

(defn get-val-from-input [message]
  (print message)
  (flush)
  (read-line))

(defrecord Payroll
  [name id hours rate])

(defn num-letters [string]
  (count (filter #(Character/isLetter %1) string)))

(defn num-digits [string]
  (count (filter #(Character/isDigit %1) string)))

(defn error [message]
  (println (str "\n" message "\n"))
  (System/exit 1))

(defn check-payroll [payroll]
  (if (empty? (:name payroll))
    (error "ERROR: Name should not be empty."))
  (if (not (and (= (num-digits (:id payroll)) 4) (= (num-letters (:id payroll)) 2)))
    (error "ERROR: ID should be in the form of \"LLNNNN\" where L is a letter and N is a number."))
  (if (not (and (<= 0 (:hours payroll)) (<= (:hours payroll) 84)))
    (error "ERROR: Hours should be between 0 and 84."))
  (if (not (and (<= 0 (:rate payroll)) (<= (:rate payroll) 25.00)))
    (error "ERROR: Rate should be between 0 and 25.00."))
  payroll)

(defn get-input []
  (let [[employee-name id hours rate] (doall (map get-val-from-input ["Enter employee name: "
                                                                      "Enter employee ID (in the form of \"LLNNNN\"): "
                                                                      "Enter employee hours: "
                                                                      "Enter employee rate: "]))
        hours (apply str (filter #(Character/isDigit %1) hours))
        hours (if (empty? hours)
                0
                (Integer/parseInt hours))
        rate (apply str (filter #(or (Character/isDigit %1) (= \. %1)) rate))
        rate (if (empty? rate)
               0
               (Double/parseDouble rate))
        payroll (Payroll. employee-name id hours rate)]
    (check-payroll payroll)))

(defn payroll-main [& args]
  (println (get-input)))

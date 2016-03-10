(ns clj-compfund3.num-analyzer)

(def nums (atom []))

(defn add-nums!
  [& args]
  (let [make-new-nums (fn [current-nums added-nums]
                        (into [] (concat current-nums (filter number? (flatten added-nums)))))]
    (swap! nums make-new-nums args)))

(defn max-num
  ([some-nums]
   (apply max some-nums))
  ([]
   (max-num @nums)))

(defn min-num
  ([some-nums]
   (apply min some-nums))
  ([]
   (min-num @nums)))

(defn total
  ([some-nums]
   (apply + some-nums))
  ([]
   (total @nums)))

(defn average
  ([some-nums]
   (let [total-nums (total some-nums)]
     (/ total-nums (count some-nums))))
  ([]
   (average @nums)))

(defn print-analysis [analysis-name coll]
  (println (str analysis-name ":\nMin: " (min-num coll) "\nMax: " (max-num coll) "\nTotal: " (total coll) "\nAverage: " (average coll))))

(defn num-main [& args]
  (let [double-coll [0.1 1.2 2.3 3.4 0.01 3.5 6.5 5.9 1.0]
        int-coll [1 2 3 34 10 35 65 59 -6]]
    (print-analysis "Double Analysis" double-coll)
    (print-analysis "Integer Analysis" int-coll)))

(ns clj-compfund3.palindrome)

;I could totally cheat here and use Clojure's reverse function and then just do an equals check,
;but I will use recursion since that is what is intended. As such, reverse will not be used in this function at all.

(defn palindrome? [s]
  (let [end-index (dec (count s))]
    (if (< end-index 1) ;Base case for this being a palindrome (if it gets to the point where the string is empty or almost empty, it has succeeded).
      true
      (if (not= (first s) (last s)) ;Base case for this not being a palindrome (if the first and last letters are not equal, stops recursion).
        false
        (recur (subs s 1 end-index))))));Recuring statement. The function "recur" is a Clojure function to provide for tail-end recursion. Because of this, the code won't crash due to stack-overflow.

;This function is just for the println messages.
(defn check-string [s]
  (if (palindrome? s)
    (println (str "The word " s " is a palindrome."))
    (println (str "The word " s " is NOT a palindrome.")))
  s)

;I map the check over multiple types of words in order to test this palindrome checker.
;I use "doall" to realize the lazy sequence map creates, so that the println statements in check-string run.
(defn palindrome-main [& args]
  (doall (map check-string (concat ["abcba" "abba" "not a palindrome" "also not a palindrome" "qwertytrewq"] args))))

;In case anyone is interested, here's the cheap way of doing this without any looping/recursion at all
;(except under the hood for the reverse function):

;(defn palindrome? [s]
;  (= s (apply str (reverse s))))

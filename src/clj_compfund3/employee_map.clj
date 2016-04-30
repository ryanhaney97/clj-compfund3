(ns clj-compfund3.employee-map)

(defn add-employee
  "Adds an Employee to the provided hash-map, takes a name and id."
  ([e-map id ename]
   (assoc e-map id {:id id :name ename})))

;This is a macro from my forge-clj project, that will make the following inter-op a little bit cleaner,
;and will also help demonstrate macros a bit.

;I PUT A LITTLE TUTORIAL HERE, SKIP IF YOU WANT.
;-----------------------------------------------

;...Oh, you probably don't know what a macro even IS, don't you. Well, it's basically code that returns code. A code generator if you will.
;Simply put, Clojure's syntax is also "written" in Clojure. For example, a function call like (+ 1 2) is actually a Clojure list, containing +, 1, and 2.
;The compiler then evaluates this list, by calling the + function with the arguements 1 and 2.
;Macros, on the other hand, are able to intervene between when this is a list and when it gets evaluated.
;They receive unevaluated lists and symbols, and are expected to return another unevaluated list, which will actually get evaluated.
;As an example, here's a simple example of a simple function that allows you to write in "infix" (only basic infix, more complex infix won't work here).

(defmacro infix-example [call]
  `(~(second call) ~(first call) ~@(drop 2 call)))

;A call to it can be made like so:

(infix-example (1 + 2))

;I'll go ahead and explain some of the weird syntax real quick as well.
;First off, realize that ' means to evaluate this as a symbol, or series of symbols. In other words, if you simply type foo, Clojure will look up what the symbol foo is bound to.
;Writing it as 'foo instead means you just want the raw symbol, or unevaluated name, before Clojure looks it up.
;A ` character is the same, except that it includes the full namespace with it. So 'foo becomes a symbol named foo, and `foo becomes, in this case, the full symbol clj-compfund3.employee-map.foo.
;It also allows the usage of the ~ character inside of it, which means to evaluate a certain piece of this symbol right now.
;~@ is the same as ~, but if the result is a collection, it "removes" the collection, placing each value individually in there.

;Oh, and finally, a neat thing you can do with a macro is use Clojure's macroexpand function, which will let you see the unevaluated list that gets returned by our macro:

(macroexpand '(infix-example (1 + 2)))

;If you don't have the repl on, that results in (+ 1 2).

;...Sorry if this went a bit over your head, as this is a rather basic explanation for a rather complex subject.
;If you need a better explanation, check out http://www.braveclojure.com/read-and-eval/ and http://www.braveclojure.com/writing-macros/ for a better (and more entertaining) explanation of this.

;Tutorial End
;-------------

;Anyways, as I said earlier, the following is a macro I will be using with the inter-op further down.

(defmacro with-prefix
  "Useful macro that takes a prefix (both strings and symbols work) and any number of statements.
  For each def/defn/def-/defn- statement within the macro, adds the prefix onto the name in each statement."
  [prefix & defs]
  (let [per-def (fn [possible-def]
                  (if (or (= (first possible-def) 'def) (= (first possible-def) 'defn) (= (first possible-def) 'def-) (= (first possible-def) 'defn-) (= (first possible-def) `def) (= (first possible-def) `defn) (= (first possible-def) `defn-))
                    (let [first-val (first possible-def)
                          def-name (second possible-def)
                          def-name (symbol (str prefix def-name))
                          def-statement (cons first-val (cons def-name (rest (rest possible-def))))]
                      def-statement)
                    possible-def))
        def-statements (cons `do (map per-def defs))]
    def-statements))

;Gonna try and demonstrate a bit of Java inter-op here.
;Here I will demonstrate how to use gen-class to generate a Java class for what I've made thus far.
(gen-class
 :name clj_compfund3.employee_map.EmployeeMap ;defines the name, including the package name.
 :state state ;Binds a name to a single global state.
 :init init ;The name of the method that will be called
 :constructors {[] []} ;A series of constructors for our class. The first vector contains a bunch of types, while the second contains types for the superclass constructor (if any).
 ;The following vectors define various method heads for our class. Consists of a name, a vector of argument types, and a return type (could be void).
 :methods [[add [String String] void]
           [get [String] clojure.lang.PersistentArrayMap]]
 :prefix emap-) ;Specifies a prefix for our class. All methods will correspond to functions with the name of prefix+name, e.g. in this case, init would become emap-init.

;Defines our "methods" for the generated class. with-prefix is a macro I created that tacks on the provided prefix to the begining of declarations.
;So init actually gets defined as emap-init in this instance.
(with-prefix emap-
  (defn init
    "A function that takes constructor arguments. Returns a vector containing a vector of superclass arguments,
    and then the initial global state. By initializing it as an atom wrapped around a hash-map, we're able to change it over time."
    []
    [[] (atom {})])
  (defn add
    "Defines the add function for this class.
    Simply calls my add-employee function with the provided arguments, and then swaps the new value into our global state."
    [^clj_compfund3.employee_map.EmployeeMap this & args]
    (apply swap! (.-state this) add-employee args))
  (defn get
    "Defines the get function for this class. I simply perform a get on the global state."
    [^clj_compfund3.employee_map.EmployeeMap this id]
    (get @(.-state this) id))
  (defn toString
    "Get string representation of this object."
    [^clj_compfund3.employee_map.EmployeeMap this]
    (str @(.-state this))))

(import clj_compfund3.employee_map.EmployeeMap)

;You can now treat EmployeeMap as a Java class.
;Also, assuming this namespace is on your classpath, you will also be able to access EmployeeMap from raw Java, via
;import clj_compfund3.employee_map.EmployeeMap
;And then you can simply use it like normal.

(defn get-entry [^EmployeeMap emap entry]
  (println (str "Getting " entry ": " (if (.get emap entry)
                                        (.get emap entry)
                                        "NOT FOUND"))))

(defn employee-map-main [& args]
  (let [emap (doto
               (EmployeeMap.)
               (.add "100100" "Steve")
               (.add "221012" "Jon")
               (.add "325032" "Bill"))]
    (println emap)
    (get-entry emap "100100")
    (get-entry emap "221012")
    (get-entry emap "000000")
    (dorun (map (partial get-entry emap) args))))

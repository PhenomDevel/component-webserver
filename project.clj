(defproject de.phenomdevel/component-webserver "1.0.1"
  :dependencies
  [[org.clojure/clojure "1.10.1"]
   [org.clojure/core.async "1.2.603"]

   ;; Webserver
   [http-kit "2.4.0"]

   ;; Misc
   [com.taoensso/timbre "4.10.0"]
   [com.stuartsierra/component "1.0.0"]]

  :source-paths
  ["src/clj"]

  :profiles
  {:dev
   {:source-paths
    ["dev"]

    :dependencies
    [[compojure "1.6.2"]]}})

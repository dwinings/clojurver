(defproject clojurver "1.0.0-SNAPSHOT"
  :description "A simple webserver written in Clojure"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [server-socket "1.0.0"]
                 [log4j "1.2.5"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/tools.logging "0.2.6"]]
  :main clojurver.core/-main
  :source-paths ["test"]
  :test-paths ["src"])

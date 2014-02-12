(ns clojurver.core
  (:use [clojurver.routing]
        [server.socket]
        [clojure.string :only (join split lower-case)])
  (:require [clojure.tools.logging :as logging]))

(import '(java.io PrintWriter InputStreamReader PrintWriter) '(Java.net.ServerSocket))

(def server (atom nil))

(defn parse-request []
  (loop
    [result (zipmap [:method :path :protocol] (split (read-line) #"\s"))
     line (split (read-line) #"\s")]
    (if (empty? line)
      result
      (recur (assoc result
               (keyword (first line))
               (last line))
             (read-line)))))



(defn stop-server []
  (close-server @server)
  (reset! server nil))

(defn start-server []
  (reset! server (create-server 8080
                                (fn [in out]
                                  (binding [*in* (java.io.BufferedReader. (java.io.InputStreamReader. in))
                                            *out* (java.io.PrintWriter. out)]
                                    (let [request (parse-request)]
                                      (controller request)
                                      (flush)))))))

(defn restart-server []
  (if-not (nil? @server)
    (close-server @server))
  (start-server))


(defn -main []
  (start-server))

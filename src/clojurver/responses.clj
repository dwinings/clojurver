(ns clojurver.responses
  (:use [clojure.string :only (join)]
        [clojurver.file]))

(def response-ok { :status-line "HTTP/1.0 200 OK"
                   :headers {:Content-Type "text/html"}})

(defn response-ok-asset [asset-type]
  { :status-line "HTTP/1.0 200 OK"
    :headers {:Content-Type (str "text/" asset-type)}})

(def response-not-found {:status-line "HTTP/1.0 404 Not Found"
                         :headers {:Content-Type "text/html"}})


(defn send-response [response body]
  (let [headers (assoc (response :headers)
                  :Content-Length
                  (count body))]
    (println (response :status-line))
    (println
     (join
      (for [header (keys headers)]
        (format "%s: %s\n" (name header) (headers header)))))
    (println body)))

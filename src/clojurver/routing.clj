(ns clojurver.routing
  (:use [clojurver.responses]
        [clojurver.file]
        [clojure.string :only (split)]))

(def routes {"/" "index"
             "/about" "about"
             "/contact" "contact"})

(defn defroute [route page]
  (assoc routes route page))


(defmulti controller
  (fn [request]
    (let [path (request :path)]
      (cond (or (= (ftype path) "css") (= (ftype path) "js")) :asset
            (boolean (routes path)) :html
            :else :default))))

(defmethod controller :html [request]
  (if (routes (request :path))
    (send-response response-ok (slurp (str "html/" (routes (request :path)) ".html")))
    (send-response response-not-found (slurp "html/404.html"))))


(defmethod controller :asset [request]
  (let [fname (str (ftype (request :path)) "/" (request :path))]
    (if (file-exists? fname)
      (send-response (response-ok-asset (ftype fname)) (slurp fname))
      (send-response response-not-found ""))))

(defmethod controller :default [request]
  (send-response response-not-found (slurp "html/404.html")))

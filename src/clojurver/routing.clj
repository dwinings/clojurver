(ns clojurver.routing
  (:use [clojurver.responses]
        [clojurver.file]
        [clojurver.rendering]
        [clojure.string :only (split)])
  (:require [clojure.tools.logging :as logging]))

(def routes {"/" "index"
             "/about" "about"
             "/contact" "contact"})

(defn defroute [route page]
  (def routes (assoc routes route page)))

(defmulti controller
  (fn [request]
    (let [path (request :path)]
      (cond (or (= (ftype path) "css") (= (ftype path) "js")) :asset
            (boolean (routes path)) :html
            :else :default))))

(defmethod controller :html [request]
  (let [asset-name (routes (request :path))]
    (if asset-name
      (let [filename (cond (file-exists? (str "html/" asset-name ".html")) (str "html/" asset-name ".html")
                           (file-exists? (str "html/" asset-name ".html.eclj")) (str "html/" asset-name ".html.eclj")
                           :default "404.html")]
        (send-response response-ok (render filename)))
      (do (logging/info "Asset not found.") (send-response response-not-found (slurp "html/404.html"))))))


(defmethod controller :asset [request]
  (let [fname (str (ftype (request :path)) "/" (request :path))]
    (if (file-exists? fname)
      (send-response (response-ok-asset (ftype fname)) (slurp fname))
      (send-response response-not-found ""))))

(defmethod controller :default [request]
  (send-response response-not-found (slurp "html/404.html")))

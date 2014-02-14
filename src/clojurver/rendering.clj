(ns clojurver.rendering
  (:use [clojurver.file]
        [clojure.data.zip.xml :only (attr text xml->)])
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.tools.logging :as logging]))

(defn render-partial [filename]
  (->> filename
      (str "html/partials/")
      (xml/parse)))

(defn search-replace [zipper predicate mutator]
  "I think I finally am beginning to start to understand zippers."
  (loop [loc zipper]
    (if (zip/end? loc)
      (zip/root loc)
      (recur (zip/next
              (if (and (zip/branch? loc) (predicate (zip/node loc)))
                (zip/edit loc mutator)
                loc))))))

(defn render [filename]
  "This function expects an eclj file"
  (logging/debugf "Rendering %s" filename)
  (if (= (ftype filename) "eclj")
    (let [zipper (-> filename
                     (xml/parse)
                     (zip/xml-zip))
          predicate #(= (%1 :tag) :embed)
          mutator #(->> (%1 :content) (first) (read-string) (eval))]
      ;; Why the hell would xml/emit default to emitting on *out*?...
      (with-out-str (xml/emit (search-replace zipper predicate mutator))))
    (slurp (str filename))))

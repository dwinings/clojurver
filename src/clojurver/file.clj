(ns clojurver.file
  (:use [clojure.java.io :only (file)]
        [clojure.string :only (split)]))


(defn file-exists? [name]
  (let [fd (file name)]
    (.exists fd)))

(defn ftype [name]
  (last (split name #"\.")))

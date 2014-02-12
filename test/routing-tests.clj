(ns clojurver.test.routing
  (:use [clojure.test]
        [clojurver.routing]))

(deftest test-add-route
  ; Test whether or not we can add a route.
    (defroute "/test" "test.html")
    (is (= "test.html" (routes "/test"))))

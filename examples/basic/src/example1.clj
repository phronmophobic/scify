(ns example1
  (:require [sci.core :as sci]
            clojure.pprint
            clojure.zip
            [com.phronemophobic.scify :as scify]))

(def ctx (sci/init {:namespaces
                    (merge
                     (scify/ns->ns-map 'clojure.pprint)
                     (scify/ns->ns-map 'clojure.zip))}))

(sci/eval-form ctx
               '(clojure.pprint/pprint {}))
;; stdout => {}

(sci/eval-form ctx
               '(do
                  (require '[clojure.zip :as z])
                  (-> (z/vector-zip [[0]])
                      z/down
                      z/down
                      (z/edit inc)
                      z/root)))
;; [[1]]


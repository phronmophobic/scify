(ns example2
  (:require [sci.core :as sci]
            [clojure.core.reducers :as r]
            [com.phronemophobic.scify :as scify]))

(defrecord MyFoldable []
  r/CollFold
  (coll-fold
    [coll n combinef reducef]
    42))

(def ctx (sci/init {:namespaces
                    (merge
                     (do
                       ;; modify protocols in
                       ;; clojure.core.reducers to accept sci values or non-sci values
                       (scify/scify-ns-protocol 'clojure.core.reducers)
                       (scify/ns->ns-map 'clojure.core.reducers)))}))

(sci/eval-form ctx
               '(do
                  (require '[clojure.core.reducers :as r])

                  (defrecord MyFoldable2 []
                    r/CollFold
                    (coll-fold
                      [coll n combinef reducef]
                      -42))

                  (r/coll-fold (->MyFoldable2) nil nil nil )))
;; -42

(r/coll-fold (->MyFoldable) nil nil nil )
;; 42



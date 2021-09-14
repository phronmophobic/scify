# scify

Simplify exposing namespaces to sci contexts.

## Usage

Add clojure.pprint and clojure.zip namespaces to sci context

```clojure
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
```

By default protocols don't work with sci values, but can be swizzled to allow for usage and extension within sci.

```clojure
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

```

## License

Copyright © 2021 Adrian Smigh

Distributed under the Eclipse Public License version 1.0.

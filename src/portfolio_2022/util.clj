(ns portfolio-2022.util
  (:require [clojure.edn :as edn]
            [taoensso.tower :as tower] 
            [clojure.java.io :as io]))

(def tower-config (edn/read-string (slurp (io/resource "tower-config.edn"))))
(def t (tower/make-t tower-config)) 

(ns portfolio-2022.util
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [taoensso.tower :as tower])) 

(def tower-config (edn/read-string (slurp (io/resource "tower-config.edn"))))
(def t (tower/make-t tower-config)) 

(defn locale-to-supported-locale 
  "Takes any locale and returns lang that this website supports.
   Returns English as default."
  [locale]
  (let [locale-str (name locale)]
    (condp s/starts-with? locale-str
      "en" :en
      "ja" :ja
      :en)))

(defn work-experience-path [locale]
  (io/resource (format "work_experience/%s.edn" (name (locale-to-supported-locale locale))))) 

(defn work-experience [locale]
  (edn/read-string (slurp (work-experience-path locale))))

(def menu-options
  [{:url "/" :name "home"}
   {:url "/about" :name "about"}
   {:url "/works" :name "works"}
   {:url "/terminal" :name "terminal"}])

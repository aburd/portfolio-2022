(ns portfolio-2022.handlers.home
  (:require [hiccup.core :refer [html]]))

(defn handler [req] 
  (let [u (or (-> req :params :user) "my guy")]
    [:h1 ((:t' req) :greeting u)]))

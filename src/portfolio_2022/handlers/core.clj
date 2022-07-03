(ns portfolio-2022.handlers.core
  (:require 
    [portfolio-2022.views.core :as views]
    [portfolio-2022.views.home :as home-views]
    [clojure.string :as s]))

(defn html-200 
  "Returns a function which will return a valid html ring response."
  [html-fn]
  (fn [req] 
    {:status 200
     :headers {"Content-Type" "text/html; charset=UTF-8"}
     :body (html-fn req)})) 
    
(def home (html-200 (fn [req] (views/default-page-view home-views/show req))))

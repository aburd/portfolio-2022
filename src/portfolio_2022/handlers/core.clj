(ns portfolio-2022.handlers.core
  (:require 
    [ring.util.response :refer [redirect]]
    [portfolio-2022.views.core :as views]
    [portfolio-2022.views.home :as home-views]
    [portfolio-2022.views.about :as about-views]
    [portfolio-2022.views.contact :as contact-views]
    [portfolio-2022.views.not-found :as not-found-views]
    [clojure.string :as s]))

(defn html-200 
  "Returns a function which will return a valid html ring response."
  [html-fn]
  (fn [req] 
    {:status 200
     :headers {"Content-Type" "text/html; charset=UTF-8"}
     :body (html-fn req)})) 
    
(def home (html-200 (fn [req] (views/default-page-view home-views/show req)))) 

(def about (html-200 (fn [req] (views/default-page-view about-views/show req))))

(def contact (html-200 (fn [req] (views/default-page-view contact-views/show req))))

(def not-found 
  {:status 404
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body (views/html-view-wrap (not-found-views/show))})

(defn change-locale 
  "Handles when user wants to change preferred locale."
  [req]
  (let [locale (get (:form-params req) "locale")
        path (get (:form-params req) "redirect-uri")]
    (merge (redirect path) {:cookies {:locale locale}})))

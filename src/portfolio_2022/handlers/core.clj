(ns portfolio-2022.handlers.core
  (:require 
    [portfolio-2022.views.core :as views]
    [portfolio-2022.views.pages :as pages]
    [ring.util.response :refer [redirect]]))

(defn html-200 
  "Returns a function which will return a valid html ring response."
  [body]
  {:status 200
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body body}) 
    
(defn home 
  [req]
  (html-200 
    (views/default-page-view req pages/home))) 

(defn about
  [req] 
  (html-200 
    (views/default-page-view req pages/about)))

(defn work-history
  [req] 
  (html-200 
    (views/default-page-view req pages/work-history)))

(defn terminal
  [req] 
  (html-200 
    (views/default-page-view req pages/terminal)))

(defn contact 
  [req]
  (html-200 
    (views/default-page-view req pages/contact)))

(defn change-locale 
  "Handles when user wants to change preferred locale."
  [req]
  (let [locale (get (:form-params req) "locale")
        path (get (:form-params req) "redirect-uri")]
    (merge (redirect path) {:cookies {:locale locale}})))

(def not-found 
  {:status 404
   :headers {"Content-Type" "text/html; charset=UTF-8"}
   :body (views/html-view-wrap (pages/not-found))})


(ns portfolio-2022.handlers.home
  (:require [hiccup.core :refer [html]]))

(defn handler [req] 
  [:div {:class "page-home"} 
   [:h1 {:class "page-tag-line"} ((:t' req) :my-name)]
   [:h2 {:class "page-sub-tag-line"} ((:t' req) :work-title)]
   [:div {:class "page-cover"} [:img {:src "/myself.jpeg"}]]
   [:p {:class "page-body-text"} ((:t' req) :about-me)]])
    

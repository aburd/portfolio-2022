(ns portfolio-2022.views.home
  (:require [hiccup.core :refer [html]]))

(defn show [req] 
  [:div {:class "page-home"} 
   [:h1 {:class "page-tag-line"} ((:t' req) :my-name)]
   [:h2 {:class "page-sub-tag-line"} ((:t' req) :work-title)]
   [:div {:class "page-cover"} [:img {:src "/myself.jpeg"}]]
   [:h3 "About Me"]
   [:p {:class "page-body-text"} ((:t' req) :about-me)]
   [:h3 "About This Website"]
   [:p {:class "page-body-text"} ((:t' req) :about-website)]])
    

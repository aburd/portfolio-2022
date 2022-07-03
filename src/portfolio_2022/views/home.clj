(ns portfolio-2022.views.home
  (:require [hiccup.core :refer [html]]))

(defn show [req] 
  (let [t (:t req)
        locale (:locale req)]
    [:div {:class "page-home"} 
     [:h1 {:class "page-tag-line"} (t locale :my-name)]
     [:h2 {:class "page-sub-tag-line"} (t locale :work-title)]
     [:div {:class "page-cover"} [:img {:src "/myself.jpeg"}]]
     [:p {:class "page-body-text"} (t locale :about-me)]]))
    

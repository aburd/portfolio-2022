(ns portfolio-2022.views.not-found
  (:require 
    [portfolio-2022.util :refer [t]]))

(defn show [] 
  (let [locale :en]
    [:div {:class "page-not-found center"} 
     [:h1 {:class "page-tag-line"} (t locale :not-found-title)]
     [:h3 {:class "page-sub-tag-line"} (t locale :not-found-subtitle)]
     [:div [:a {:href "/"} (t locale :ui/nav/home)]]
     [:div {:class "page-cover"} [:img {:src "https://picsum.photos/200"}]]]))
    


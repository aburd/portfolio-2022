(ns portfolio-2022.views.about
  (:require [hiccup.core :refer [html]]))

(defn show [req] 
  (let [t (:t req)
        locale (:locale req)]
    [:div {:class "page-about"} 
     [:h3 (t locale :about-website-title)]
     [:p {:class "page-body-text"} (t locale :about-website)]
     [:iframe
      {:allowfullscreen "allowfullscreen",
       :allow
       "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture",
       :frameborder "0",
       :title "YouTube video player",
       :src "https://www.youtube.com/embed/SxdOUGdseq4",
       :height "315",
       :width "560"}]]))
    

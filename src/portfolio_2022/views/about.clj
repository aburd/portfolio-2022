(ns portfolio-2022.views.about
  (:require [hiccup.core :refer [html]])
  (:require [clojure.string :as s]))

(defn show [req] 
  (let [t (:t req)
        locale (:locale req)]
    [:div {:class "page-about"} 
     [:h1 (t locale :ui/nav/about)]
     [:h3 (t locale :about-website-title)]
     [:p {:class "page-body-text"} (t locale :about-website)]
     [:h3 (t locale :about-want-title)]
     [:p {:class "page-body-text"} (t locale :about-want)]
     [:h3 (t locale :about-like-title)]
     [:p {:class "page-body-text"} (t locale :about-like)]
     [:ul
      [:li "Static Types"]
      [:li "Dynamic Types"]
      [:li
       "\"Simple\" systems"
       [:br]
       [:iframe
        {:allowfullscreen "allowfullscreen",
         :allow
         "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture",
         :frameborder "0",
         :title "YouTube video player",
         :src "https://www.youtube.com/embed/SxdOUGdseq4",
         :height "315",
         :width "560"}]]
      [:li "Functional programming"]
      [:li "Minimizing state"]
      [:li "Robust programs"]
      [:li [:a {:href "https://en.wikipedia.org/wiki/Unix_philosophy"} "Unix Philosophy"]]
      [:li "Code that serves human-beings"]]
     [:h3 (t locale :about-dislike-title)]
     [:p {:class "page-body-text"} (t locale :about-dislike)]
     [:ul
      [:li "Large amounts of state in programs"]
      [:li "Using tools that aren't necessary"]
      [:li "Building things without user-input"]]]))
    

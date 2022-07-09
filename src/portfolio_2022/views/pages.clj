(ns portfolio-2022.views.pages
  (:require 
    [portfolio-2022.views.components :as c]
    [portfolio-2022.util :refer [t]]))

(defn home [req] 
  (let [locale (:locale req)]
    [:div {:class "page-home"} 
     [:h1 {:class "page-tag-line"} (t locale :my-name)]
     [:h3 {:class "page-sub-tag-line"} (t locale :work-title)]
     [:div {:class "page-cover"} [:img {:src "/myself.jpeg"}]]
     [:p {:class "page-body-text"} (t locale :about-me)]]))

(defn about [req] 
  (let [locale (:locale req)]
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

(defn work-history [req work-history] 
  (let [locale (:locale req)]
    [:div {:class "page-work-history"} 
     [:h1 {:class "page-tag-line"} (t locale :work-history-title)]
     [:ul {:class "work-list"}
      (for [work work-history]
        [:li (c/work-item work)])]]))

(defn terminal [req] 
  (let [locale (:locale req)]
    [:div {:class "page-terminal"} 
     [:h1 {:class "page-tag-line"} (t locale :terminal-title)]
     [:div {:id "terminal"}]]))

(defn contact [req] 
  (let [{locale :locale} req]
    [:div {:class "page-contact"} 
     [:h1 {:class "page-tag-line"} (t locale :contact-title)]
     [:form {:class "form-contact"}
      (c/form-group 
        (t locale :ui/name)
        [:input {:type "text" :name "name"}])
      (c/form-group 
        (t locale :ui/email)
        [:input {:type "text" :name "email"}])
      (c/form-group 
        (t locale :ui/comments)
        [:textarea {:name "comments"}])]]))

(defn not-found [] 
  (let [locale :en]
    [:div {:class "page-not-found center"} 
     [:h1 {:class "page-tag-line"} (t locale :not-found-title)]
     [:h3 {:class "page-sub-tag-line"} (t locale :not-found-subtitle)]
     [:div [:a {:href "/"} (t locale :ui/nav/home)]]
     [:div {:class "page-cover"} [:img {:src "https://picsum.photos/200"}]]]))

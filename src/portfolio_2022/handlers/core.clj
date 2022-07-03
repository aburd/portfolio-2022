(ns portfolio-2022.handlers.core
  (:require 
    [portfolio-2022.handlers.home :as home]
    [clojure.string :as s]
    [hiccup.core :refer [html]]))

(defn html-view-wrap [inner-html]
  (html [:html
         [:head
          [:meta {:name "viewport" :content "width: device-width, initial-scale: 1.0"}]]
         [:link {:rel "stylesheet" :href "/styles.css"}] 
         [:body inner-html]
         [:script {:src "/js/main.js"}]]))

(defn html-200 
  "Returns a function which will return a valid html ring response."
  [html-fn]
  (fn [req] 
    {:status 200
     :headers {"Content-Type" "text/html; charset=UTF-8"}
     :body (html-view-wrap (html-fn req))})) 

(defn nav 
  "Returns a nav hiccup navbar with some list of options.
  The `active` arg will add an 'active' class."
  [options active]
  [:nav {:class "main-nav"}
   [:ul
    (for [option options]
      [:li
       [:a {:href (:url option)} (s/capitalize (:text option))]])]])

(defn switch
  "Makes a toggle switch"
  []
  [:label {:class "switch"}
   [:input {:type "checkbox"}]
   [:span {:class "slider round"}]])

(defn social
  "Returns social icons"
  []
  [:div {:class "social-icons"}
   [:ul
    [:li 
     [:a {:href "https://www.github.com/aburd"}
      [:img {:src "/github.png"}]]]
    [:li 
     [:a {:href "mailto:aaron.burdick@protonmail.com"}
      [:img {:src "/email.png"}]]]
    [:li
      [:div {:class "container-themes"} (switch)]]]])

(defn page-wrapper 
  [req html-fn]
  (let [t (:t' req)
        nav-options [{:url (str "/" "home") :text (t :ui/nav/home)}
                     {:url (str "/" "works") :text (t :ui/nav/works)}
                     {:url (str "/" "terminal") :text (t :ui/nav/terminal)}
                     {:url (str "/" "contact") :text (t :ui/nav/contact)}]]
    [:div {:class "container"}
     [:div {:class "container-nav"} (nav nav-options nil)]
     [:div {:class "container-social"} (social)]
     [:div {:body "container-body"} (html-fn req)]]))
    

(def home (html-200 (fn [req] (page-wrapper req home/handler))))

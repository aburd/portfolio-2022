(ns portfolio-2022.views.core
  (:require 
    [clojure.string :as s]
    [hiccup.core :refer [html]]))

(defn html-view-wrap [inner-html]
  (html [:html
         [:head
          [:meta {:name "viewport" :content "width: device-width, initial-scale: 1.0"}]]
         [:link {:rel "stylesheet" :href "/styles.css"}] 
         [:body inner-html]
         [:script {:src "/js/main.js"}]]))

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

(defn lang-form 
  "A form which will allow the user to change languages"
  [req]
  (let [locale (:locale req)
        langs [{:value :en :display "English"}
               {:value :ja :display "日本語"}]]
    [:form {:class "form-lang"}
     [:select
      (for [{value :value display :display} langs]
        [:option {:value value :selected (= value locale)} display])]]))

(defn social
  "Returns social icons"
  [req]
  [:div {:class "social-icons"}
   [:ul
    [:li 
     [:a {:href "https://www.github.com/aburd"}
      [:img {:src "/github.png"}]]]
    [:li 
     [:a {:href "mailto:aaron.burdick@protonmail.com"}
      [:img {:src "/email.png"}]]]
    [:li
      [:div {:class "container-themes"} (switch)]]
    [:li
      [:div {:class "container-lang"} (lang-form req)]]]])

(defn default-page-view 
  [html-fn req]
  (let [t (:t' req)
        nav-options [{:url (str "/" "home") :text (t :ui/nav/home)}
                     {:url (str "/" "works") :text (t :ui/nav/works)}
                     {:url (str "/" "terminal") :text (t :ui/nav/terminal)}
                     {:url (str "/" "contact") :text (t :ui/nav/contact)}]]
    (html-view-wrap [:div {:class "container"}
                     [:div {:class "container-nav"} (nav nav-options nil)]
                     [:div {:class "container-social"} (social req)]
                     [:div {:body "container-body"} (html-fn req)]])))

(ns portfolio-2022.views.core
  (:require 
    [clojure.string :as s]
    [hiccup.core :refer [html]]
    [portfolio-2022.util :as u]
    [portfolio-2022.views.components :as c]))

(defn html-view-wrap [inner-html]
  (html [:html
         [:head
          [:meta {:name "viewport" :content "width: device-width, initial-scale: 1.0"}]]
         [:link {:rel "stylesheet" :href "/styles.css"}] 
         [:body inner-html]
         [:script {:src "/js/main.js"}]]))

(defn term-view-wrap [inner-html]
  (html [:html
         [:head
          [:meta {:name "viewport" :content "width: device-width, initial-scale: 1.0"}]]
         [:link {:rel "stylesheet" :href "/styles.css"}] 
         [:link {:rel "stylesheet" :href "/xterm.css"}] 
         [:body inner-html]
         [:script {:src "/js/main.js"}]]))

(defn nav-option-active 
  "Check if the nav-options is active based on request data."
  [option req]
  (= (:url option) (-> req :reitit.core/match :path)))

(defn nav 
  "Returns a nav hiccup navbar with some list of options."
  [options]
  [:nav {:class "main-nav"}
   [:button {:class "start-btn"} "Start"]
   [:ul
    (for [option options]
      (let [class-name (if (:active option) "active" "")]
        [:li {:class class-name}
         (if (:active option)
           (s/capitalize (:text option))
           [:a {:href (:url option)} (s/capitalize (:text option))])]))]])

(defn locale-form 
  "A form which will allow the user to change languages."
  [req]
  (let [{locale :locale uri :uri query-string :query-string} req
        locales [{:value :en :display "English"}
                 {:value :ja :display "日本語"}]
        redirect-uri (cond-> uri
                       (some? query-string) (str "?" query-string))]
                       
    [:form {:class "form-locale" :action "locale" :method "post"}
     [:input {:name "redirect-uri" :type "hidden" :value redirect-uri}]
     [:select {:name :locale}
      (for [{value :value display :display} locales]
        [:option {:value value :selected (= value locale)} display])]]))

(defn social
  "Returns social icons."
  [req]
  [:div {:class "social-icons"}
   [:ul
    [:li 
     [:a {:href "https://www.github.com/aburd"}
      [:div {:class "icon github-icon"}]]]
    [:li 
     [:a {:href "mailto:aaron.burdick@protonmail.com"}
      [:div {:class "icon email-icon"}]]]
    [:li
      [:div {:class "container-themes"} (c/switch true "theme")]]
    [:li
      (locale-form req)]]])

(defn nav-options [req]
  (let [t (:t req)
        locale (:locale req)]
    (map 
      #(merge % {:active (nav-option-active % req)} {:text (t locale (str "ui/nav/" (:name %)))}) 
      u/menu-options)))

(defn default-layout [main-hiccup req & args]
  [:div {:class "container"}
   [:header 
    [:div {:class "container-nav"} (nav (nav-options req))]
    [:div {:class "container-social"} (social req)]]
   [:main (apply main-hiccup req args)]
   [:footer]])

(defn default-page-view 
  [req main-hiccup & args]
  (html-view-wrap (apply default-layout main-hiccup req args)))

(defn term-page-view 
  [req main-hiccup & args]
  (term-view-wrap (apply default-layout main-hiccup req args)))

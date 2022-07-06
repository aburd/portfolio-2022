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

(defn switch
  "Makes a toggle switch."
  [on switch-name]
  [:label {:class "switch"}
   [:input {:type "checkbox" :checked on :name switch-name}]
   [:span {:class "slider round"}]])

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
      [:div {:class "container-themes"} (switch true "theme")]]
    [:li
      (locale-form req)]]])

(defn default-page-view 
  [html-fn req]
  (let [t (:t req)
        locale (:locale req)
        nav-options (map 
                      #(merge % {:active (nav-option-active % req)}) 
                      [{:url (str "/") :text (t locale :ui/nav/home)}
                       {:url (str "/" "about") :text (t locale :ui/nav/about)}
                       {:url (str "/" "works") :text (t locale :ui/nav/works)}
                       {:url (str "/" "terminal") :text (t locale :ui/nav/terminal)}
                       {:url (str "/" "contact") :text (t locale :ui/nav/contact)}])]
        
    (html-view-wrap [:div {:class "container"}
                     [:header [:div {:class "container-nav"} (nav nav-options)]
                              [:div {:class "container-social"} (social req)]]
                     [:main (html-fn req)]
                     [:footer]])))

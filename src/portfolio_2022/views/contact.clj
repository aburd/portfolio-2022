(ns portfolio-2022.views.contact
  (:require 
    [portfolio-2022.util :refer [t]]))

(defn form-group [name el]
  [:dev {:class "form-group"}
   [:label {:for name} name]
   [:div {:class "form-element"} el]])

(defn show [req] 
  (let [{t :t locale :locale} req]
    [:div {:class "page-contact"} 
     [:h1 {:class "page-tag-line"} (t locale :contact-title)]
     [:form {:class "form-contact"}
      (form-group 
        (t locale :ui/name)
        [:input {:type "text" :name "name"}])
      (form-group 
        (t locale :ui/email)
        [:input {:type "text" :name "email"}])
      (form-group 
        (t locale :ui/comments)
        [:textarea {:name "comments"}])]]))
      
    


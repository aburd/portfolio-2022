(ns portfolio-2022.views.components
  (:require [portfolio-2022.util :refer [t]]
            [taoensso.tower.utils :refer [markdown]]))

(defn form-group [group-name el]
  [:dev {:class "form-group"}
   [:label {:for name} group-name]
   [:div {:class "form-element"} el]])

(defn switch
  "Makes a toggle switch."
  [on switch-name]
  [:label {:class "switch"}
   [:input {:type "checkbox" :checked on :name switch-name}]
   [:span {:class "slider round"}]])

(defn work-item [work & {:keys [locale]}]
  (let [{company-name :company-name
         date-start :date-start
         date-end :date-end
         employment-type :employment-type
         tags :tags
         description :description} work
        locale (or locale :en)]
    [:div {:class "work-item"}
     [:div {:class "company-name"} company-name]
     [:div {:class "work-period"}
       [:label (t locale :ui/work-period)]
       [:span {:class "date-start"} date-start] 
       " ~ "
       [:span {:class "date-end"} (if (nil? date-end) "Current" date-end)]] 
     [:div {:class "employment-type"} 
      [:label (t locale :ui/employment)]
      employment-type] 
     [:div {:class "description"} (markdown {:inline? false} description)]
     [:ul {:class "tags"}
      (for [tag tags]
        [:li {:class "tag"} tag])]])) 
    

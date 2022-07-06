(ns portfolio-2022.views.components)

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

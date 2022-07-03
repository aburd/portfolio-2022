(ns portfolio-2022.fe.core)

; theming code
(defn css-root []
  (.-documentElement js/document))

(defn light-theme []
  {"--main-bg-color" "#ddd"
   "--main-f-color" "#333"
   "--main-f-color-visited" "#3c005a"})

(defn dark-theme []
  {"--main-bg-color" "#333"
   "--main-f-color" "#ddd"
   "--main-f-color-visited" "#eabfff"})

(defn set-theme [theme]
  (let [root (css-root)]
    (doseq [[prop-name prop-val] theme]
      (.. root -style (setProperty prop-name prop-val)))))

(defn theme-switch []
  (.querySelector js/document ".container-themes .switch"))

(defn handle-theme-click [ev]
  (if (.. ev -target -checked)
    (set-theme (dark-theme))
    (set-theme (light-theme))))

; locale code
(defn locale-form []
  (.querySelector js/document "form.form-locale"))

(defn locale-select []
  (.querySelector js/document "form.form-locale select"))

(defn handle-locale-change [ev]
  (println ev)
  (.submit (locale-form)))

(defn bind-controls []
  (.addEventListener (theme-switch) "click" handle-theme-click) 
  (.addEventListener (locale-select) "change" handle-locale-change)) 

(defn init []
  (set-theme (light-theme))
  (bind-controls))

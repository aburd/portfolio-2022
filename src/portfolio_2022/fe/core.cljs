(ns portfolio-2022.fe.core)

; theming code
(defn css-root []
  (.-documentElement js/document))

(defn light-theme []
  {"--main-bg-color" "#ddd"
   "--main-border-color" "#555"
   "--primary-color" "#333"
   "--secondary-color" "#3c005a"
   "--github-icon-url" "url(\"/github_light.png\")"
   "--email-icon-url" "url(\"/email_light.png\")"})
   

(defn dark-theme []
  {"--main-bg-color" "#333"
   "--main-border-color" "#bbb"
   "--primary-color" "#ddd"
   "--secondary-color" "#eabfff"
   "--github-icon-url" "url(\"/github_dark.png\")"
   "--email-icon-url" "url(\"/email_dark.png\")"})

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

; Start button
(defn start-btn []
  (.querySelector js/document "nav .start-btn"))

(defn start-menu []
  (.querySelector js/document "nav.main-nav ul"))

(defn has-class [el class]
  (.. el -classList (contains class)))

(defn remove-class [el class]
  (.. el -classList (remove class)))

(defn add-class [el class]
  (.. el -classList (add class)))

(defn toggle-active [el]
  (if (has-class el "active")
    (remove-class el "active")
    (add-class el "active")))

(defn handle-start-click [ev]
  (toggle-active (start-menu)))

(defn bind-controls []
  (.addEventListener (start-btn) "click" handle-start-click)
  (.addEventListener (theme-switch) "click" handle-theme-click) 
  (.addEventListener (locale-select) "change" handle-locale-change)) 

(defn init []
  (set-theme (dark-theme))
  (bind-controls))

(ns portfolio-2022.fe.core
  (:require [portfolio-2022.fe.term :refer [mount-term]]))

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


(defn browser-theme-setting []
  (if (and (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)")))
    "dark"
    "light"))

(defn theme []
  (let [theme-from-storage (.. js/window -localStorage (getItem "theme"))]
    (if (some? theme-from-storage)
      theme-from-storage
      (browser-theme-setting))))

(defn store-theme [theme]
  (.. js/window -localStorage (setItem "theme" theme)))

(defn set-theme-css [theme]
  (let [root (css-root)]
    (doseq [[prop-name prop-val] theme]
      (.. root -style (setProperty prop-name prop-val)))))

(defn theme-switch []
  (.querySelector js/document ".container-themes .switch"))

(defn theme-input []
  (.querySelector js/document ".container-themes [name='theme']"))

(defn set-theme [theme]
  (condp = theme
    "dark" (do (set-theme-css (dark-theme))
               (store-theme "dark"))
    "light" (do (set-theme-css (light-theme))
               (store-theme "light"))))

(defn handle-theme-click [ev]
  (if (.. ev -target -checked)
    (set-theme "dark")
    (set-theme "light")))

; locale code
(defn locale-form []
  (.querySelector js/document "form.form-locale"))

(defn locale-select []
  (.querySelector js/document "form.form-locale select"))

(defn handle-locale-change [_ev]
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

(defn handle-start-click [_ev]
  (toggle-active (start-menu)))

(defn first-inactive-menu-item []
  (.querySelector js/document "nav.main-nav li:not(.active)"))

(defn handle-window-keydown [ev]
  (let [shift? (.-ctrlKey ev)
        pressed-key (.-key ev)]
    (when (and shift? (= pressed-key "k"))
      (.preventDefault ev)
      (toggle-active (start-menu))
      (.focus (first-inactive-menu-item)))))

(defn bind-controls []
  (.addEventListener (start-btn) "click" handle-start-click)
  (.addEventListener js/window "keydown" handle-window-keydown)
  (.addEventListener (theme-switch) "click" handle-theme-click) 
  (.addEventListener (locale-select) "change" handle-locale-change)) 

  
(defn bootstrap []
  (bind-controls)
  (when (= (theme) nil)
    (store-theme (theme))))

(defn init []
  (bootstrap)
  (set-theme (theme))
  (set! (.-checked (theme-input)) (= (theme) "dark"))
  (mount-term))

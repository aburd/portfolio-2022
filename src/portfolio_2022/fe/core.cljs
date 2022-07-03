(ns portfolio-2022.fe.core)

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
    

(defn bind-controls []
  (.addEventListener (theme-switch) "click" handle-theme-click)) 

(defn init []
  (set-theme (light-theme))
  (bind-controls))

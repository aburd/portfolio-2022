(ns portfolio-2022.fe.term
  (:require ["xterm" :as xt]
            ["xterm-addon-fit" :refer [FitAddon]]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [clojure.string :as s]
            [goog.string :as gstring]
            [goog.string.format]))

(declare commands)
(defonce terminal (atom nil))
(defonce terminal-fit-addon (atom (FitAddon.)))
(defonce buffer (atom ""))
(defonce history (atom {:cur-idx -1
                        :prev-cmds []}))

(def debug true)
(defn info [& args]
  (when debug
    (apply println args)))

(defn query-selector 
  "Allias for document.querySelector."
  [selector]
  (.querySelector js/document selector))

(def term-el (query-selector "#terminal"))
(def prompt-text "$ ")
(def term-options (clj->js {"cursorBlink" true
                            "theme" {"background" "#222"
                                     "cursor" "#eabfff"}}))

(def menu-options
  {"home" "/"
   "about" "/about"
   "works" "/works"})

(defn term-str [text]
  (s/replace text "\n" "\r\n"))

(defn fetch-page [url]
  (-> (.fetch js/window url)
      (.then #(.json %))))

(some #{:key :ke} [:ke])

(defn term-ls [& opts]
  (let [long? (some #{"-l"} opts)
        separator (if long? "\n" " ")
        files (->> menu-options
                   keys
                   (s/join separator))]
    (.write @terminal (str "\r\n" (term-str files))))) 

(defn term-open [file]
  (if-some [url (get menu-options file)]
    (do 
      (.write @terminal (str "\r\nOpening " file "..."))
      (set! (.. js/window -location -href) url))
    (.write @terminal (str "\r\n" "Can't open " file))))

(defn help-message [] 
  (let [msg (s/join 
              "\n"
              (map (fn [[cmd cmd-info]] (str "- " cmd ": " (:help cmd-info))) commands))]
    (gstring/format "\nThis terminal has the following commands:\n%s\n" msg)))

(defn term-help []
  (.write @terminal (term-str (help-message)))) 

(defn prompt []
  (.write @terminal (str "\r\n" prompt-text)))

(defn term-cat [file]
  (go
    (when-some [url (get menu-options file)]
      (let [term @terminal
            body (<p! (fetch-page (str "/api" url)))]
        (.write term (term-str (.stringify js/JSON body nil 2)))
        (prompt)))))

(def commands {"help" {:cmd term-help
                       :help "Displays this message."}
               "ls" {:cmd term-ls
                     :help "Displays 'files'.\n\t-l: Long format"}
               "cat" {:cmd term-cat
                      :help "Prints contents of 'file' to terminal"}
               "nav" {:cmd term-open
                      :help "Navigate to that file (closes terminal)."}})

(defn parse-cmd-text [text]
  (-> text
      (s/split #"\s")))

(defn handle-submit []
  (let [[cmd-s & args] (parse-cmd-text @buffer)]
    (if-some [cmd-info (get commands cmd-s)]
      (apply (:cmd cmd-info) args)
      (.write @terminal (str "\r\n" cmd-s ": command not found")))
    (reset! history {:cur-idx -1 :prev-cmds (vec (concat [@buffer] (:prev-cmds @history)))})
    (reset! buffer "")))

(vec (concat [] [:hey]))

(defn handle-enter []
  (handle-submit)
  (prompt)
  (reset! buffer ""))

(defn handle-backspace []
  (let [s @buffer]
    (reset! buffer (subs s 0 (- (count s) 1)))
    (.write @terminal "\b \b")))

(defn handle-ctrl-l []
  (.clear @terminal))

(defn clear-term-line []
  (.write @terminal (apply str (repeat (count @buffer) "\b \b"))))

(defn change-history [idx-fn]
  (let [{cur :cur-idx c :prev-cmds} @history]
    (when-some [text (get c (idx-fn cur))] 
      (clear-term-line)
      (.write @terminal text)
      (reset! buffer text)
      (reset! history (merge @history {:cur-idx (idx-fn cur)}))))
  (info @history))

(defn handle-up-key []
  (change-history inc))

(defn handle-down-key []
  (when (> (:cur-idx @history) 0)
    (change-history dec)))

(defn handle-key [e]
  (info e)
  (let [term @terminal
        c (.-key e)]
    (condp = c
      "\u007f" (handle-backspace)
      "\r" (handle-enter)
      "\u000c" (handle-ctrl-l)
      "\u001b[A" (handle-up-key)
      "\u001b[B" (handle-down-key)
      (do 
        (reset! buffer (str @buffer c))
        (.write term c)))))

(defn set-handlers []
  (let [term @terminal]
    (.onKey term handle-key)))

(defn mount-term []
  (when (some? term-el)
    (info "Terminal DOM element found. Mounting...")
    (reset! terminal (xt/Terminal. term-options))
    (.loadAddon @terminal @terminal-fit-addon)
    (.open @terminal term-el)
    (.fit @terminal-fit-addon)
    (term-help)
    (.focus @terminal)
    (prompt)
    (set-handlers)))
  

(ns portfolio-2022.fe.term
  (:require ["xterm" :as xt]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]]
            [clojure.string :as s]))

(defonce terminal (atom nil))
(defonce buffer (atom ""))
(defonce history (atom {:cur-idx 0
                        :prev-cmds []}))

(def debug false)
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

(def help-message "
This terminal has the following commands:
- help: Displays this message
- ls: Displays 'files'
- open: 'Open' a 'file'
- cat: Prints contents of 'file' to terminal
")

(def menu-options
  {"home" "/"
   "about" "/about"
   "works" "/works"})

(defn term-str [text]
  (s/replace text "\n" "\r\n"))

(defn fetch-page [url]
  (-> (.fetch js/window url)
      (.then #(.json %))))

(defn term-ls [dir]
  (let [term @terminal
        files (->> menu-options
                   keys
                   (s/join " "))]
    (.write term (str "\r\n" files)))) 

(defn term-help []
  (.write @terminal (term-str help-message))) 

(defn term-open [file]
  (if-some [url (get menu-options file)]
    (do 
      (.write @terminal (str "\r\nOpening " file "..."))
      (set! (.. js/window -location -href) url))
    (.write @terminal (str "\r\n" "Can't open " file))))

(defn prompt []
  (.write @terminal (str "\r\n" prompt-text)))

(defn term-cat [file]
  (go
    (when-some [url (get menu-options file)]
      (let [term @terminal
            body (<p! (fetch-page (str "/api" url)))]
        (.write term (str body))
        (prompt)))))

(def commands {"ls" term-ls
               "help" term-help
               "open" term-open
               "cat" term-cat})

(defn parse-cmd-text [text]
  (-> text
      (s/split #"\s")))

(defn handle-submit []
  (let [b @buffer
        term @terminal
        [cmd-s & args] (parse-cmd-text b)]
    (if-some [cmd (get commands cmd-s)]
      (apply cmd args)
      (.write term (str "\r\n" cmd-s ": command not found")))
    (reset! history {:cur-idx 0 :prev-cmds (vec (concat [@buffer] (:prev-cmds @history)))})
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

(defn handle-up-key []
  (let [{cur :cur-idx c :prev-cmds} @history]
    (when-some [text (get c cur)] 
      (clear-term-line)
      (.write @terminal text)
      (reset! buffer text)
      (reset! history (merge @history {:cur-idx (inc cur)})))))

(defn handle-down-key [])

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
    (.open @terminal term-el)
    (term-help)
    (.focus @terminal)
    (prompt)
    (set-handlers)))
  

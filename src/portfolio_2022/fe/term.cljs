(ns portfolio-2022.fe.term
  (:require ["xterm" :as xt]
            [clojure.string :as s]))

(defonce terminal (atom nil))
(defonce buffer (atom ""))

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
   "works" "/works"
   "terminal" "/terminal"})

(defn term-str [text]
  (s/replace text "\n" "\r\n"))

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

(defn term-cat [dir]
  (println "cat"))

(def commands {"ls" term-ls
               "help" term-help
               "open" term-open
               "cat" term-cat})

(defn parse-cmd-text [text]
  (-> text
      (s/split #"\s")))

(defn prompt []
  (.write @terminal (str "\r\n" prompt-text)))

(defn handle-submit []
  (let [b @buffer
        term @terminal
        [cmd-s & args] (parse-cmd-text b)]
    (if-some [cmd (get commands cmd-s)]
      (apply cmd args)
      (.write term (str "\r\n" cmd-s ": command not found")))
    (reset! buffer "")))

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

(defn handle-key [e]
  (.log js/console e)
  (let [term @terminal
        c (.-key e)]
    (condp = c
      "\u007f" (handle-backspace)
      "\r" (handle-enter)
      "\u000c" (handle-ctrl-l)
      (do 
        (reset! buffer (str @buffer c))
        (.write term c)))))

    

(defn set-handlers []
  (let [term @terminal]
    (.onKey term handle-key)))

(defn mount-term []
  (when (some? term-el)
    (println "Terminal DOM element found. Mounting...")
    (reset! terminal (xt/Terminal. term-options))
    (.open @terminal term-el)
    (term-help)
    (.focus @terminal)
    (prompt)
    (set-handlers)))
  

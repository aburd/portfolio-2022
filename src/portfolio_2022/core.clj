(ns portfolio-2022.core
  (:require [clojure.pprint :refer [pprint]] 
            [portfolio-2022.handlers.html :as html-handlers]
            [portfolio-2022.handlers.json :as json-handlers]
            [portfolio-2022.util :refer [tower-config]]
            [reitit.ring :refer [ring-handler router]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.refresh :refer [wrap-refresh]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]
            [taoensso.tower.ring :refer [wrap-tower]]) 
  (:gen-class))

(defonce server (atom nil))

(def routes
  (some-fn
    (ring-handler
      (router 
        [
         ["/" {:get {:handler html-handlers/home}}]
         ["/about" {:get {:handler html-handlers/about}}]
         ["/works" {:get {:handler html-handlers/work-history}}]
         ["/terminal" {:get {:handler html-handlers/terminal}}]
         ["/locale" {:post {:handler html-handlers/change-locale}}]
         ["/echo"
           {:get {:handler (fn [req] 
                             {:status 200
                              :headers {"Content-Type" "text/plain"}
                              :body (with-out-str (pprint req))})}}]
         ["/api" {}
          ["/" {:get {:handler json-handlers/home}}]
          ["/about" {:get {:handler json-handlers/about}}]
          ["/works" {:get {:handler json-handlers/work-history}}]]]))
    (constantly html-handlers/not-found)))

(defn wrap-cookie-settings [handler]
  (fn [req]
    (let [cookies (:cookies req)
          cookie-options (cond-> {}
                           (contains? cookies "locale") (merge {:locale (keyword (:value (get cookies "locale")))})
                           (contains? cookies :theme) (merge {:theme (:theme cookies)}))]
     (handler (merge req cookie-options)))))

(defn basic-app [handler]
  (-> handler
      wrap-cookie-settings
      (wrap-tower tower-config)
      (wrap-resource "public")
      wrap-cookies
      wrap-keyword-params
      wrap-params))

(defn reload-app [handler]
  (-> handler
      wrap-refresh
      wrap-reload))

(def app (-> routes
             basic-app))

(defn start-server 
  ([port dev-mode]
   (reset! server 
           (jetty/run-jetty (if (true? dev-mode) 
                              (reload-app #'app)
                              #'app) 
                          {:port port
                           :join? false}))) 
  ([port]
   (reset! server 
           (jetty/run-jetty #'app 
                          {:port port
                           :join? false})))) 

(defn stop-server []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))

(defn reset-server [port]
  (when-some [_s @server]
    (stop-server))
  (start-server port))

(def usage
  "Portfolio usage:
  -p, --port 'PORT': The port you want the server to run on. Defaults to 3001.")

(def flags {"-p" :port
            "--port" :port})

(defn flag-match [m pair]
  (let [[flag value] pair
        match (get flags flag)]
    (if (some? match)
      (assoc m match value)
      m)))

(defn args->map [args]
  (->> args
       (partition 2)
       (reduce flag-match {})))

(defn args->opts [args]
  (args->map args))

(defn check-args [args]
  (when (not= 0 (mod (count args) 2))
    (println usage)
    (System/exit 0)))

(defn -main
  "Run the portfolio 2022 web server."
  [& args]
  (check-args args)
  (let [opts (args->opts args)
        port (Integer/parseInt (or (:port opts) "3001"))]
    (println (format "Running server on port '%s'" port))
    (start-server port)))

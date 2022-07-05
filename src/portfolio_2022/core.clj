(ns portfolio-2022.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.refresh :refer [wrap-refresh]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [reitit.ring :refer [ring-handler router]]
            [taoensso.tower.ring :refer [wrap-tower]] 
            [clojure.pprint :refer [pprint]]
            [portfolio-2022.handlers.core :as handlers]
            [portfolio-2022.util :refer [tower-config]])
  (:gen-class))

(defonce server (atom nil))


(def routes
  (some-fn
    (ring-handler
      (router 
        [
         ["/" {:get {:handler handlers/home}}]
         ["/about" {:get {:handler handlers/about}}]
         ["/contact" {:get {:handler handlers/contact}}]
         ["/locale" {:post {:handler handlers/change-locale}}]
         ["/echo"
           {:get {:handler (fn [req] 
                             {:status 200
                              :headers {"Content-Type" "text/plain"}
                              :body (with-out-str (pprint req))})}}]]))
    (constantly handlers/not-found)))

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
  (when-some [s @server]
      (stop-server))
  (start-server port))

(defn -main
  "Run the portfolio 2022 web server"
  [& args]
  (start-server 3001))

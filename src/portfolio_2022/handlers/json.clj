(ns portfolio-2022.handlers.json
  (:require 
    [cheshire.core :refer [generate-string]]
    [portfolio-2022.util :as util]
    [portfolio-2022.views.pages :as pages]))

(defn json-200 
  "Returns a json ring response."
  [body]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (generate-string body)}) 

(defn home 
  [req]
  (json-200 
    (pages/home req))) 

(defn about
  [req] 
  (json-200 
    (pages/about req)))

(defn work-history
  [req] 
  (json-200 
    (pages/work-history req (reverse (util/work-experience (:locale req))))))


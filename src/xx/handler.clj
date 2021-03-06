(ns xx.handler
  (:use compojure.core
        xx.views)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (index))
  (GET "/tag/:t" [t] (tagged t))
  (GET "/about" [] (about))
  (GET "/markdown/:t" [t] (markdown t))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

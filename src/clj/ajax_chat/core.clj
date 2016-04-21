(ns ajax-chat.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resources]
            [ring.util.response :as response]
            [ring.middleware.params :as p]
            [compojure.core :as c]
            [compojure.route :as r])
  (:gen-class))

(def ^:const port 3000)

(defn open-in-browser! [address]
  (println "Location:" address)
  (when (java.awt.Desktop/isDesktopSupported)
    (.browse (java.awt.Desktop/getDesktop) (java.net.URI. address))))

(def messages (atom []))

(c/defroutes app
  (c/POST "/add-message" request
    (let [text (get (:params request) "text")]
      (swap! messages conj text))
    {:status 200})
  (r/resources "/")
  (c/GET "/" request
    (response/redirect "/index.html"))
  (c/GET "/messages" request
    (pr-str @messages)))

(defn -main [& args]
  (jetty/run-jetty (p/wrap-params app) {:port port :join? false})
  (open-in-browser! (str "http://localhost:" port)))

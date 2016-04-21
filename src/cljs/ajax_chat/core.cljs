(ns ajax-chat.core
  (:require [reagent.core :as r]
            [ajax.core :as a]))

(defn on-key-press [event]
  (when (= 13 (.-charCode event))
    (a/POST "/add-message" {:params {:text (.-value (.-target event))}
                            :format :raw})
    (set! (.-value (.-target event)) "")))

(defn content []
  [:div
   [:input {:type :text
            :id :message
            :placeholder "Enter message..."
            :on-key-press on-key-press}]
   [:div {:id :messages}]])

(defn init []
  (r/render-component [content] (.querySelector js/document "#content")))

(defn handler [response]
  (.log js/console response))

(defn get-messages []
  (a/GET "/messages" {:handler handler}))

; window.onload = init;
(set! (.-onload js/window) init)

(.setInterval js/window get-messages 1000)
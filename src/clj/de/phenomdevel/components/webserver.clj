(ns de.phenomdevel.components.webserver
  (:require
   [clojure.spec.alpha :as s]

   [org.httpkit.server :as server]
   [taoensso.timbre :as log]
   [com.stuartsierra.component :as c]))


;; =============================================================================
;; Component

(defrecord Webserver [port
                      handler

                      server-handle]

  c/Lifecycle
  (start [this]
    (log/info "[Webserver] Started webserver on port" port)
    (->> (select-keys this [:port :max-body])
         (server/run-server handler)
         (assoc this :server-handle)))

  (stop [this]
    (log/info "[Webserver] Stopped webserver.")
    (when server-handle
      (server-handle :timeout 100))))


;; =============================================================================
;; Specs

(s/def ::port int?)
(s/def ::max-body int?)
(s/def ::handler ifn?)
(s/def ::config
  (s/keys
   :req-un [::port ::handler]
   :opt-un [::max-body]))


;; =============================================================================
;; Public API

(defn new-webserver
  [config]
  {:pre [(s/valid? ::config config)]}
  (map->Webserver config))


;; =============================================================================
;; For the repl

(comment
  (do
    (require '[compojure.route :as cr])
    (require '[compojure.core :as cc])

    (cc/defroutes app
      (cc/GET "/" []
        (fn [request]
          {:status 200
           :body "Hello World"}))

      (cr/not-found "Page not found"))

    (def !server
      (atom
       (new-webserver {:port 1212 :handler app})))

    (swap! !server c/start)

    ;; (swap! !server c/stop)
    )
  )

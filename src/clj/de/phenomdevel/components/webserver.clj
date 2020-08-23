(ns de.phenomdevel.components.webserver
  (:require
   [clojure.spec.alpha :as s]

   [taoensso.timbre :as log]
   [org.httpkit.server :as server]
   [com.stuartsierra.component :as c]

   [de.phenomdevel.components.webserver.specs :as specs]))


;; =============================================================================
;; Component

(defrecord Webserver [port
                      handler-factory

                      server-handle]

  c/Lifecycle
  (start [this]
    (log/info "[Webserver] Started webserver on port" port)
    (let [handler-config
          (dissoc this :port :handler-factory :server-handle)]

      (->> (select-keys this [:port :max-body])
           (server/run-server (handler-factory handler-config))
           (assoc this :server-handle))))

  (stop [this]
    (log/info "[Webserver] Stopped webserver.")
    (when server-handle
      (server-handle :timeout 100))
    (assoc this :server-handle nil)))


;; =============================================================================
;; Public API

(defn new-webserver
  "Takes `config` and returns a new
   webserver component (com.stuartsierra/component).

  Config can contain
  - :port -> port the webserver should listen on (int)
  - :max-body -> maximum webserver body size (int)
  - :handler-factory -> factory fn which will produce a new handler
                        Gets a map of components as first argument which
                        contains all dependencies of `webserver` component."
  [config]
  {:pre [(s/valid? ::specs/config config)]}
  (map->Webserver config))

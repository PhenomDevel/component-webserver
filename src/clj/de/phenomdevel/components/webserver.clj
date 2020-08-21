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
      (server-handle :timeout 100))
    (assoc this :server-handle nil)))


;; =============================================================================
;; Public API

(defn new-webserver
  "Takes `config` and returns a new
   webserver component (com.stuartsierra/component).

  Config can contain
  - :port -> port the webserver should listen on (int)
  - :max-body -> maximum webserver body size (int)"
  [config]
  {:pre [(s/valid? ::specs/config config)]}
  (map->Webserver config))

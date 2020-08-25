(ns de.phenomdevel.components.webserver
  (:require
   [clojure.spec.alpha :as s]

   [medley.core :as m]
   [taoensso.timbre :as log]
   [org.httpkit.server :as server]
   [com.stuartsierra.component :as c]

   [de.phenomdevel.components.webserver.specs :as specs]))


;; =============================================================================
;; Private Helper

(defn- dependencies
  [component]
  (m/filter-vals record? component))


;; =============================================================================
;; Component

(defrecord Webserver [port

                      handler-factory
                      handler-config

                      server-handle]

  c/Lifecycle
  (start [this]
    (log/info "[Webserver] Started webserver on port" port)
    (let [handler-config'
          (merge handler-config
                 (dependencies this))]

      (->> (select-keys this [:port :max-body])
           (server/run-server (handler-factory handler-config'))
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

  Args:
  config
  - :port -> port the webserver should listen on (int)
  - :max-body -> maximum webserver body size (int)

  handler-factory
  Factory which will produce a new handler. Will be supplied with
  all dependencies of webserver assoced into `handler-config` on their respective keys.

  handler-config
  Contains any config for `handler-factory`."
  [config handler-factory handler-config]
  {:pre [(s/valid? ::specs/handler-factory handler-factory)
         (s/valid? ::specs/config config)]}
  (-> config
      (assoc :handler-factory handler-factory
             :handler-config handler-config)
      (map->Webserver)))

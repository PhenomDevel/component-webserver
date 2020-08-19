# Usage

```clj

(require '[de.phenomdevel.components.server :as server])
(require '[com.stuartsierra.component :as c])

(def ^:private !server
  (atom nil))

(defn- app-handler
  [request]
  {:status 200
   :body "Hello World"})

(def ^:private config
  {:port 1212
   :handler app-handler}

(reset! !server (server/new-server config))

(swap! !server c/start) ;; => This will start the server

;; If you want to stop the server again do the following command

(swap! !server c/stop)

```

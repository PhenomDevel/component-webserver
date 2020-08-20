# component-webserver
This library provides an easy to use [`com.stuartsierra/component`](https://github.com/stuartsierra/component) component to start up a [`http-kit`](https://github.com/http-kit/http-kit) webserver which
can be provided with a handler. The handler then will handle all incoming requests.

# Installation
NOTE: This will be the steps once the project is on clojars. It is not currently on there!

To install, add the following to your project `:dependencies`:
```
[de.phenomdevel/component-webserver "1.0.0"]
```

## Quick and dirty
```clj
(require '[de.phenomdevel.components.webserver :as webserver])
(require '[com.stuartsierra.component :as c])

(def ^:private !server
  (atom nil))

(defn- app-handler
  [request]
  {:status 200
   :body "Hello World"})

(def ^:private config
  {:port 1212
   :handler app-handler})

(reset! !server (webserver/new-webserver config))

(swap! !server c/start) ;; => This will start the server

;; If you want to stop the server again do the following command

(swap! !server c/stop)

```

## Use within com.stuartsierra.component system
This method is pretty much the same as above but the a whole component system which will
normally be used.
This is a really basic example just to show how it should be done.

```clj
(require '[de.phenomdevel.components.webserver :as webserver])
(require '[com.stuartsierra.component :as c])

;; Could also come from a file or something
(def ^:private config
  {:server
   {:port 1212}})

;; This might also be a compojure handler
(defn- app-handler
  [request]
  {:status 200
   :body "Hello World"})

(def !system
  (atom
   (c/system-map
     :server
     (webserver/new-webserver (merge (:server config)
                                     {:handler app-handler})))))

(swap! !system c/start)
;; This will start your system with the webserver

```

# License
Copyright © 2020 Kevin Kaiser

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

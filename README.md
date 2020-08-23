[![Clojars Project](https://img.shields.io/clojars/v/cljs-workers.svg)](https://clojars.org/component-webserver)
[![cljdoc badge](https://cljdoc.org/badge/cljs-workers/cljs-workers)](https://cljdoc.org/d/component-webserver/component-webserver/CURRENT)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/PhenomDevel/component-webserver/blob/master/LICENSE)

# component-webserver
This library provides an easy to use [`com.stuartsierra/component`](https://github.com/stuartsierra/component) component to start up a [`http-kit`](https://github.com/http-kit/http-kit) webserver which
can be provided with a handler. The handler then will handle all incoming requests.

# Installation
To install, add the following to your project `:dependencies`:
```
[de.phenomdevel/component-webserver "1.0.1"]
```

## Usage
The code below shows an lightweight example of how you could use this component within your
[`com.stuartsierra/component`](https://github.com/stuartsierra/component) system.
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

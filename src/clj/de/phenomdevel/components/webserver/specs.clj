(ns de.phenomdevel.components.webserver.specs
  (:require
   [clojure.spec.alpha :as s]))


;; =============================================================================
;; Specs

(s/def ::port int?)
(s/def ::max-body int?)
(s/def ::handler-factory ifn?)
(s/def ::config
  (s/keys
   :req-un [::port]
   :opt-un [::max-body]))

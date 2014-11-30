(ns leiningen.generate
  (:require [clojure.java.io :as io]))

(def ^:private project-template
  '(defproject us.forkloop.xx "0.1.0-SNAPSHOT"
     :description "A Clojure Markdown backed static server."
     :url "http://example.com/FIXME"
     :dependencies [[org.clojure/clojure "1.5.1"]
                    [compojure "1.1.6"]]
     :plugins [[lein-ring "0.8.8"]]
     :ring {:handler xx.handler/app :init xx.utils/init}
     :profiles
     {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                           [ring-mock "0.1.5"]
                           [markdown-clj "0.9.38"]
                           [hiccup "1.0.4"]]}})
  )

(defn- copy-dir "" [src dest]
  (io/copy (io/file src) (io/file dest)))

(defn generate
  "create a new website"
  [project & args]
  (if-let [dest (first args)]
    (if (.exists (io/as-file dest))
      (leiningen.core.main/warn (str dest " alreay exist!")))
    (leiningen.core.main/info "Please provide a name")))

(defproject us.forkloop.xx "0.1.0-SNAPSHOT"
  :description "A Clojure Markdown backed static server."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]]
  :plugins [[lein-ring "0.8.8"]
            [generate "0.1.0-SNAPSHOT"]]
  :ring {:handler xx.handler/app :init xx.utils/init}
  :main xx.cli
  :aot [xx.cli]
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [markdown-clj "0.9.38"]
                        [hiccup "1.0.4"]]}})

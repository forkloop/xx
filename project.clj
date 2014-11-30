(defproject us.forkloop.xx "0.1.0"
  :description "A Clojure Markdown backed static server."
  :url "http://xx.forkloop.us"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]]
  :plugins [[lein-ring "0.8.8"]
            [generate "0.1.0"]]
  :ring {:handler xx.handler/app :init xx.utils/init}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [markdown-clj "0.9.58"]
                        [hiccup "1.0.4"]]}})

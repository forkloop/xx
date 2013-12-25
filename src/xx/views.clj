(ns xx.views
  (:use [hiccup core page]
         markdown.core)
  (:require [clojure.java.io :as io]))

(defn anchor "create anchor" [f]
  (let [basename (clojure.string/replace f ".md" "")]
  [:a {:href basename} (clojure.string/replace basename #"^markdown/" "")]))

(defn list-files "list markdown files" []
  (filter #(.endsWith (.getName %) ".md") (file-seq (io/file "markdown"))))

(defn index "index page" []
  (html5
    [:head
     [:title "forkloop"]
     (include-css "/css/screen.css")]
    [:body
     [:div
      [:ul (for [f (list-files)] [:li (anchor f)])]]]))

(defn markdown "render markdown" [title]
    (let [content (slurp (clojure.string/join "" ["markdown/" title ".md"]))]
      (html5
        [:head
         [:title title]
         (include-css "/css/screen.css")]
        [:body
         (md-to-html-string content)])
  ;(md-to-html-string content)
  ))

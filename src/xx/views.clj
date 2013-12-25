(ns xx.views
   (:use [hiccup core page]
          markdown.core))

(defn index "index page" []
  (html5
    [:head
     [:title "forkloop"]
     (include-css "/css/screen.css")]
    [:body
     [:h1 "Hello"]]))

(defn markdown "render markdown" [title]
    (let [content (slurp (clojure.string/join "" ["markdown/" title ".md"]))]
      (html5
        [:head
         [:title title]
         (include-css "/css/screen.css")]
        [:body
         [:h1 (md-to-html-string content)]])
  ;(md-to-html-string content)
  ))

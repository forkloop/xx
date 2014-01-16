(ns xx.views
  (:use [hiccup core page]
         markdown.core)
  (:require [clojure.java.io :as io]))

(defn anchor "create anchor" [f]
  (let [basename (clojure.string/replace f ".md" "")]
  [:a {:href basename} (clojure.string/replace basename #"^markdown/" "")]))

; filter out files startsWith `_` as unpublished
; filter out files not endsWith `.md`
(defn list-files "list markdown files" []
  (filter (apply every-pred
                 [#(.endsWith (.getName %) ".md") #(not (.startsWith (.getName %) "_"))])
          (file-seq (io/file "markdown"))))

(defn index "index page" []
  (html5
    [:head
     [:title "forkloop"]
     (include-css "/css/screen.css")]
    [:body
     [:div
      [:ul (for [f (list-files)] [:li (anchor f)])]]]))

(defn stat "doc-string" [file]
  (.lastModified (io/file (clojure.string/join "" ["markdown/" file ".md"]))))

(defn markdown "render markdown" [title]
    (let [content (slurp (clojure.string/join "" ["markdown/" title ".md"]))
          timestamp (stat title)]
      (html5
        [:head
         [:title title]
         (include-css "/css/screen.css")]
        [:body
         [:a {:href "/"} "index"]
         [:p {:class "right"} timestamp]
         [:div
          (md-to-html-string content)]])
  ;(md-to-html-string content)
  ))

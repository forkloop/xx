(ns xx.views
  (:use [hiccup core page]
         markdown.core
        xx.utils)
  (:require [clojure.java.io :as io]))

(defn anchor "create anchor" [f]
  (let [basename (clojure.string/replace f ".md" "")]
  [:a {:href basename} (clojure.string/replace basename #"^markdown/" "")]))

; sort based on timestamp
; filter out files startsWith `_` as unpublished
; filter out files not endsWith `.md`
(defn list-files "list markdown files" []
  (filter (apply every-pred
                 [#(.endsWith (.getName %) ".md") #(not (.startsWith (.getName %) "_"))])
          (file-seq (io/file "markdown"))))

(defn stat "doc-string" [file]
  (.lastModified (io/file file)))

(defn files-metadata "list files and its metadata" []
  (map #(hash-map (.toString %) (.lastModified (io/file %))) (list-files)))

(defn index "index page" []
  (html5
    [:head
     [:title "forkloop"]
     (include-css "/css/screen.css")]
    [:body
     ;[:div
      ; better [:p (sort-by (comp val first) > (files-metadata))]
      ;[:p (sort-by #(val (first %)) > (files-metadata))]]
     [:div
      [:ul (for [f (sort-by (comp val first) > (files-metadata))] [:li (anchor (key (first f)))])]]
     [:footer
      [:a {:href "/about"} "About"]]]))

(defn- render-markdown "doc-string" [title, file]
  (let [content (slurp file)
        timestamp (stat file)]
    (html5
      [:head
       [:title title]
       (include-css "/css/screen.css")]
      [:body
       [:a {:href "/"} "index"]
       [:p {:class "right"} (format-date timestamp)]
       [:div
        (md-to-html-string content)]
       (if (not= title "about")
       [:footer
        [:a {:href "/about"} "About"]])])))

(defn about "doc-string" []
  (render-markdown "about" "resources/pages/about.md"))

(defn markdown "render markdown" [title]
  (render-markdown title (clojure.string/join "" ["markdown/" title ".md"])))

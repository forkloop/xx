(ns xx.views
  (:use [hiccup core page]
         markdown.core
        xx.utils)
  (:require [clojure.java.io :as io]))

;;;
;;; TODO tag
;;;
(defn anchor "create anchor" [f]
  (let [basename (clojure.string/replace (key f) ".md" "")]
  [:a {:href basename} (str (format-date (val f)) " -- " (clojure.string/replace basename #"^markdown/" ""))]))

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

(defn- footer "Generate the footer" [url]
  [:footer
   (if (not= url "about")
     [:a {:href "/about"} "About"])
   (if (:twitter settings)
     [:a {:class "pull-right" :href (str "https://twitter.com/" (:twitter settings))} (str "@"(:twitter settings))])])

(defn index "index page" []
  (html5
    [:head
     [:title "forkloop"]
     (include-css "/css/screen.css")]
    [:body
     (if (:name settings)
       [:h1 {:id "name"} (:name settings)])
     [:div {:id "wrapper"}
      [:ul
       (let [md (files-metadata)]
         (for [f (sort-by (comp val first) > md)] [:li (anchor (first f))]))]]
     (footer "index")]))

(defn- render-markdown "doc-string" [title, file]
  (let [content (slurp file)
        timestamp (stat file)]
    (html5
      [:head
       [:title title]
       (include-css "/css/screen.css")]
      [:body
       [:a {:href "/" :class "home"}
         [:i {:class "fa fa-home fa-4x"}]]
       [:div {:id "wrapper"}
        (if (not= title "about")
          [:p {:class "right"} [:i {:class "fa fa-pencil-square-o fa-lg"}](format-date timestamp)])
        [:div {:class "markdown"}
         (md-to-html-string content)]]
      (footer title)])))

(defn about "doc-string" []
  (render-markdown "about" "resources/pages/about.md"))

(defn markdown "render markdown" [title]
  (render-markdown title (clojure.string/join "" ["markdown/" title ".md"])))

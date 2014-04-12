(ns xx.views
  (:use [hiccup core page]
         markdown.core)
  (:require [clojure.java.io :as io]
            [xx.templates :as templates]
            [xx.utils :as utils]))


(defn anchor "create anchor" [f]
  (let [basename (clojure.string/replace (key f) ".md" "")]
  [:a {:href basename} (str (utils/format-date (val f)) " -- " (clojure.string/replace basename #"^markdown/" ""))]))

(defn stat "doc-string" [file]
  (.lastModified (io/file file)))

(defn files-metadata "list files and its metadata" []
  (map #(hash-map (.toString %) (.lastModified (io/file %))) (utils/list-files)))

(defn- footer "Generate the footer" [url]
  [:footer
   (if (not= url "about")
     [:a {:href "/about"} "About"])
   (if (:twitter utils/settings)
     [:a {:class "pull-right" :href (str "https://twitter.com/" (:twitter utils/settings))} (str "@"(:twitter utils/settings))])])

(defn index "index page" []
  (html5
    [:head
     [:title "forkloop"]
     (include-css "/css/screen.css")]
    [:body
     (if (:name utils/settings)
       [:h1 {:id "name"} (:name utils/settings)])
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
        (templates/tags ["NYC" "life"])
        (if (not= title "about")
          [:p {:class "right edit-date"} [:i {:class "fa fa-pencil-square-o fa-lg"}](utils/format-date timestamp)])
        [:div {:class "markdown"}
         (md-to-html-string content)]]
      (footer title)])))

(defn about "doc-string" []
  (render-markdown "about" "resources/pages/about.md"))

(defn markdown "render markdown" [title]
  (render-markdown title (clojure.string/join "" ["markdown/" title ".md"])))

(defn tagged "list markdown with tag t" [t]
  (templates/layout (str "tag - " t)
                    (html
                      [:a {:href "/" :class "home"}
                       [:i {:class "fa fa-home fa-4x"}]]
                      [:div#wrapper
                       [:h1 (str "TAG -- " t)]
                       (templates/ul nil (get @utils/*tag-list* t) #(-> [:a {:href (str "/markdown/" %1)} %1]))]
                      (footer "tag"))))

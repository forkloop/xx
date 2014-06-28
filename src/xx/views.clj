(ns xx.views
  (:use [hiccup core page]
         markdown.core)
  (:require [clojure.java.io :as io]
            [xx.templates :as templates]
            [xx.utils :as utils]))


(defn anchor "create anchor" [f]
  (let [basename (clojure.string/replace (key f) ".md" "")]
    [:a {:href (utils/add-hypen basename)} (str (utils/format-date (val f)) " -- " (clojure.string/replace basename #"^markdown/" ""))]))

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
     [:title (:name utils/settings)]
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
  (let [[tag content] (utils/scan-markdown file)
        timestamp (stat file)]
    (html5
      [:head
       [:title title]
       (include-css "/css/screen.css")]
      [:body
       [:a {:href "/" :class "home"}
         [:i {:class "fa fa-home fa-4x"}]]
       [:div {:id "wrapper"}
        (templates/tags (:tags tag))
        (if (not= title "about")
          [:p {:class "right edit-date"} [:i {:class "fa fa-pencil-square-o fa-lg"}](utils/format-date timestamp)])
        [:div {:class "markdown"}
         (md-to-html-string content)]]
      (footer title)])))

(defn about "doc-string" []
  (render-markdown "about" "resources/pages/about.md"))

(defn markdown "render markdown" [t]
  (let [title (utils/remove-hypen t)]
    (render-markdown title (clojure.string/join "" ["markdown/" title ".md"]))))

(defn tagged "list markdown with tag t" [t]
  (let [tag (utils/remove-hypen t)]
    (templates/layout (str "tag - " tag)
                      (html
                        [:a {:href "/" :class "home"}
                         [:i {:class "fa fa-home fa-4x"}]]
                        [:div#wrapper
                         [:h1
                          [:i {:class "fa fa-tag fa-2x"}]
                          [:span tag]]
                         (templates/ul nil (get @utils/*tag-list* tag) #(-> [:a {:href (str "/markdown/" (utils/add-hypen %1))} %1]))]
                        (footer "tag")))))

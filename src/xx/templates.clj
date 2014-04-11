(ns xx.templates
  (:use [hiccup core page]))

(defn layout "doc-string" [title body]
  (html5
    [:head
     [:title title]
     (include-css "/css/screen.css")]
    [:body body]))

(defn ul "" [css-class l]
   [:ul
    (if css-class
      (for [e l] [:li {:class css-class} e])
      (for [e l] [:li e]))])

(ns xx.templates
  (:use [hiccup core page]))

(defn layout "doc-string" [title body]
  (html5
    [:head
     [:title title]
     (include-css "/css/screen.css")]
    [:body body]))

(defn ul "" [css-class l f]
   [:ul
    (if css-class
      (for [e l] [:li {:class css-class}
                  (f e)])
      (for [e l] [:li
                  (f e)]))])

(ns xx.templates
  (:require [xx.utils :as utils])
  (:use [hiccup core page]))

(defn layout "doc-string" [title body]
  (html5
    [:head
     [:title title]
     (include-css "/css/screen.css")]
    [:body body]))

(defn tags "" [t]
  (if (> (count t) 0)
    [:p {:class "left tags"}
     [:i {:class "fa fa-tags"}]
     (for [e t]
       [:span
        [:a {:href (str "/tag/" (utils/add-hypen e))} e]])]))

(defn ul "" [css-class l f]
   [:ul
    (if css-class
      (for [e l] [:li {:class css-class}
                  (f e)])
      (for [e l] [:li
                  (f e)]))])

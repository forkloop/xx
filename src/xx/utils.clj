(ns xx.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import [java.io PushbackReader]
           java.util.Date
           java.text.SimpleDateFormat))

(def settings (with-open [in (io/reader "config.edn")]
                (read (PushbackReader. in))))

(def ^:dynamic *tag-list*
  (atom {}))

(def ^:private date-format "MMM dd, yyyy")

(defn format-date "format the timestamp into readable date" [timestamp]
  (.format (SimpleDateFormat. date-format) (Date. timestamp)))

;; by convention, the tag is a Clojure hash written at the beginning
;; of a markdown file, surrounded by --
(defn scan-tag "scan for the tag given a file name." [filename]
  (with-open [fd (clojure.java.io/reader filename)]
    (let [tag (atom "{}")
          state (atom :seek)
          lines (line-seq fd)]
      (loop [line (first lines)
             left (rest lines)]
        (if (and (not (empty? line)) (not= @state :done))
          (do
            (cond
              (and (= line "--") (= @state :seek)) (reset! state :read) ;; begin of tag
              (and (= line "--") (= @state :read)) (reset! state :done) ;; terminate
              (= @state :read) (reset! tag line))
            (recur (first left) (rest left)))))
      (load-string @tag))))

; filter out files startsWith `_` as unpublished
; filter out files not endsWith `.md`
(defn list-files "list markdown files" []
  (filter (apply every-pred
                 [#(.endsWith (.getName %) ".md") #(not (.startsWith (.getName %) "_"))])
          (file-seq (io/file "markdown"))))

(defn init "" []
  (do
    (doseq [file (list-files)]
      (if-let [tags (:tags (scan-tag file))]
        (let [filename (clojure.string/replace (.getName file) #"\.md$" "")]
          (doseq [t tags]
            (if (contains? @*tag-list* t)
              (swap! *tag-list* assoc t (conj (get @*tag-list* t) filename))
              (swap! *tag-list* assoc t [filename])))))
      (println (str @*tag-list*)))))

(defn scan-markdown "process markdown to extract tags and others." [md]
  (with-open [fd (clojure.java.io/reader md)]
    (let [tag (atom "{}")
          content (atom [])
          state (atom :seek)]
      (doseq [line (line-seq fd)]
        (cond
          (and (= line "--") (= @state :seek)) (reset! state :read)
          (and (= line "--") (= @state :read)) (reset! state :done)
          (= @state :read) (reset! tag line)
          (= @state :done) (swap! content conj line)))
      [(load-string @tag) (clojure.string/join "\n" @content)])))

(defn add-hypen "replace whitespace with hypen for url" [s]
  (string/replace s #"\s" "-"))

(defn remove-hypen "replace hypen with whitespace" [s]
  (string/replace s "-", " "))

(defmacro elapse "doc-string" [& body]
  `(let [start-time# (System/currentTimeMills)
         ~'get-elapsed-time (fn [] (- (System/currentTimeMills) start-time#))]
     ~@body))

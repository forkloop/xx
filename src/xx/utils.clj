(ns xx.utils
  (:require [clojure.java.io :as io])
  (:import [java.io PushbackReader]
           java.util.Date
           java.text.SimpleDateFormat))

(def settings (with-open [in (io/reader "config.edn")]
                (read (PushbackReader. in))))

(def ^:private date-format "MMM dd, yyyy")

(defn format-date "format the timestamp into readable date" [timestamp]
  (.format (SimpleDateFormat. date-format) (Date. timestamp)))

;; by convention, the tag is a Clojure hash written at the beginning
;; of a markdown file, surrounded by --
;; FIXME stop reading whole file
(defn scan-tag "scan for the tag given a file name." [filename]
  (with-open [fd (clojure.java.io/reader filename)]
    (let [tag (atom "")
          state (atom :seek)]
    (doseq [line (line-seq fd)]
      (cond
        (and (= line "--") (= @state :seek)) (reset! state :read) ;; begin of tag
        (and (= line "--") (= @state :read)) (reset! state :done) ;; terminate
        (= @state :read) (reset! tag line)
        ))
      (load-string @tag))))

(defmacro elapse "doc-string" [& body]
  `(let [start-time# (System/currentTimeMills)
         ~'get-elapsed-time (fn [] (- (System/currentTimeMills) start-time#))]
     ~@body))

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

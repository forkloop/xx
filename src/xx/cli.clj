(ns xx.cli
  (:gen-class))

(defn- exit "helper function to exit with a message" [message]
  (println message)
  (System/exit 0))

(defn -main "CLI" [& args]
  (loop [options args]
    (if-let [option (first options)]
      (cond (or (= option "--root") (= option "-r"))
            (do
              (println (first (rest options)))
              (recur (rest (rest options))))
            (or (= option "--name") (= option "-n"))
            (do
              (println (first (rest options)))
              (recur (rest (rest options))))
            (or (= option "--help") (= option "-h"))
            (exit "Usage:\n")
            :else (throw (str "Invalid options: " option))))))

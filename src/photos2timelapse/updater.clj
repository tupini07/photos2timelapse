(ns photos2timelapse.updater
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [io.aviso.ansi :as pt]))

(def local-tag "1.0.0")

(defn check-for-updates []
  (println "Checking for updates ..")
  (let [remote-tag (:tag_name (json/read-str
                               (:body (client/get "https://api.github.com/repos/tupini07/photos2timelapse/releases/latest"))
                               :key-fn keyword))]

    (if (not= local-tag remote-tag)

      (do
        (println (pt/green "A new version of this software is available! You can download it from")
                 (pt/yellow "https://github.com/tupini07/photos2timelapse/releases"))
        (print "Since there is an update, do you want to stop the program? [N/y] ")

        (flush)

        (let [option (read-line)]
          (if (= option "y")
            (do
              (println "ok, bye!")
              (. System exit 0)))))

      (println "No updates available"))))

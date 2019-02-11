(ns photos2timelapse.updater
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [io.aviso.ansi :as pt]))

(def local-tag "1.0.1")

(defn copy-uri-to-file [uri file]
  (with-open [in (clojure.java.io/input-stream uri)
              out (clojure.java.io/output-stream file)]
    (clojure.java.io/copy in out)))

(defn check-for-updates []
  (println "Checking for updates ..")
  (let [remote-tag (:tag_name (json/read-str
                               (:body (client/get "https://api.github.com/repos/tupini07/photos2timelapse/releases/latest"))
                               :key-fn keyword))]

    (if (not= local-tag remote-tag)

      (do
        (def new-version-url (str "https://github.com/tupini07/photos2timelapse/releases/tag/" remote-tag))
        (def new-filename (str "photos2timelapse-" remote-tag "-standalone.jar"))

        (println (pt/green "A new version of this software is available! It can be found at")
                 (pt/yellow new-version-url))

        (println "New version will be downloaded to the current folder, as" new-filename)

        (copy-uri-to-file (str new-version-url "/" new-filename) new-filename)

        (print "Since there is an update, do you want to stop the program? [N/y] ")

        (flush)

        (let [option (read-line)]
          (if (= option "y")
            (do
              (println "ok, bye!")
              (. System exit 0)))))

      (println "No updates available"))))

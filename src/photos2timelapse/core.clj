(ns photos2timelapse.core
  (:gen-class)
  (:require [photos2timelapse.updater :as updater]
            [clojure.string :as str]
            [io.aviso.ansi :as pt])
  (:use [clojure.java.shell :only [sh]]))


(defn asku [default-value query-sentence]

  "Function that simply asks a user to input information, 
  it also accepts a default which can optionally be returned
  in the case the user inputs an empty string (so, no input)"

  (print query-sentence)
  (flush)
  (let [u-input (read-line)]

    (if (nil? u-input) ; when user presses Ctrl+c when requested input
      (. System exit 0))

    (if (not= u-input "")
      u-input
      default-value)))


(defn -main
  [& args]

  (println (pt/cyan "Starting execution, press") (pt/yellow "Ctrl+c") (pt/cyan "at any time to stop."))

  (updater/check-for-updates)

  (println (pt/red "Working on folder") (.getAbsolutePath (clojure.java.io/file ".")))


  (let [frame-rate (asku "19" "- Which frame rate do you want to use? [19] ")
        frame-size (asku "hd1080" "- Which image size do you want to use (resolution)? [hd1080] ")
        output-name (asku "timelapse.mp4" "- Which name do you want the output video to have? [timelapse.mp4] ")]


    ; Transform file names to properly numbered stuff
    (def files (->> (clojure.java.io/file ".")
                    (file-seq)
                    (filter #(let [x (.getName %1)
                                   f-format (last (str/split x #"\."))
                                   f-format (if (nil? f-format) "" f-format)
                                   f-format (str/lower-case f-format)]

                               (and (not= x ".")
                                    (not= x "..") 1
                                    (not (.isDirectory %1))
                                    (not= f-format "jar")
                                    (or
                                     (= f-format "jpg")
                                     (= f-format "png")
                                     (= f-format "jpeg")
                                     (= f-format "gif")))))

                    (sort #(compare (.getName %1) (.getName %2)))))


    (def max-name-length (apply max (map #(count (.getName %1)) files)))

    (loop [i 0
           fls files]

      (when (not (empty? fls))

        (def file (first fls))
        (def f-format (str/lower-case (last (str/split (.getName file) #"\."))))

        (.renameTo file (clojure.java.io/file (str "./" (format (str "%0" max-name-length "d") i) "." f-format)))

        (recur (inc i) (rest fls))))


    ; ffmpeg -r 19 -i %03d.jpg -s hd1080 -vcodec libx264 -crf 18 -preset veryslow timelapse_yt.mp4
    (def command (str "ffmpeg -r " frame-rate " -i %0" max-name-length "d.jpg -s " frame-size " -vcodec libx264 -crf 18 -preset veryslow " output-name))
    (println "Running command: " command)

    (def command-result (apply sh (str/split command #" ")))

    (println (:out command-result))
    (println (:err command-result))))


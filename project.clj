(defproject photos2timelapse "1.0.5"
  :description "Small wrapper for FFMPEG which builds a comand to transform many images into a video"
  :url "https://github.com/tupini07/photos2timelapse"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[io.aviso/pretty "0.1.37"]]
  :middleware [io.aviso.lein-pretty/inject]

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clj-http "3.9.1"]
                 [org.clojure/data.json "0.2.6"]
                 [io.aviso/pretty "0.1.37"]]

  :main ^:skip-aot photos2timelapse.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

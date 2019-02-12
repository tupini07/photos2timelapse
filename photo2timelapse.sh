mkdir -p ~/.photo2timelapse/
if [ $(ls ~/.photo2timelapse | grep .jar | wc -w) == 0 ]; then
    echo "Application .jar file not found, downloading.. "
    wget -O ~/.photo2timelapse/photos2timelapse-1.0.2-standalone.jar https://github.com/tupini07/photos2timelapse/releases/download/1.0.2/photos2timelapse-1.0.2-standalone.jar 
    chmod +x ~/.photo2timelapse/photos2timelapse-1.0.2-standalone.jar

    echo "Downloaded !"
fi

jars=($(ls ~/.photo2timelapse | grep .jar))
latest=${jars[-1]}

java -jar ~/.photo2timelapse/$latest

jar_in_current_folder=$(ls | grep photos2timelapse | grep .jar)
if [ $(echo $jar_in_current_folder | wc -w) > 0 ]; then
    mv $jar_in_current_folder ~/.photo2timelapse/
fi


cd ~/.photo2timelapse
if [ $(echo ${jars[@]} | wc -w) > 1 ]; then

    for f in ${jars[@]}; do
    if [ $f != $latest ]; then
        rm -rf $f
    else
        chmod +x $f
    fi
    done
    
fi


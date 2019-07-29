# check if application is "installed", if it is not then "install it"
mkdir -p ~/.photo2timelapse/
if [ $(ls ~/.photo2timelapse | grep .jar | wc -w) == 0 ]; then
    echo "Application .jar file not found, downloading.. "
    wget -O ~/.photo2timelapse/photos2timelapse-1.0.4-standalone.jar https://github.com/tupini07/photos2timelapse/releases/download/1.0.4/photos2timelapse-1.0.4-standalone.jar 
    chmod +x ~/.photo2timelapse/photos2timelapse-1.0.4-standalone.jar

    echo "Downloaded !"
fi

# find application executable
jars=($(ls ~/.photo2timelapse | grep .jar))

# if there are many then always pick the latest one
latest=${jars[-1]}

# launch application
java -jar ~/.photo2timelapse/$latest

# if there was an update, the application will have downloaded it to the current
# folder. In that case just pick this new .jar and move it to the install
# folder ~/.photo2timelapse
jar_in_current_folder=$(ls | grep photos2timelapse | grep .jar)
if [ $(echo $jar_in_current_folder | wc -w) > 0 ]; then
    mv $jar_in_current_folder ~/.photo2timelapse/
fi

# Finally, remove older executables and make sure new executable is actually
# marked as such
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


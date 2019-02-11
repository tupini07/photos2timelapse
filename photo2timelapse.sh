jars=($(ls ~/.photo2timelapse | grep .jar))
latest=${jars[-1]}

java -jar ~/.photo2timelapse/$latest

jar_in_current_folder=$(ls | grep photos2timelapse | grep .jar)
if [ $(echo $jar_in_current_folder | wc -w) > 0 ]; then
    mkdir -p ~/.photo2timelapse/
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


#!/bin/sh
UNITYLIBS="/Applications/Unity/Unity.app/Contents/PlaybackEngines/AndroidPlayer/bin/classes.jar"
ADGLIBS="adgeneration/ADG_AndroidSDK/ADG-1.4.0.jar"
export ANT_OPTS=-Dfile.encoding=UTF8
android update project -t 1 -p .
mkdir -p libs build
cp $UNITYLIBS libs
cp $ADGLIBS libs
ant release
cp -a bin/classes.jar build/CgateADGNativeManager.jar
#ant clean
rm -rf libs proguard-project.txt

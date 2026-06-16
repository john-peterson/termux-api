#!/bin/env bash
# set -x
set -e

# tools_version=26.0.1
BUILD_TOOLS_VERSION=current
BUILD_TOOLS=$ANDROID_SDK/build-tools/$BUILD_TOOLS_VERSION
ANDROID_JAR=$ANDROID_SDK/platforms/android-33/android.jar
# androidx=~/.gradle/caches/transforms-3/27d576167a170fa332f195a5e220334e/transformed/appcompat-1.6.1-api.jar
jar=obj:$ANDROID_JAR:$androidx
res=app/src/main/res
src=app/src/main/java
# manifest=app/src/main/AndroidManifest.xml
manifest=AndroidManifest.xml
# manifest=app/src/main/manifest.xml
jdk=8
# build=aapt

# if test $# -gt 0; then
if test "$build" = aapt; then
	# $BUILD_TOOLS/aapt package -f -m -S $res -J $src -M $manifest -I $ANDROID_JAR
	$BUILD_TOOLS/aapt package -f -m -J $src -M $manifest -I $ANDROID_JAR
	rm -rf obj/*
	# javac -Xmaxerrs 1 -d ./obj -source $jdk -target $jdk -classpath $jar  -sourcepath $src $src/open/app/*.java
	find $src/com/termux -name "*.java" | xargs javac -Xmaxerrs 1 -d obj -source 1.7 -target 1.7 -bootclasspath $JAVA_HOME/jre/lib/rt.jar -classpath $jar -sourcepath
	$BUILD_TOOLS/dx --dex --output=out/classes.dex ./obj
	apk=unaligned.apk
	chmod 644 $apk
	# $BUILD_TOOLS/aapt package -f -M $manifest -S $res -I $ANDROID_JAR -F $apk out
	$BUILD_TOOLS/aapt package -f -M $manifest -I $ANDROID_JAR -F $apk out
	# rm app/src/main/java/open/app/R.java

else
apk=(app/build/outputs/apk/debug/*.apk)
# apk=app/build/outputs/apk/debug/termux-api-app_v0.53.0+debug.apk
chmod 644 $apk 
gradle assembleDebug |& ack -i error -A3 -m1 && false
fi

set +e
chmod 400 $apk 
CLASSPATH=$apk app_process -Xnoimage-dex2oat / com.termux.api.cmd $@
# CLASSPATH=$apk app_process -Xnoimage-dex2oat / com.termux.api.main
# chmod 644 $apk 
exit

$JAVA_HOME/bin/jarsigner -verbose -keystore ~/.android/debug.keystore -storepass android -keypass android  unaligned.apk androiddebugkey
$BUILD_TOOLS/zipalign -f 4 unaligned.apk aligned.apk
termux-share -a view aligned.apk

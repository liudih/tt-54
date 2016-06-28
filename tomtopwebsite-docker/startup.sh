#!/bin/sh

script_dir=`dirname $0`
export script_dir

lib_dir=`(cd $script_dir; cd ..; pwd -P)`/lib
export lib_dir

__APP_CLASSPATH__
export app_classpath

__APP_MAINCLASS__
export app_mainclass

java -classpath "$app_classpath" "$app_mainclass"

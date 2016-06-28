#!/bin/sh

SERVER=$1

if [ "$1" = "" ]
then
	echo "$0 <server>"
	echo ""
	echo "e.g. $0 http://www.tomtop.com"
	exit 1
fi

java -jar sbt-launch.jar -Dsbt.log.noformat=true \
	-Dsbt.override.build.repos=true \
	-Dsbt.repository.config=sbt/repositories \
	-Dserver=$SERVER \
	test

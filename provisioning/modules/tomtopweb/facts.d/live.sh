#!/bin/sh

if [ -f /usr/share/tomtopweb/LIVE ]
then
	echo "tomtopweb_live=`cat /usr/share/tomtopweb/LIVE`"
else
	echo "tomtopweb_live=9000"
fi

#!/bin/bash

[ -d /deploy-9000 ] || mkdir /deploy-9000
[ -d /deploy-9001 ] || mkdir /deploy-9001

if [ -f /usr/share/tomtopweb/LIVE ]
then
  STAGING=`if [ "$(cat /usr/share/tomtopweb/LIVE)" = "9000" ]; then echo 9001; else echo 9000; fi`
else
  STAGING=9000
fi

echo "Pulling Build $1 to /deploy-$STAGING..."
wget -q -O /deploy-$STAGING/tomtopweb-1.0-SNAPSHOT.tgz http://192.168.7.13:82/$1/tomtopweb-1.0-SNAPSHOT.tgz
touch /deploy-$STAGING/REINSTALL


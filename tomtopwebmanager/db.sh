#!/bin/sh

# ---- Check docker is running -------
if [ `boot2docker status` != "running" ]
then
        boot2docker start
fi
$(boot2docker shellinit)

SERVER_IP=`boot2docker ip 2> /dev/null`

docker run --name postgres_cli -it --rm 192.168.7.15:5000/postgres psql -h $SERVER_IP -U tomtop

#!/bin/sh

PART1=`docker images -f dangling=true | cut -c 40-60 | grep -v "IMAGE ID"`
PART2=`docker images | grep tomtop/website | tail -n +20 | cut -c 60-80`

for i in $PART1 $PART2
do
  docker rmi $i
done

HOW TO BUILD
============

    docker build --tag 192.168.7.15:5000/tomtop/centos-ssh-java .

HOW TO RUN
==========

    docker run --privileged -it --rm -p 1022:22 \
    	-v /sys/fs/cgroup:/sys/fs/cgroup:ro \
    	-v /data/uat/.ssh:/root/.ssh \
    	192.168.7.15:5000/tomtop/centos-ssh-java


IMAGE WITH MAVEN INSTALLED
==========================

    docker build --tag 192.168.7.15:5000/tomtop/centos-ssh-java-maven maven



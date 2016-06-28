#!/bin/sh

if [ -d provisioning ]
then
  ( cd provisioning; git pull )
else
  git clone http://192.168.7.15:10080/tomtopwebsite/provisioning.git
fi

if [ ! -L modules ]
then
  ln -sf provisioning/modules modules
fi

git pull
puppet apply --modulepath=modules manifests/default.pp 

# XXX should check whether the website is started before returning
sleep 5

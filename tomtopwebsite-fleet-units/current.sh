#!/bin/sh

etcdctl ls --recursive /services/tomtopwebsite-active | cut -f 4 -d '/' | cut -f 2 -d ':'


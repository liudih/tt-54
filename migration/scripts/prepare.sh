#!/bin/bash

. env.sh

if [ ! -f $JARFILE ]
then
	if [ "$USE_CURL" = "true" ]
	then
		curl -o $JARFILE $DISTURL
	else
		wget -O $JARFILE $DISTURL
	fi
fi

[ -d data/categories ] || mkdir -p data/categories
[ -d data/products ] || mkdir -p data/products
[ -d data/product_trans ] || mkdir -p data/product_trans
[ -d data/relations ] || mkdir -p data/relations
[ -d data/members ] || mkdir -p data/members
[ -d data/orders ] || mkdir -p data/orders
[ -d data/comments ] || mkdir -p data/comments
[ -d data/keywords ] || mkdir -p data/keywords
[ -d data/groupprices ] || mkdir -p data/groupprices

[ -d source ] || mkdir -p source

# put selling points to source
if [ ! -d source/sellpoint ]
then
	get_remote $SELLPOINT_URL /tmp/sellpoint.tgz
	tar zxfv /tmp/sellpoint.tgz -C source
fi

# put category excel to source


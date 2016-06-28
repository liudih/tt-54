#!/bin/bash

if [ "$0" = "./uat.sh" ]
then
SERVER_IP=192.168.7.13
SEPARATE_DB=1
DB_USER=tomtopwebsite
DB_PASS=tomtopwebsite
else
  if [ "$1" != "" ]
  then
    SERVER_IP=$1
    shift
  else
    # ---- Check docker is running -------
    if [ `boot2docker status` != "running" ]
    then
	boot2docker start
    fi
    $(boot2docker shellinit)
    SERVER_IP=`boot2docker ip 2> /dev/null`
  fi
  SEPARATE_DB=0
  DB_USER=tomtop
  DB_PASS=tomtop
fi


echo "------ Server IP: $SERVER_IP"


# ---- Check container is running ------
is_running() {
	name="$1"
	((docker inspect "$name" 2> /dev/null | grep -v '\[\]') || echo '"Running": nonexistent') | grep "Running" | sed 's/ *"Running": \([^,]*\).*/\1/g'
}

case "`is_running postgres`" in
	"false")
		docker start postgres;;
	"nonexistent")
		docker run --name postgres -d \
			-p 5432:5432 \
		        -e 'POSTGRES_USER=tomtop' \
		        -e 'POSTGRES_PASSWORD=tomtop' \
			192.168.7.15:5000/postgres
		;;
esac
case "`is_running redis`" in
	"false")
		docker start redis;;
	"nonexistent")
		docker run --name redis -d \
			-p 6379:6379 \
			192.168.7.15:5000/redis
		;;
esac
case "`is_running phppgadmin`" in
	"false")
		docker start phppgadmin;;
	"nonexistent")
		docker run --name phppgadmin -d \
			-p 8000:80 \
			--link postgres:postgresql \
			192.168.7.15:5000/maxexcloo/phppgadmin
		;;
esac
case "`is_running elasticsearch`" in
	"false")
		docker start elasticsearch;;
	"nonexistent")
		docker run --name elasticsearch -d \
			-p 9200:9200 -p 9300:9300 \
			192.168.7.15:5000/dockerfile/elasticsearch
		;;
esac


if [ "$SEPARATE_DB" = "" -o "$SEPARATE_DB" = "0" ]
then
DBDRIVER_BASE=org.postgresql.Driver \
DBURL_BASE=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_BASE=${DB_USER} \
DBPASS_BASE=${DB_PASS} \
DBDRIVER_PRODUCT=org.postgresql.Driver \
DBURL_PRODUCT=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_PRODUCT=${DB_USER} \
DBPASS_PRODUCT=${DB_PASS} \
DBDRIVER_IMAGE=org.postgresql.Driver \
DBURL_IMAGE=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_IMAGE=${DB_USER} \
DBPASS_IMAGE=${DB_PASS} \
DBDRIVER_MEMBER=org.postgresql.Driver \
DBURL_MEMBER=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_MEMBER=${DB_USER} \
DBPASS_MEMBER=${DB_PASS} \
DBDRIVER_INTERACTION=org.postgresql.Driver \
DBURL_INTERACTION=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_INTERACTION=${DB_USER} \
DBPASS_INTERACTION=${DB_PASS} \
DBDRIVER_ADVERTISING=org.postgresql.Driver \
DBURL_ADVERTISING=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_ADVERTISING=${DB_USER} \
DBPASS_ADVERTISING=${DB_PASS} \
DBDRIVER_ORDER=org.postgresql.Driver \
DBURL_ORDER=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_ORDER=${DB_USER} \
DBPASS_ORDER=${DB_PASS} \
DBDRIVER_LOYALTY=org.postgresql.Driver \
DBURL_LOYALTY=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_LOYALTY=${DB_USER} \
DBPASS_LOYALTY=${DB_PASS} \
DBDRIVER_CART=org.postgresql.Driver \
DBURL_CART=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_CART=${DB_USER} \
DBPASS_CART=${DB_PASS} \
DBDRIVER_MANAGER=org.postgresql.Driver \
DBURL_MANAGER=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_MANAGER=${DB_USER} \
DBPASS_MANAGER=${DB_PASS} \
DBDRIVER_PAYPAL=org.postgresql.Driver \
DBURL_PAYPAL=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_PAYPAL=${DB_USER} \
DBPASS_PAYPAL=${DB_PASS} \
DBDRIVER_TRACKING=org.postgresql.Driver \
DBURL_TRACKING=jdbc:postgresql://${SERVER_IP}:5432/tomtop \
DBUSER_TRACKING=${DB_USER} \
DBPASS_TRACKING=${DB_PASS} \
REDIS_HOST="${SERVER_IP}:6379" \
ELASTICSEARCH_HOST=${SERVER_IP}:9300 \
./activator $*
else
DBDRIVER_BASE=org.postgresql.Driver \
DBURL_BASE=jdbc:postgresql://${SERVER_IP}:5432/base \
DBUSER_BASE=${DB_USER} \
DBPASS_BASE=${DB_PASS} \
DBDRIVER_PRODUCT=org.postgresql.Driver \
DBURL_PRODUCT=jdbc:postgresql://${SERVER_IP}:5432/product \
DBUSER_PRODUCT=${DB_USER} \
DBPASS_PRODUCT=${DB_PASS} \
DBDRIVER_IMAGE=org.postgresql.Driver \
DBURL_IMAGE=jdbc:postgresql://${SERVER_IP}:5432/image \
DBUSER_IMAGE=${DB_USER} \
DBPASS_IMAGE=${DB_PASS} \
DBDRIVER_MEMBER=org.postgresql.Driver \
DBURL_MEMBER=jdbc:postgresql://${SERVER_IP}:5432/member \
DBUSER_MEMBER=${DB_USER} \
DBPASS_MEMBER=${DB_PASS} \
DBDRIVER_INTERACTION=org.postgresql.Driver \
DBURL_INTERACTION=jdbc:postgresql://${SERVER_IP}:5432/interaction \
DBUSER_INTERACTION=${DB_USER} \
DBPASS_INTERACTION=${DB_PASS} \
DBDRIVER_ADVERTISING=org.postgresql.Driver \
DBURL_ADVERTISING=jdbc:postgresql://${SERVER_IP}:5432/advertising \
DBUSER_ADVERTISING=${DB_USER} \
DBPASS_ADVERTISING=${DB_PASS} \
DBDRIVER_ORDER=org.postgresql.Driver \
DBURL_ORDER=jdbc:postgresql://${SERVER_IP}:5432/order \
DBUSER_ORDER=${DB_USER} \
DBPASS_ORDER=${DB_PASS} \
DBDRIVER_LOYALTY=org.postgresql.Driver \
DBURL_LOYALTY=jdbc:postgresql://${SERVER_IP}:5432/loyalty \
DBUSER_LOYALTY=${DB_USER} \
DBPASS_LOYALTY=${DB_PASS} \
DBDRIVER_CART=org.postgresql.Driver \
DBURL_CART=jdbc:postgresql://${SERVER_IP}:5432/cart \
DBUSER_CART=${DB_USER} \
DBPASS_CART=${DB_PASS} \
DBDRIVER_MANAGER=org.postgresql.Driver \
DBURL_MANAGER=jdbc:postgresql://${SERVER_IP}:5432/manager \
DBUSER_MANAGER=${DB_USER} \
DBPASS_MANAGER=${DB_PASS} \
DBDRIVER_PAYPAL=org.postgresql.Driver \
DBURL_PAYPAL=jdbc:postgresql://${SERVER_IP}:5432/paypal \
DBUSER_PAYPAL=${DB_USER} \
DBPASS_PAYPAL=${DB_PASS} \
DBDRIVER_TRACKING=org.postgresql.Driver \
DBURL_TRACKING=jdbc:postgresql://${SERVER_IP}:5432/tracking \
DBUSER_TRACKING=${DB_USER} \
DBPASS_TRACKING=${DB_PASS} \
REDIS_HOST="${SERVER_IP}:6379" \
ELASTICSEARCH_HOST=${SERVER_IP}:9300 \
./activator $*
fi


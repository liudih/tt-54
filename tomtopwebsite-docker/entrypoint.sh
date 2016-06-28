#!/bin/ash

#
# give BASE/PRODUCT/IMAGE/ORDER/MEMBER to process
# the host linkage to 4 individual database environment
#
# assume docker link exists, with environments set:
#
#  PostgreSQL Type
#  -------------------------
#  xDB_NAME
#  xDB_PORT_5432_TCP_ADDR
#  xDB_PORT_5432_TCP_PORT
#  xDB_ENV_POSTGRES_USER (optionally set by linked container, if none user input required)
#  xDB_ENV_POSTGRES_PASSWORD (optionally set by linked container, if none user input required)
#
#  Redis Server
#  -------------------------
#  REDIS_PORT_6379_TCP_ADDR=172.17.0.64
#  REDIS_PORT_6379_TCP_PORT=6379
#
#  ElasitcSearch Server
#  -------------------------
#  ELASTICSEARCH_PORT_9300_TCP_PORT=9300
#  ELASTICSEARCH_PORT_9300_TCP_ADDR=172.17.0.73
#
process_base () {

LOWERNAME=`echo $1 | tr '[:upper:]' '[:lower:]'`

# ( echo pstgres | grep -i postgres > /dev/null ) || echo "not exists"
if [ "`printenv ${1}DB_NAME`" = "" ]
then
	echo WARNING: ${1}DB_NAME not defined, please use --link yourdbcontainer:${LOWERNAME}db
fi

TMPUSER=$3
TMPPASS=$4
if [ "$TMPUSER" = "" ]
then
	TMPUSER=`printenv ${1}DB_ENV_POSTGRES_USER`
fi
if [ "$TMPPASS" = "" ]
then
	TMPPASS=`printenv ${1}DB_ENV_POSTGRES_PASSWORD`
fi

eval "DBDRIVER_$1=org.postgresql.Driver"
if [ "`printenv ${1}DB_PORT_5432_TCP_ADDR`" != "" ]
then
eval "DBURL_$1=jdbc:postgresql://`printenv ${1}DB_PORT_5432_TCP_ADDR`:`printenv ${1}DB_PORT_5432_TCP_PORT`/$2"
fi
eval "DBUSER_$1=$TMPUSER"
eval "DBPASS_$1=$TMPPASS"

export DBDRIVER_$1
export DBURL_$1
export DBUSER_$1
export DBPASS_$1

if [ "`printenv DBDRIVER_$1`" = "" \
     -o "`printenv DBURL_$1`" = "" \
     -o "`printenv DBUSER_$1`" = "" ]
then
	echo "Not all parameters set for db: $LOWERNAME"
	echo "DBDRIVER_$1 = `printenv DBDRIVER_$1`"
	echo "DBURL_$1 = `printenv DBURL_$1`"
	echo "DBUSER_$1 = `printenv DBUSER_$1`"
	exit 1
fi

}


# --- Database Processing ---
for DB in BASE MEMBER PRODUCT IMAGE SEARCH CART ORDER INTERACTION ADVERTISING MANAGER LOYALTY PAYPAL
do
	eval process_base $DB "\$${DB}_DBNAME" "\$${DB}_DBUSER" "\$${DB}_DBPASS"
done


# --- Redis Processing ---
REDIS_HOST="${REDIS_PORT_6379_TCP_ADDR}:${REDIS_PORT_6379_TCP_PORT}"

export REDIS_HOST


# --- ElasticSearch Processing ---
ELASTICSEARCH_HOST="${ELASTICSEARCH_PORT_9300_TCP_ADDR}:${ELASTICSEARCH_PORT_9300_TCP_PORT}"

export ELASTICSEARCH_HOST

env $* 


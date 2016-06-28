#!/bin/sh

# either 9000 or 9001
ACTIVE=$(etcdctl ls --recursive /services/tomtopwebsite-active | cut -f 4 -d '/' | cut -f 2 -d ':')

case "$ACTIVE" in 
	"9000")
		UPDATE=9001
		;;
	"9001")
		UPDATE=9000
		;;
	*)
		UPDATE=9000
		;;
esac

echo Active Instance: $ACTIVE
echo Update Instance: $UPDATE

sudo /bin/sh -c "echo \"BUILD_NUMBER=$1\" > /etc/env.d/tomtopwebsite@${UPDATE}.env"

echo "Switching active instance"
git pull
fleetctl stop tomtopwebsite*\@${UPDATE}.service
fleetctl destroy tomtopwebsite*\@${UPDATE}.service
fleetctl submit tomtopwebsite*\@${UPDATE}.service
fleetctl start tomtopwebsite*\@${UPDATE}.service

etcdctl watch /services/tomtopwebsite/192.168.7.13:${UPDATE}

etcdctl rm /services/tomtopwebsite-active/192.168.7.13:${ACTIVE}
etcdctl set /services/tomtopwebsite-active/192.168.7.13:${UPDATE} "192.168.7.13:${UPDATE}"
STATUS=$?

`dirname $0`/purge.sh

exit $STATUS

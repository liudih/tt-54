FROM 192.168.7.15:5000/nginx

MAINTAINER KM Tong "kmtong@gmail.com"

RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*
RUN cd /usr/bin; wget --no-check-certificate -O confd https://github.com/kelseyhightower/confd/releases/download/v0.6.3/confd-0.6.3-linux-amd64
RUN chmod a+x /usr/bin/confd

COPY conf.d/ /etc/confd/conf.d
COPY templates/ /etc/confd/templates

CMD service nginx start && confd -interval=5 -node ${ETCD_NODE}

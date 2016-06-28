Why
---

We used "confd" to configure the default.conf inside /etc/nginx/conf.d and perform reload
when configuration changes.

Building
--------

    docker build --tag=192.168.7.15:5000/tomtopwebsite/nginx .
    docker push 192.168.7.15:5000/tomtopwebsite/nginx



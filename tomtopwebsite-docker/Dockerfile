FROM 192.168.7.15:5000/jeanblanchard/busybox-java:8
MAINTAINER KM Tong <kmtong@gmail.com>

COPY packages/tomtopweb-1.0-SNAPSHOT.zip /tmp/

RUN ( cd /usr/share; unzip /tmp/*.zip )

RUN rm -f /tmp/*.zip

COPY startup.sh /usr/share/tomtopweb-1.0-SNAPSHOT/bin/

RUN ( app_classpath=`grep 'declare -r app_classpath' /usr/share/tomtopweb-1.0-SNAPSHOT/bin/tomtopweb | sed 's/declare -r //g' | sed 's:[]\[\^\$\.\*\/]:\\\\&:g'` && sed -i "s/__APP_CLASSPATH__/$app_classpath/g" /usr/share/tomtopweb-1.0-SNAPSHOT/bin/startup.sh )

RUN ( app_mainclass=`grep 'declare -r app_mainclass' /usr/share/tomtopweb-1.0-SNAPSHOT/bin/tomtopweb | sed 's/declare -r //g' | sed 's:[]\[\^\$\.\*\/]:\\\\&:g'` && sed -i "s/__APP_MAINCLASS__/$app_mainclass/g" /usr/share/tomtopweb-1.0-SNAPSHOT/bin/startup.sh )

RUN chmod a+x /usr/share/tomtopweb-1.0-SNAPSHOT/bin/startup.sh

EXPOSE 9000

CMD /usr/share/tomtopweb-1.0-SNAPSHOT/bin/startup.sh

COPY entrypoint.sh /
RUN chmod a+x /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]

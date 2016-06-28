FROM centos:7

MAINTAINER "KM Tong" <kmtong@gmail.com>

ENV container docker
RUN yum -y swap -- remove fakesystemd -- install systemd systemd-libs
RUN yum -y update; yum clean all; \
(cd /lib/systemd/system/sysinit.target.wants/; for i in *; do [ $i == systemd-tmpfiles-setup.service ] || rm -f $i; done); \
rm -f /lib/systemd/system/multi-user.target.wants/*;\
rm -f /etc/systemd/system/*.wants/*;\
rm -f /lib/systemd/system/local-fs.target.wants/*; \
rm -f /lib/systemd/system/sockets.target.wants/*udev*; \
rm -f /lib/systemd/system/sockets.target.wants/*initctl*; \
rm -f /lib/systemd/system/basic.target.wants/*;\
rm -f /lib/systemd/system/anaconda.target.wants/*;
VOLUME [ "/sys/fs/cgroup" ]
CMD ["/usr/sbin/init"]

# ---- basic support for UAT, similar to production ----
RUN yum -y install openssh-server; yum clean all; systemctl enable sshd.service

# Install GIT
RUN yum -y install git wget tar

# Install Puppet
RUN rpm -ivh http://yum.puppetlabs.com/puppetlabs-release-el-7.noarch.rpm
RUN yum -y install puppet

# Install Java
RUN (cd /opt/; \
	wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u31-b13/jdk-8u31-linux-x64.tar.gz"; \
	tar xzf jdk-8u31-linux-x64.tar.gz; \
	cd /opt/jdk1.8.0_31/; \
	alternatives --install /usr/bin/java java /opt/jdk1.8.0_31/bin/java 2 ; \
	alternatives --install /usr/bin/jar jar /opt/jdk1.8.0_31/bin/jar 2 ; \
	alternatives --install /usr/bin/javac javac /opt/jdk1.8.0_31/bin/javac 2 ; \
	alternatives --set jar /opt/jdk1.8.0_31/bin/jar ; \
	alternatives --set javac /opt/jdk1.8.0_31/bin/javac)

# Install misc utiltites
RUN yum -y install hostname initscripts

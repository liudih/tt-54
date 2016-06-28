TomTop Website Project
======================

This is a Play (on Java) Application.

[![Build Status](http://192.168.7.14:8080/buildStatus/icon?job=TomtopWeb-Build)](http://192.168.7.14:8080/job/TomtopWeb-Build/)
[![Test Status](http://192.168.7.14:8080/job/TomtopWeb-SIT-Test/badge/icon)](http://192.168.7.14:8080/job/TomtopWeb-SIT-Test/)


Development Environment Setup
------------------------------

我们采用Docker容器提供应用外围的服务，包括PostgreSQL，Redis等。

Windows:

* 安装VirtualBox 4.3.12（不要安装4.3.12以上的版本，会启动不了Docker VM）
  下载地址：http://download.virtualbox.org/virtualbox/4.3.12/VirtualBox-4.3.12-93733-Win.exe

* 安装Boot2Docker（不要安装里面自带的VirtualBox，其他的都安装）
  下载地址：https://github.com/boot2docker/windows-installer/releases/latest


安装好以后，启动桌面的 Boot2Docker Start ，会进入Docker VM里面。

由于我们用内部镜像库，并没有HTTPS，故此第一次进入Docker VM，要执行

    sudo sh -c "echo EXTRA_ARGS=\'--insecure-registry 192.168.7.15:5000\' > /var/lib/boot2docker/profile"
    sudo /etc/init.d/docker restart


随后，执行

    docker run --name postgres -d -p 5432:5432 -e 'POSTGRES_USER=tomtop' -e 'POSTGRES_PASSWORD=tomtop' 192.168.7.15:5000/postgres
    docker run --name phppgadmin -d -p 8000:80 --link postgres:postgresql 192.168.7.15:5000/maxexcloo/phppgadmin
    docker run --name redis -d -p 6379:6379 192.168.7.15:5000/redis
    docker run --name elasticsearch -d -p 9200:9200 -p 9300:9300 192.168.7.15:5000/elasticsearch elasticsearch -Des.script.disable_dynamic=false -Des.script.groovy.sandbox.enabled=true


第二次进入docker的时候 只需要执行

    docker restart postgres phppgadmin redis elasticsearch

Docker VM 的默认地址为 192.168.59.103，你可以试试用浏览器打开 phpPgAdmin 界面，
数据库用户/密码为：tomtop/tomtop

    http://192.168.59.103:8000

准备完毕，开始开发。在另一个 cmd 执行：

    dev 192.168.59.103

参数为Docker VM的服务器地址。执行之后会启动 Play 的 activator。
这个命令已经根据环境变量链接到 Docker VM 的 PostgreSQL 了。

由于我们采用Liquibase跟踪数据库变更，可以访问一下链接初始化数据库结构：

    http://localhost:9000/common/liquibase/update

全库删除＋更新结构＋测试数据:

    http://localhost:9000/common/liquibase/reset

初始化搜素引擎索引:

    http://localhost:9000/product/_indexing?drop=true&create=true


合并申请 Merge Request
=============

为避免代码胡乱提交，导致垃圾积压，影响软件质量，我们用轻量的代码审核方法：Merge Request（合并请求）。
做法如下：


先建立自己的库
--------------

在项目主页的右侧，会看到一个 Fork ，Fork 是在服务器建立分支的意思。按了就拷贝了一份代码到你自己的空间。
如：http://192.168.7.15:10080/kmtong/tomtopwebsite


克隆自己的空间
--------------

    git clone http://192.168.7.15:10080/--替换成个人空间--/tomtopwebsite

操作git commit、push等和一般的操作无异


建立合并请求
------------

* 到 Merge Request 选项卡，按一下`+ New Merge Request`添加一个新的请求。
* 选择要合并的来源地（分支）、目的地（分支）、和处理者（审核人）等等，加上适当的变更描述。
* 成功提交申请后，有待审核人员做审核和合并，你的代码才会在主干上出现。


需要合并主干更新
----------------

主干可能有一些更新是你自己的空间的分支是不存在的。在这个时候，你需要合并主干的代码。

作为主干，可以想象为另一个源、远程库。首先需要添加一个远程库配置到你本地git上：

    git remote add mainline http://192.168.7.15:10080/tomtopwebsite/tomtopwebsite

这个操作只需要做一次，你就会有一个叫`mainline`远程库的定义了。

    git pull mainline master

意思是从 `mainline` 的 `master` 合并过来本地。


问题？
=====

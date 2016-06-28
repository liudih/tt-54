@echo off

set SERVER_IP=%1%

set DBDRIVER_BASE=org.postgresql.Driver
set DBURL_BASE=jdbc:postgresql://%SERVER_IP%:5432/base
set DBUSER_BASE=tomtopwebsite
set DBPASS_BASE=tomtopwebsite123

set DBDRIVER_PRODUCT=org.postgresql.Driver
set DBURL_PRODUCT=jdbc:postgresql://%SERVER_IP%:5432/product
set DBUSER_PRODUCT=tomtopwebsite
set DBPASS_PRODUCT=tomtopwebsite123

set DBDRIVER_IMAGE=org.postgresql.Driver
set DBURL_IMAGE=jdbc:postgresql://%SERVER_IP%:5432/image
set DBUSER_IMAGE=tomtopwebsite
set DBPASS_IMAGE=tomtopwebsite123

set DBDRIVER_MEMBER=org.postgresql.Driver
set DBURL_MEMBER=jdbc:postgresql://%SERVER_IP%:5432/member
set DBUSER_MEMBER=tomtopwebsite
set DBPASS_MEMBER=tomtopwebsite123

set	DBDRIVER_INTERACTION=org.postgresql.Driver
set DBURL_INTERACTION=jdbc:postgresql://%SERVER_IP%:5432/interaction
set DBUSER_INTERACTION=tomtopwebsite
set DBPASS_INTERACTION=tomtopwebsite123

set DBDRIVER_ADVERTISING=org.postgresql.Driver
set DBURL_ADVERTISING=jdbc:postgresql://%SERVER_IP%:5432/advertising
set DBUSER_ADVERTISING=tomtopwebsite
set DBPASS_ADVERTISING=tomtopwebsite123

set DBDRIVER_ORDER=org.postgresql.Driver
set DBURL_ORDER=jdbc:postgresql://%SERVER_IP%:5432/order
set DBUSER_ORDER=tomtopwebsite
set DBPASS_ORDER=tomtopwebsite123

set DBDRIVER_LOYALTY=org.postgresql.Driver
set DBURL_LOYALTY=jdbc:postgresql://%SERVER_IP%:5432/loyalty
set DBUSER_LOYALTY=tomtopwebsite
set DBPASS_LOYALTY=tomtopwebsite123

set DBDRIVER_CART=org.postgresql.Driver
set DBURL_CART=jdbc:postgresql://%SERVER_IP%:5432/cart
set DBUSER_CART=tomtopwebsite
set DBPASS_CART=tomtopwebsite123

set DBDRIVER_MANAGER=org.postgresql.Driver
set DBURL_MANAGER=jdbc:postgresql://%SERVER_IP%:5432/manager
set DBUSER_MANAGER=tomtopwebsite
set DBPASS_MANAGER=tomtopwebsite123


set REDIS_HOST=%SERVER_IP%:6379
set ELASTICSEARCH_HOST=%SERVER_IP%:9300

activator.bat


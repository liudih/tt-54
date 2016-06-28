INSTRUCTIONS
=============

You need to define the following environment variables for this image
to work correctly.

    BASE_DBNAME: Required (Database name in PostgreSQL)
    BASE_DBUSER: Optional
    BASE_DBPASS: Optional

    IMAGE_DBNAME: Required (Database name in PostgreSQL)
    IMAGE_DBUSER: Optional
    IMAGE_DBPASS: Optional

    PRODUCT_DBNAME: Required (Database name in PostgreSQL)
    PRODUCT_DBUSER: Optional
    PRODUCT_DBPASS: Optional

    MEMBER_DBNAME: Required (Database name in PostgreSQL)
    MEMBER_DBUSER: Optional
    MEMBER_DBPASS: Optional

Optional parameters are propagated from postgresql container if you are using --link.


Sample run command:

    docker run -it --rm \
	-e BASE_DBNAME=base \
	-e IMAGE_DBNAME=image \
	-e PRODUCT_DBNAME=product \
	-e MEMBER_DBNAME=member \
	--link postgres:basedb \
	--link postgres:imagedb \
	--link postgres:productdb \
	--link postgres:memberdb \
	tomtop/website

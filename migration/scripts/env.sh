DB_HOST=192.168.7.13
DB_NAME=magento
DB_USER=magento
DB_PASS=magento

JARFILE=migration.jar

DISTURL=http://192.168.7.15:8080/artifactory/libs-snapshot-local/com/tomtop/website/migration/0.0.1-SNAPSHOT/migration-0.0.1-20150311.011924-43-jar-with-dependencies.jar

SELLPOINT_URL=http://192.168.7.13:82/migration/sellpoint.tgz

USE_CURL=true

ENDPOINT=http://localhost:9000

MEMBER_ENDPOINT=$ENDPOINT/member/api/member/push
MEMBER_POINT_ENDPOINT=$ENDPOINT/loyalty/api/points/push
CATEGORY_ENDPOINT=$ENDPOINT/api/category/push
WEBSITECATEGORY_ENDPOINT=$ENDPOINT/api/websitecategory/push
PRODUCT_ENDPOINT=$ENDPOINT/api/product/push
PRODUCT_SALE_ENDPOINT=$ENDPOINT/api/product/promotionprice/push
ORDER_ENDPOINT=$ENDPOINT/checkout/api/order/push
CATEGORY_ATTRIBUTE_ENDPOINT=$ENDPOINT/api/category/attribute

PRODUCT_CATEGORY_RELATION_ENDPOINT=$ENDPOINT/api/product/category/push
PRODUCT_PRICE=$ENDPOINT/api/product/price
PRODUCT_URL=$ENDPOINT/api/product/url
PRODUCT_STATUS=$ENDPOINT/api/product/status
PRODUCT_MULTI_ATTRIBUTE=$ENDPOINT/api/mutilproduct/attribute
PRODUCT_SELLINGPOINTS=$ENDPOINT/api/product/sellingpoints
PRODUCT_IMAGES=$ENDPOINT/api/product/images

KEYWORD_ENDPOINT=$ENDPOINT/api/product/keyword/push
COMMENT_ENDPOINT=$ENDPOINT/review/api/review/push

function execute {
        java -jar migration.jar -D $DB_NAME -H $DB_HOST -U $DB_USER -P $DB_PASS $*
}

function get_remote {
	if [ "$USE_CURL" = "true" ]
	then
		curl -o $2 $1
	else
		wget -O $2 $1
	fi
}


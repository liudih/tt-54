#!/bin/bash

. env.sh

./prepare.sh

execute import -P data/categories/categorys.json -W $CATEGORY_ENDPOINT
execute import -P data/categories/tran -W $CATEGORY_ENDPOINT -S 1
execute import -P data/categories/websitecategorys.json -W $WEBSITECATEGORY_ENDPOINT
execute import -p data/categories/attribute -W $CATEGORY_ATTRIBUTE_ENDPOINT -S 50

execute import -P data/relations/product -W $PRODUCT_ENDPOINT -S 500
execute import -P data/relations/nocategoryproduct -W $PRODUCT_ENDPOINT -S 500
execute import -P data/products/promotions -W $PRODUCT_SALE_ENDPOINT -S 500
execute import -P data/relations/skuCategoryRelation.json -W $PRODUCT_CATEGORY_RELATION_ENDPOINT -S 1
execute import -P data/relations/sku-category.json -W $PRODUCT_CATEGORY_RELATION_ENDPOINT -S 1

execute import -P data/products/multi -W $PRODUCT_PRICE -S 500 -T product-update
execute import -P data/products/multi -W $PRODUCT_URL -S 500 -T product-update
execute import -P data/products/multi -W $PRODUCT_STATUS -S 500 -T product-update
execute import -P data/products/multi -W $PRODUCT_IMAGES -S 500 -T product-update
execute import -P data/products/multi -W $PRODUCT_MULTI_ATTRIBUTE -S 500 -T product-multi
execute import -P source/sellpoint -W $PRODUCT_SELLINGPOINTS -S 500 -T product-sellpoint

execute import -P data/members -T member -W $MEMBER_ENDPOINT -S 500
execute import -P data/members -T member -W $MEMBER_POINT_ENDPOINT -S 500
execute import -P data/orders -T order -W $ORDER_ENDPOINT -S 500

execute import -P data/keywords -W $KEYWORD_ENDPOINT -S 500
execute import -P data/comments -W $COMMENT_ENDPOINT -S 500 -T comment

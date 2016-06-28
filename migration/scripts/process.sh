#!/bin/bash

. env.sh

./prepare.sh

# translation
execute trans -L ru,es -S data/products -SP source/sellpoint -D data/product_trans -T product
execute trans -L ru,es -S data/categories/categorys.json -D data/categories -T category

# sku与品类关系匹配，没匹配就为空(需要先导品类)
execute relation -T source/sku.xls -D data/relations -S data/categories/categorys.json -P data/products

#生成sku与品类路径对应关系
execute category-relation -D data/relations -CP source/category_category.xls -CE source/erpcategory.json -CS source/skucategoryjson.txt

# 有执行翻译的话在执行一下这个
execute relation -T source/sku.xls -D data/relations -S data/categories/categorys.json -P data/product_trans


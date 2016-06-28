#!/bin/bash

. env.sh

./prepare.sh

execute category-export -D data/categories -T source/category.xls -TV source/viewcategory.xls

execute product-export -D data/products -SP source/sellpoint &
PRODUCT_SP_PID=$!

execute product-export -D data/products -SP source/sellpoint -M true &
PRODUCT_PID=$!

execute order-export -D data/orders &
ORDER_PID=$!

execute member-export -D data/members &
MEMBER_PID=$!

execute comment-export -D data/comments &
COMMENT_PID=$!

execute keyword-export -D data/keywords &
KEYWORD_PID=$!

execute product-groupprice -D data/groupprices &
GROUPPRICE_PID=$!

shutdown() {
    trap - INT              # Resore signal handling for SIGINT
    # Get our process group id
    PGID=$(ps -o pgid= $$ | grep -o [0-9]*)

    # Kill it in a new new process group
    kill -- -$PGID
    exit 0
}

trap "shutdown" INT            # Set up SIGINT trap to call function.

wait $PRODUCT_PID $ORDER_PID $MEMBER_PID

trap - INT

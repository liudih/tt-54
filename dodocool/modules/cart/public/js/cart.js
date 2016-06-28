$(document).on("click", "#add-to-cart", function() {
	var data = "";
	var list = [];
	var map = {};
	var listingid = $(this).data('listingid');
	map['bismain'] = "false";
	map['clistingid'] = listingid;
	map['qty'] = 1;
	list[0] = map;
	data = JSON.stringify(list);
	var url = cartRoutes.controllers.cart.Cart.saveCart(data).url;
	$.ajax({
		url : url,
		type : "get",
		dataType : "json",
		success : function(data) {
			var result = data.result;
			if ("success" == result) {
				showCartSize();
			} else {
				alert('failure !');
			}
		}
	});
});

function showCartSize() {
	var url = cartRoutes.controllers.cart.Cart.showCartSize().url;
	$.ajax({
		url : url,
		type : "get",
		dataType : "json",
		success : function(data) {
			var result = data.count;
			console.info($('#cartNum_head'));
			$('#cartNum_head').text(result);
		}
	});
}

$(document).on("click", ".qty_num_Reduction", function() {
	var n = $(this).next("input");
	var num = parseInt(n.val());
	if (num > 1) {
		num = num - 1;
	}
	n.val(num);
	submitNum($(this), num); // 更新数量
});

//增加数量
$(document).on("click", ".qty_num_add", function() {
	var n = $(this).prev("input");
	var num = parseInt(n.val());
	if (num < 9999) {
		num = num + 1;
	}
	n.val(num);
	submitNum($(this), num); // 更新数量
});

//删除单个商品
$(document).on("click", ".glyphicon", function() {
	$(this).children(".deletePop").toggle();
});

function delCart(lid) {
	var url = cartRoutes.controllers.cart.Cart.delCart(lid, "").url;
	$.ajax({
		url : url,
		type : "GET",
		dataType : "json",
		async : true,
		success : function(data) {
			if (data.result == "success") {
				refresh();
			}
		},
		complete : function() {
		}
	});
}

function refreshTotal() {
	$.get(cartRoutes.controllers.cart.Cart.refreshTotal().url, function(html) {
		$("#cart_grand_total").replaceWith(html);
	}, "html");
}

function refreshCartTable() {
	$.get(cartRoutes.controllers.cart.Cart.refreshCartTable().url, function(
			html) {
		$("#cart_view_table").replaceWith(html);
		refreshTotal();
	}, "html");
}

function refresh(){
	refreshCartTable();
	refreshTotal();
}

function submitNum(node, num) {
	var cid = node.data('cartid');
	var url = cartRoutes.controllers.cart.Cart.editNum(cid, num).url;
	$.ajax({
				url : url,
				type : "get",
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						refresh();
					} else if (data.result == "not enough") {
						node.parent().find("input:eq(0)").val(data.oldnum);
						alert("Low stocks!");
					} else {
						alert("update error!");
					}
				},
				complete : function() {
				}
			});
}

$(function(){
    var offset = $("#cartIt").offset();
    $(".add_cart").click(function(event){
        var img = $("#mainImg").attr('src');
        var flyer = $('<img src="'+img+'">');
        flyer.fly({
            start: {
                left: event.pageX, //开始位置（必填）#fly元素会被设置成position: fixed
                top: event.pageY //开始位置（必填）
            },
            end: {
                left: offset.left+60, //结束位置（必填）
                top: offset.top+50, //结束位置（必填）
                width: 0, //结束时宽度
                height: 0 //结束时高度
            },
            onEnd: function() {
                this.destory(); //销毁抛物体
            }
        });

    });
});

$(document).on("click","#cartIt a",function(){
	var count = $("#cartNum_head").text();
	if(count <= 0){
		return false;
	}
});


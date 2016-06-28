define([ 'jquery', 'jqueryjson' ], function() {
	function WholeSale() {
	}

	WholeSale.prototype = {
		initPage : function() {
			var $this = this;
			$(".chooseProduct").unbind('click');
			$(".chooseProduct").click(function() {
				$(this).children("span").toggleClass("choosed");
				$this.chooseProduct();
			});
			$(".qty_add").unbind('click');
			$(".qty_add").click(function() {
				$this.addQty($(this));
			});
			$(".qty_reduction").unbind('click');
			$(".qty_reduction").click(function() {
				$this.reductionQty($(this));
			});
			$(".deleteYes").unbind('click');
			$(".deleteYes").click(function() {
				$this.deleteProduct($(this));
			});
			$(".chooseAll").unbind('click');
			$(".chooseAll").click(function() {
				$this.chooseAll($(this));
			});
			$(".quantity-text").unbind('blur');
			$(".quantity-text").blur(function() {
				$this.changeQty(this);
			});
			$(".number").unbind('keyup');
			$(".number").keyup(function() {
				$this.checkNum(this);
			});
			$(".quantity-text").keydown(function(event) {
				if (event.keyCode == 13) {
					$this.changeQty(this);
				}
			});
		},

		addWholeSaleProduct : function() {
			var form = $("#wholesaleform");
			var url = form.attr("action");
			var $this = this;
			var csku = form.find('input[name=csku]').val();
			var iqty = form.find('input[name=iqty]').val();
			if ("" == csku || "" == iqty) {
				var message = "please input sku and qty!";
				$('.message').html(message);
				$('.pu_pop').show();
				return false;
			}
			//验证停售状态和库存
			var b = false;
			$.ajax({
				url: "/checkout/checkproductstatus",
				type: "get",
				dataType: "json",
				async: false,
				data:{
					"sku" : csku,
					"qty" : iqty
				},
				success:function(data){
					if(data.result=="success"){
						b = true;
					}else{
						pophtml("Error",data.result);
						b = false;
					}
				}
			});
			if(!b){
				return false;
			}
			
			$.post(url, form.serialize(), function(data) {
				if ("error" in data) {
					var message = data['error'];
					$('.message').html(message);
					$('.pu_pop').show();
				} else {
					$this.chooseProduct();
				}
			}, "json");
		},

		chooseProduct : function() {
			var i = 0;
			var list = [];
			$('.chooseProduct').each(function() // multiple checkbox的name
			{
				if ($(this).children("span").hasClass("choosed")) {
					var wproductid = $(this).data('wproductid');
					list[i] = wproductid;
					i++;
				}
			});
			var data = JSON.stringify({
				"wproductIds" : list
			});
			var url = wholesale.controllers.wholesale.WholeSaleProductList
					.chooseProduct().url;
			var $this = this;
			$.ajax({
				url : url,
				type : "post",
				data : data,
				contentType : "application/json; charset=utf-8",
				dataType : "html",
				success : function(html) {
					$('#wholeproduct-html').replaceWith(html);
					$this.initPage();
				}
			});
		},
		
		addQty: function(e) {
			var maxQty = $(e).data('maxqty');
			var qty = $(e).data('qty');
			if (parseInt(maxQty) <= parseInt(qty)) {
				return false;
			}
			$(e).data('qty', qty + 1);
			var $this = this;
			this.updateQty(e);
		},
		
		reductionQty: function(e) {
			var minQty = $(e).data('minqty');
			var qty = $(e).data('qty');
			if (parseInt(minQty) >= parseInt(qty)) {
				return false;
			}
			$(e).data('qty', qty - 1);
			var $this = this;
			this.updateQty(e);
		},
		
		changeQty: function(e) {
			var minQty = $(e).data('minqty');
			var qty = $(e).val();
			if (isNaN(qty) || parseInt(minQty) >= parseInt(qty) || parseInt(qty) > 9999) {
				qty = minQty;
			}
			$(e).data('qty', qty);
			var $this = this;
			this.updateQty(e);
		},

		updateQty : function(e) {
			var iid = $(e).data('iid');
			var qty = $(e).data('qty');
			var url = wholesale.controllers.wholesale.WholeSaleProductList
					.updateProductQty(iid, qty).url;
			var $this = this;
			$.post(url, function(json) {
				var result = json['result'];
				if (result == true) {
					$this.chooseProduct();
				} else {
					alter('fair');
				}
			}, "json");
		},

		deleteProduct : function(e) {
			var iid = $(e).data('iid');
			var url = wholesale.controllers.wholesale.WholeSaleProductList
					.deleteProduct(iid).url;
			var $this = this;
			$.post(url, function(json) {
				var result = json['result'];
				if (result == true) {
					$this.chooseProduct();
				} else {
					alter('fair');
				}
			}, "json");
		},

		chooseAll : function(e) {
			if ($(e).children('span').hasClass('aftersAll') == false) {
				$('.rightThis').children('span').removeClass('afters');
				$('.rightThis').children('span').removeClass('choosed');
			} else {
				$('.rightThis').children('span').addClass('afters');
				$('.rightThis').children('span').addClass('choosed');
			}
			var $this = this;
			$this.chooseProduct();
		},

		deleteAllProduct : function() {
			var i = 0;
			var list = [];
			$('.chooseProduct').each(function() // multiple checkbox的name
			{
				if ($(this).children("span").hasClass("choosed")) {
					var wproductid = $(this).data('wproductid');
					list[i] = wproductid;
					i++;
				}
			});
			var data = JSON.stringify({
				"wproductIds" : list
			});
			var url = wholesale.controllers.wholesale.WholeSaleProductList
					.deleteAllProduct().url;
			var $this = this;
			$.ajax({
				url : url,
				type : "post",
				data : data,
				contentType : "application/json; charset=utf-8",
				dataType : "html",
				success : function(html) {
					$('#wholeproduct-html').replaceWith(html);
					$this.initPage();
				}
			});
		},
		
		checkNum : function(e) {
			var value = $(e).val();
			value = value.match(/\d+/);
			if (value != null && value != 0) {
				value = parseInt(value);
			}
			$(e).val(value);
		}
	}

	return WholeSale;
});
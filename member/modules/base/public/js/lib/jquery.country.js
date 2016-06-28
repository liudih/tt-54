
/**
 * 国家插件
 * demo : $('#id').country();
 * 当某个国家被选中时你需要处理某些逻辑的时候
 *  $('#id').country({selected : function(){
 *  	//yourself code
 *  }});
 * @author lijun
 */
;(function($, document, undefined) {

	var pluginName = 'country',
		defaults = {
			countryIdInputName : 'icountry',
			countryCodeInputName : 'countryCode',
			defaultCountryId : 0
		};
			
			
	function Plugin(element,options) {
		if(typeof(element) === 'string'){
			this.element = $('#' + element);
		}else{
			this.element = element;
		}
		
		this.options = $.extend({}, defaults, options);

		this._defaults = defaults;
		this._name = pluginName;

		this.init();
	}

	Plugin.prototype = {
		init: function() {
			this.options.id = pluginName + '_' + Math.floor(Math.random() * 1e9);

			this._createElements();
			this._handleFocusEvent.apply(this);
			this._handleKeyupEvent.apply(this);
			//this._handleBlurEvent.apply(this);
			this._handleSelectEvent.apply(this);
		},
		_createElements: function() {
			var ele = [];
			ele.push('<div id="' + this.options.id + '" class="newshopping_address_country">');
			ele.push('<input tag="countryCode" type="text" name="' + this.options.countryCodeInputName + '" style="display:none;"/>');
			ele.push('<input tag="countryId" type="text" name="' + this.options.countryIdInputName + '" style="display:none;"/>');
			ele.push('<input tag="countryName" type="text" name="countryName" style="display:none;"/>');
			ele.push('<div class="address_country"><div tag="text"></div><span><i></i></span></div>');
			ele.push('<div class="country_all" style="display: none;">');
			ele.push('<div class="search_country"><input type="text"></div>');
			ele.push('</div>');
			ele.push('</div>');
			this.element.before(ele.join(''));
			this.element.appendTo('#' + this.options.id + ' .country_all');
		},
		getValue: function() {
			return $('#' + this.options.id).find('input[tag=value]').val();
		},
		
		clear : function(){
			var parent = $('#' + this.options.id);
			parent.find('input[name]').each(function(){
				$(this).val('');
			});
			parent.find('div[tag=text]').text('');
			parent.find('.country_all').hide();
		},
		
		destroy: function() {
			
		},
		
		_handleSelectEvent: function() {
			var textEle = $('#' + this.options.id).find('div[tag=text]');
			var countryId = $('#' + this.options.id).find('input[tag=countryId]');
			var countryCode = $('#' + this.options.id).find('input[tag=countryCode]');
			var countryName = $('#' + this.options.id).find('input[tag=countryName]');
			var select =  $('#' + this.options.id).find('.country_all');
			var label =  $('#' + this.options.id).siblings('label');
			$('#' + this.options.id).find('li').click(function(){
				var text = $(this).text();
				var code = $(this).attr('code');
				var cid = $(this).attr('cid');
				
				label.hide();
				
				textEle.text(text);
				countryName.val(text);
				countryCode.val(code);
				countryId.val(cid);
				select.hide();
			}
			);
		},
		
		_handleFocusEvent : function(){
			var c = $('#' + this.options.id);
			var textEle = $('#' + this.options.id).find('.address_country');
			var select =  $('#' + this.options.id).find('.country_all');
			textEle.click(function(){
				select.show();
			});
			c.hover(function(){
				select.hide();
			});
		},
		
		_handleBlurEvent : function(){
			$('#' + this.options.id).hover(function(){},function(){
				$(this).find('div[tag=select]').hide();
			});
		},
		
		_handleKeyupEvent: function() {
			var textEle = $('#' + this.options.id).find('.search_country input');
			var select = $('#' + this.options.id).find('.country_list li');
			var $this = this;
			textEle.keyup(function(){
				var value = $(this).val();
				value = value.toLowerCase();
				select.each(function(){
					var text = $(this).text().toLowerCase();
					if (text.indexOf(value) != 0) {
						$(this).hide();
					} else {
						$(this).show();
					}
				});
			})
		}
		
	};

	$[pluginName] = Plugin;
	$[pluginName + 'Options'] = [];
	$.fn[pluginName] = function(options){
		var object = new $[pluginName](this.first(),options);
		$[pluginName + 'Options'].push(object.options);
		return object;
	}
	
}(jQuery, document));
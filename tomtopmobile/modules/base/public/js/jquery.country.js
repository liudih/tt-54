
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

	var pluginName = 'country',getAllCountryUrl = '/base/getAllCountry',
		defaults = {
			countryInputName : 'countryId',
			defaultCountryId : 0,
			selected : function() { 
				var parentId = $(this).parent().attr('parentId');
				var cid = $(this).attr('cid');
				var text = $(this).text();
				$('#' + parentId).find('input[tag=value]').val(cid);
				$('#' + parentId).find('input[tag=value]').attr('cname',text);;
				var textEle = $('#' + parentId).find('input[tag=text]');
				textEle.val(text);
				$('#' + parentId).find('div[tag=select]').hide();
				textEle.removeClass('input_error');
				}
		};
	var allCountry = [];
	//加载所有国家
	$.ajax({
			url: getAllCountryUrl,
			type: "get",
			dataType: "json",
			success:function(data){
				$.allCountry = allCountry = data;	
				$($[pluginName + 'Options']).each(function(index,op){
					if(op.defaultCountryId){
						for(var i = 0 ; i < allCountry.length ; i++){
							var c = allCountry[i];
							if(c.iid == op.defaultCountryId){
								var textEle = $('#' + op.id).find('input[tag=text]');
								var valueEle = $('#' + op.id).find('input[tag=value]');
								valueEle.val(c.iid);
								textEle.val(c.cname);
								valueEle.attr('cname',c.cname);
								break;
							}
						}
					}
				});
			},
			error : function(){
				
			}
	});
			
			
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
			this._handleBlurEvent.apply(this);
			this._handleSelectEvent.apply(this);
		},
		_createElements: function() {
			var ele = [];
			ele.push('<div id="' + this.options.id + '" class="countriesSelect input_control">');
			ele.push('<input tag="text" cname="" class="txtInput placeholder_country placeholder_countryClick"  type="text" placeholder="Country" />');
			ele.push('<input tag="value" type="text" name="' + this.options.countryInputName + '" style="display:none;"/>');
			ele.push('<i class="sClean"> </i>');
			ele.push('<div tag="select" class="select_country select_countryNone">');
			ele.push('<ul parentId="' + this.options.id + '" class="country_list"></ul>');
			ele.push('</div>');
			ele.push('</div>');
			
			this.element.append(ele.join(''));
		},
		getValue: function() {
			return $('#' + this.options.id).find('input[tag=value]').val();
		},
		
		clear : function(){
			var parent = $('#' + this.options.id);
			parent.find('input[tag=value]').val('');
			parent.find('input[tag=text]').val('');
			$(parent).find('div[tag=select]').hide();
		},
		
		destroy: function() {
			this.empty();
		},
		
		_handleSelectEvent: function() {
			$('#' + this.options.id).find('li').click(
				this.options.selected
			);
		},
		
		_handleFocusEvent : function(){
			var textEle = $('#' + this.options.id).find('input[tag=text]');
			var select =  $('#' + this.options.id).find('div[tag=select]');
			textEle.focus(function(){
				select.show();
			})
		},
		
		_handleBlurEvent : function(){
			$('#' + this.options.id).hover(function(){},function(){
				$(this).find('div[tag=select]').hide();
			});
		},
		
		_handleKeyupEvent: function() {
			var textEle = $('#' + this.options.id).find('input[tag=text]');
			var select = $('#' + this.options.id).find('.country_list');
			var $this = this;
			textEle.keyup(function(){
				var value = $(this).val();
				select.empty();
				for(var i = 0 ; i < allCountry.length ; i++){
					var c = allCountry[i];
					if(c.cname.toLowerCase().indexOf(value.toLowerCase()) == 0){
						select.append('<li cid="' + c.iid + '">' + c.cname + '</li>');
					}
				}
				$this._handleSelectEvent();
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
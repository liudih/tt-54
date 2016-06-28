/*
 * productColorizer - jQuery Plugin
 * version: 1.2 October 2012
 * @requires jQuery v1.6 or later
 *
 * Examples at http://nikorablin.com/sandbox/productColorizer/
 * Free to use and abuse under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 *
 */
(function($){
    $.fn.productColorizer = function(options) {
		
		// defaults
		var defaults = {  
			transparency: 0.55,
			swatchTransparency: 0.75,
			secondaryTransparency: 0.55,
			swatchClass: '.swatch'
		};
		// extend defaults
		var options = $.extend(defaults, options); 
		
		return this.each(function() {
			
			// init objects
			var o = options;
			var obj = $(this);
			var swatch = obj.find(o.swatchClass);
			var swatches = obj.find(o.swatchClass + " a");
			var mask = obj.find('.mask');
			
			// set swatch colors with the rel values
			$(swatches).each(function(){
				var color = "rgba(" + $(this).attr('rel') + "," + o.swatchTransparency + ")";
				$(this).html('<span>'+$(this).html()+'</span>');
				$(this).find('span').css('background', color);
				if($.browser.msie) {
					color = $(this).attr('rel');
					var colors = color.split(",");
					color = "#" + colorToHex(colors);
					$(this).find('span').css({'background-color': color});
				}
			});
			
			// set background color of mask with rel values and transparency
			$(swatches).hover(function(e) {
				e.preventDefault();
				$(swatches).removeClass('active');
				$(this).addClass('active');
				var color = "rgba(" + $(this).attr('rel') + "," + o.transparency + ")";
				var mask = $(this).attr('href');
				if($(mask).attr('role')) {
					$(mask).empty();
					var R = Raphael(mask.substring(1), $(mask).width(), $(mask).height());
			    	var style = {
			            fill: "rgb(" + $(this).attr('rel') + ")",
			            opacity: o.secondaryTransparency,
			            "stroke-width": 0
			    	};
			    	R.path($(mask).attr('role')).attr(style);
				} else {
					$(mask).css({"background-color": color}, 1000);
					if($.browser.msie) {
						color = $(this).attr('rel');
						var colors = color.split(",");
						color = colorToHex(colors);
						$(mask).css({'background': 'transparent', 'zoom': 1, 'filter': 'progid:DXImageTransform.Microsoft.gradient(startColorstr=#80' + color + ',endColorstr=#80' + color + ')'});
					}
				}
			});
			
		
			
			//rgb to hex
			function colorToHex(color) {
			    
			    var red = parseInt(color[0]);
			    var green = parseInt(color[1]);
			    var blue = parseInt(color[2]);
			    
			    var rgb = blue | (green << 8) | (red << 16);
			    return rgb.toString(16);
			};
			
		});

    }
})(jQuery);



$(document).ready(function() {	
	$('.product-preview').productColorizer({'secondaryTransparency':1});
	prettyPrint();
});

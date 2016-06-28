(function($) {
    $(document).ready(function() {
        $('.cloud-zoom, .cloud-zoom-gallery').CloudZoom()
    });
    function format(a) {
        for (var i = 1; i < arguments.length; i++) {
            a = a.replace('%' + (i - 1), arguments[i])
        }
        return a
    }
    function CloudZoom(g, i) {
        var j = $('img', g);
        var k;
        var l;
        var m = null;
        var n = null;
        var o = null;
        var p = null;
        var q = null;
        var r = null;
        var s;
        var t = 0;
        var u, ch;
        var v = 0;
        var z = 0;
        var A = 0;
        var B = 0;
        var C = 0;
        var D, my;
        var E = this,
            zw;
        setTimeout(function() {
                if (n === null) {
                    var w = g.width();
                    g.parent().append(format('<div style="width:%0px;position:absolute;top:75%;left:%1px;text-align:center" class="cloud-zoom-loading" >Loading...</div>', w / 3, (w / 2) - (w / 6))).find(':last').css('opacity', 0.5)
                }
            },
            200);
        var F = function() {
            if (r !== null) {
                r.remove();
                r = null
            }
        };
        this.removeBits = function() {
            if (o) {
                o.remove();
                o = null
            }
            if (p) {
                p.remove();
                p = null
            }
            if (q) {
                q.remove();
                q = null
            }
            F();
            $('.cloud-zoom-loading', g.parent()).remove()
        };
        this.destroy = function() {
            g.data('zoom', null);
            if (n) {
                n.unbind();
                n.remove();
                n = null
            }
            if (m) {
                m.remove();
                m = null
            }
            this.removeBits()
        };
        this.fadedOut = function() {
            if (m) {
                m.remove();
                m = null
            }
            this.removeBits()
        };
        this.controlLoop = function() {
            if (o) {
                var x = (D - j.offset().left - (u * 0.5)) >> 0;
                var y = (my - j.offset().top - (ch * 0.5)) >> 0;
                if (x < 0) {
                    x = 0
                } else if (x > (j.outerWidth() - u)) {
                    x = (j.outerWidth() - u)
                }
                if (y < 0) {
                    y = 0
                } else if (y > (j.outerHeight() - ch)) {
                    y = (j.outerHeight() - ch)
                }
                o.css({
                    left: x,
                    top: y
                });
                o.css('background-position', ( - x) + 'px ' + ( - y) + 'px');
                v = (((x) / j.outerWidth()) * s.width) >> 0;
                z = (((y) / j.outerHeight()) * s.height) >> 0;
                B += (v - B) / i.smoothMove;
                A += (z - A) / i.smoothMove;
                m.css('background-position', ( - (B >> 0) + 'px ') + ( - (A >> 0) + 'px'))
            }
            t = setTimeout(function() {
                    E.controlLoop()
                },
                30)
        };
        this.init2 = function(a, b) {
            C++;
            if (b === 1) {
                s = a
            }
            if (C === 2) {
                this.init()
            }
        };
        this.init = function() {
            $('.cloud-zoom-loading', g.parent()).remove();
            n = g.parent().append(format("<div class='mousetrap' style='background-image:url(\".\");z-index:999;position:absolute;width:100%;height:100%;left:%2px;top:%3px;\'></div>", j.outerWidth(), j.outerHeight(), 0, 0)).find(':last');
            n.bind('mousemove', this,
                function(a) {
                    D = a.pageX;
                    my = a.pageY
                });
            n.bind('mouseleave', this,
                function(a) {
                    clearTimeout(t);
                    if (o) {
                        o.fadeOut(299)
                    }
                    if (p) {
                        p.fadeOut(299)
                    }
                    if (q) {
                        q.fadeOut(299)
                    }
                    m.fadeOut(300,
                        function() {
                            E.fadedOut()
                        });
                    return false
                });
            n.bind('mouseenter', this,
                function(a) {
                    D = a.pageX;
                    my = a.pageY;
                    zw = a.data;
                    if (m) {
                        m.stop(true, false);
                        m.remove()
                    }
                    var b = i.adjustX,
                        yPos = i.adjustY;
                    var c = j.outerWidth();
                    var d = j.outerHeight();
                    var w = i.zoomWidth;
                    var h = i.zoomHeight;
                    if (i.zoomWidth == 'auto') {
                        w = c
                    }
                    if (i.zoomHeight == 'auto') {
                        h = d
                    }
                    var e = g.parent();
                    switch (i.position) {
                        case 'top':
                            yPos -= h;
                            break;
                        case 'right':
                            b += c;
                            break;
                        case 'bottom':
                            yPos += d;
                            break;
                        case 'left':
                            b -= w;
                            break;
                        case 'inside':
                            w = c;
                            h = d;
                            break;
                        default:
                            e = $('#' + i.position);
                            if (!e.length) {
                                e = g;
                                b += c;
                                yPos += d
                            } else {
                                w = e.innerWidth();
                                h = e.innerHeight()
                            }
                    }
                    m = e.append(format('<div id="cloud-zoom-big" class="cloud-zoom-big" style="display:none;position:absolute;left:%0px;top:%1px;width:%2px;height:%3px;background-image:url(\'%4\');z-index:99;"></div>', b, yPos, w, h, s.src)).find(':last');
                    if (j.attr('title') && i.showTitle) {
                        m.append(format('<div class="cloud-zoom-title">%0</div>', j.attr('title'))).find(':last').css('opacity', i.titleOpacity)
                    }
                    if ($.browser.msie && $.browser.version < 7) {
                        r = $('<iframe frameborder="0" src="#"></iframe>').css({
                            position: "absolute",
                            left: b,
                            top: yPos,
                            zIndex: 99,
                            width: w,
                            height: h
                        }).insertBefore(m)
                    }
                    m.fadeIn(500);
                    if (o) {
                        o.remove();
                        o = null
                    }
                    u = (j.outerWidth() / s.width) * m.width();
                    ch = (j.outerHeight() / s.height) * m.height();
                    o = g.append(format("<div class = 'cloud-zoom-lens' style='display:none;z-index:98;position:absolute;width:%0px;height:%1px;'></div>", u, ch)).find(':last');
                    n.css('cursor', o.css('cursor'));
                    var f = false;
                    if (i.tint) {
                        o.css('background', 'url("' + j.attr('src') + '")');
                        p = g.append(format('<div style="display:none;position:absolute; left:0px; top:0px; width:%0px; height:%1px; background-color:%2;" />', j.outerWidth(), j.outerHeight(), i.tint)).find(':last');
                        p.css('opacity', i.tintOpacity);
                        f = true;
                        p.fadeIn(500)
                    }
                    if (i.softFocus) {
                        o.css('background', 'url("' + j.attr('src') + '")');
                        q = g.append(format('<div style="position:absolute;display:none;top:2px; left:2px; width:%0px; height:%1px;" />', j.outerWidth() - 2, j.outerHeight() - 2, i.tint)).find(':last');
                        q.css('background', 'url("' + j.attr('src') + '")');
                        q.css('opacity', 0.5);
                        f = true;
                        q.fadeIn(500)
                    }
                    if (!f) {
                        o.css('opacity', i.lensOpacity)
                    }
                    if (i.position !== 'inside') {
                        o.fadeIn(500)
                    }
                    zw.controlLoop();
                    return
                })
        };
        k = new Image();
        $(k).load(function() {
            E.init2(this, 0)
        });
        k.src = j.attr('src');
        l = new Image();
        $(l).load(function() {
            E.init2(this, 1)
        });
        l.src = g.attr('href')
    }
    $.fn.CloudZoom = function(d) {
        try {
            document.execCommand("BackgroundImageCache", false, true)
        } catch(e) {}
        this.each(function() {
            var c, opts;
            eval('var	a = {' + $(this).attr('rel') + '}');
            c = a;
            if ($(this).is('.cloud-zoom')) {
                $(this).css({
                    'position': 'relative',
                    'display': 'block'
                });
                $('img', $(this)).css({
                    'display': 'block'
                });
                if ($(this).parent().attr('id') != 'wrap') {
                    $(this).wrap('<div id="wrap" style="top:0px;z-index:9999;position:relative;text-align:center;width:100%;height:100%"></div>')
                }
                opts = $.extend({},
                    $.fn.CloudZoom.defaults, d);
                opts = $.extend({},
                    opts, c);
                $(this).data('zoom', new CloudZoom($(this), opts))
            } else if ($(this).is('.cloud-zoom-gallery')) {
                opts = $.extend({},
                    c, d);
                $(this).data('relOpts', opts);
                $(this).bind('click', $(this),
                    function(a) {
                        var b = a.data.data('relOpts');
                        $('#' + b.useZoom).data('zoom').destroy();
                        $('#' + b.useZoom).attr('href', a.data.attr('href'));
                        $('#' + b.useZoom + ' img').attr('src', a.data.data('relOpts').smallImage);
                        $('#' + a.data.data('relOpts').useZoom).CloudZoom();
                        return false
                    })
            }
        });
        return this
    };
    $.fn.CloudZoom.defaults = {
        zoomWidth: 'auto',
        zoomHeight: 'auto',
        position: 'right',
        tint: false,
        tintOpacity: 0.5,
        lensOpacity: 0.5,
        softFocus: false,
        smoothMove: 3,
        showTitle: true,
        titleOpacity: 0.5,
        adjustX: 0,
        adjustY: 0
    }
})(jQuery);
//以上为图片展示插件
//运动框架

function getByClass(oParent,sClass){
    var aEle = oParent.getElementsByTagName("*");
    var aResult = [];
    for(var i=0; i<aEle.length; i++){
        if(aEle[i].className==sClass){
            aResult.push(aEle[i]);
        }
    }
    return aResult;
}
function getStyle(obj,name)
{
    if(obj.currentStyle)
    {
        return obj.currentStyle[name];
    }else
    {
        return getComputedStyle(obj,false)[name];
    }
}
function getPos(ev)
{
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
    return {x:ev.clientX+scrollLeft,y:ev.clientY+scrollTop}
}
function startMove(obj,json,fnEnd)
{
    try
    {
        clearInterval(obj.timer);
        obj.timer=setInterval(function()
        {
            var bStop = true; //假设所有的都到了
            for(var attr in json)
            {
                cur = 0;
                if(attr=="opacity")
                {
                    cur=Math.round(parseFloat(getStyle(obj,attr))*100);
                }
                else
                {
                    cur=parseInt(getStyle(obj,attr))
                }
                var speed = (json[attr]-cur)/6;
                speed = speed>0? Math.ceil(speed):Math.floor(speed);
                if(cur!=json[attr])
                    bStop=false;

                if(attr=="opacity")
                {
                    obj.style.filter="alpha(opacity:"+(cur+speed)+")";
                    obj.style.opacity=(cur+speed)/100;
                }
                else
                {
                    obj.style[attr]=cur+speed+"px"
                }
            }
            if(bStop)
            {
                clearInterval(obj.timer);
                if(fnEnd)fnEnd();
            }
        },30);
    }catch(e){};
}
//运动框架结束

//


$(function(){
    //选择小图添加样式
    var goods_index;
    $(".goods_smallimg_list li a").click(function(){
        goods_index=$(this).parent().index();
        $(this).parent().siblings("li").removeClass();
        $(this).parent().addClass("sel_img");
    });
    //商品样式选中
    $(".goods_style dd").click(function(){
        $(this).siblings("dd").removeClass();
        $(this).addClass("sel_goods_style");
    });
    //商品图片弹出层
    $(".goods_info_img").click(function(){
        $("#pop_img").fadeIn(250);
        $(".pop_img_list li").eq(goods_index).find("a").trigger("click");
    });

    var ewidth=(document.documentElement.clientWidth)*0.8*0.5;
    if(ewidth>=500){
        $(".pop_img_box").css("margin-left","-500px");
    }else{
        $(".pop_img_box").css("margin-left",-ewidth);
    };
    window.onresize=function(){
        ewidth=(document.documentElement.clientWidth)*0.8*0.5;
        if(ewidth>=500){
            $(".pop_img_box").css("margin-left","-500px");
        }else{
            $(".pop_img_box").css("margin-left",-ewidth);
        }
    }
    //关闭弹出层
    $(".pop_close").click(function(){
        $("#pop_img").fadeOut(300);
    });
    $(".pop_img_list li a").click(function(){
        $(this).parent("li").siblings("li").removeClass();
        $(this).parent("li").addClass("sel_pop_img");
    });
    //左右选图按钮
    $(".a_pop_left").click(function(){
        if(!$(".pop_img_list li[class='sel_pop_img']").prev().length>0){
            $(".pop_img_list li").last().find("a").trigger("click");
        }else{
            $(".pop_img_list li[class='sel_pop_img']").prev().find("a").trigger("click");
        }
    });
    $(".a_pop_right").click(function(){
        if(!$(".pop_img_list li[class='sel_pop_img']").next().length>0){
            $(".pop_img_list li").first().find("a").trigger("click");
        }else{
        $(".pop_img_list li[class='sel_pop_img']").next().find("a").trigger("click");
        }
    });
    //左侧选择图片栏
    var img_length=$(".goods_smallimg_list li").length;
    var img_num=Math.ceil(img_length/5);
    var numNow=1;
    $(".goods_info_left .a_down").click(function(){
       if(numNow<img_num){
          numNow++;
            $(".goods_smallimg_list").animate({top:"-=330px"},400);
       }
    });
    $(".goods_info_left .a_up").click(function(){
        if(numNow>1){
            numNow--;
            $(".goods_smallimg_list").animate({top:"+=330px"},400);
        }
    });
});










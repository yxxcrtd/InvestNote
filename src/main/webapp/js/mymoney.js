		FastClick.attach(document.body);
			(function(window, $) {
				//tab切换js
				$(".list_head li a").click(function() {
					var obj = $(this).parent();
					if (!obj.hasClass("active")) {
						obj.addClass("active");
						obj.siblings("li").removeClass("active");
						execFun();
					}
				});
				//切换时调用方法
				function execFun() {
					//				alert(1);
				}
				//单个下拉
				$("#moneyTypeSel").click(function() {
					$(document.body).addClass("ovfHiden");
					var aobj=obj = $(this);
					var offset = aobj.offset();
					var defaultVal=$(this).attr("defaultVal");
					var sel = $(".sel");
					sel.eq(0).css({
						top: offset.top-document.body.scrollTop,
						left: offset.left,
						width: $(this).width()
					});
					sel.find(".arrow").css({
						"transform": "rotate(180deg)",
						"-moz-transform": "rotate(180deg)",
						"-webkit-transform": "rotate(180deg)"
					});
					//默认值赋值
					$(".sel_mask li").each(function(){
						if(defaultVal==$(this).attr("data-val")){
							$(this).parent().siblings(".title").children("span").html($(this).html());
						}
					});
					$(".sel_mask").show();
				});
				//li单机事件
				$(".selItem li").click(function() {
					var index = $(this).attr("index");
					var val = $(this).attr("data-val");
					var parent = $(this).parent();
					$(parent.attr("target")).html($(this).html());
					parent.siblings("div").children("span").html($(this).html());
					$(".sel").find(".arrow").css({
						"transform": "rotate(0deg)","-moz-transform":"rotate(0deg)","-webkit-transform":"rotate(0deg)"
					});
					$(".sel_mask").hide();
					liFun.call($(this), val, index);
					$(document.body).removeClass("ovfHiden");
					window.event ? window.event.cancelBubble = true : evt.stopPropagation();
				});
				//li点击时执行逻辑
				function liFun(val) {
					window.location.href = $("#ctx").val() + "/weixin/note/capital?t=" + val;
				}
				//隐藏弹出
				$(".sel_mask").click(function() {
					$(this).find(".arrow").css({
						"transform": "rotate(180deg)",
						"-moz-transform": "rotate(180deg)",
						"-webkit-transform": "rotate(180deg)"
					});
					$(document.body).removeClass("ovfHiden");
					$(this).hide();
				});
			})(window, $);
			$(function(){
				$(".sel_mask li").each(function(){
					if($(this).attr("selected")){
						$($(this).parent().attr("target")).html($(this).html());
					}
				});
			});
			
			
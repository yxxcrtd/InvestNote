FastClick.attach(document.body);
(function(window, $) {
	//tab切换
	$(".starting,.ending").click(function() {
		if (!$(this).hasClass("active")) {
			$(this).siblings().removeClass("active");
			$(this).addClass("active");
			$(this).siblings().each(function() {
				if ($(this).attr("target")) {
					$("." + $(this).attr("target")).hide();
				}
			});
			$("." + $(this).attr("target")).show();
			tabExec.apply($(this));
		}
		setFooter();
	});
	//切换时调用方法
	function tabExec(currobj) {
	}
	//点击条目添加背景色
	$(".list_suce>div").click(function() {
		$(this).siblings("div").css("background-color", "");
		$(this).css("background-color", "#dcdcdc");
		});
})(window, $);
//滚动加载方法
//function scrollLoad(){
//	if($(this).parent().hasClass("starting_cont")){//第一个列表，进行中
//
//	}else{//第二个列表 已结束
//
//	}
//	//追加html元素
////	$(this).siblings(".list_suce").append(str);
//	$("#load").hide();
//}
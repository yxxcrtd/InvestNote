FastClick.attach(document.body);
$("#focus").click(function(){
	var type=0;
    var cur = $(this);
	$.get("/weixin/note/focus?t=" + cur.attr("type") + "&userId=" + cur.attr("targetId"), function(data) {
		if(cur.attr("type") && cur.attr("type")==1){
			cur.attr("type", type=0);
			cur.text("关注");
		}else{
			cur.attr("type", type=1);
			cur.text("取消关注");
		}
		if ("success" != data) {
			dialog("网络异常，请稍后关注！");
		}
    });
});
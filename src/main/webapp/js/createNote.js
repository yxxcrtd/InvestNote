FastClick.attach(document.body);
(function(window, $) {
	getEndDate(2,$("#sp"));
	//下拉切换
	//二级下拉配置
	var objConfig = ['3%', '5%', '8%', '10%', '15%', '20%', '25%', '30%'];
	//显示下拉
	$(".upInfo").click(function() {
		$("body,html").addClass("ovfHiden");
		var obj = $(this).parent().parent().find("a"),
			aobj = $(obj[0]);
		var offset = aobj.offset();
		var sel = $(".sel");
		var index=$(this).attr("index");
		var selobj = $($(".sel")[index]);
		selobj.siblings().removeClass("on");
		selobj.addClass("on").show();
		selobj.find(".arrow").css({
			"transform": "rotate(180deg)",
			"-moz-transform": "rotate(180deg)",
			"-webkit-transform": "rotate(180deg)"
		});
		sel.eq(0).css({
			top: offset.top-document.body.scrollTop,
			left: offset.left,
			width: $(this).width()
		});
		sel.eq(index).children("ul").show();
		sel.eq(index).siblings().children("ul").hide();
		$(".sel_mask").show();
	});
	//单机显示的头部显示内容
	$(".title").click(function(evt) {
		$(this).parent().siblings().find("ul").hide();
		$(this).siblings("ul").show();
		$(this).parent().siblings().removeClass("on");
		$(this).parent().siblings().find(".arrow").css({
			"transform": "rotate(0deg)",
			"-moz-transform": "rotate(0deg)",
			"-webkit-transform": "rotate(0deg)"
		});
		var selobj = $(this).parent();
		if (!selobj.hasClass("on")) {
			selobj.addClass("on");
			selobj.find(".arrow").css({
				"transform": "rotate(180deg)",
				"-moz-transform": "rotate(180deg)",
				"-webkit-transform": "rotate(180deg)"
			});
		}
		window.event ? window.event.cancelBubble = true : evt.stopPropagation()
	});
	//li单机事件
	var liClick = function() {
			var index = $(this).attr("index");
			var val = $(this).attr("data-val");
			var parent = $(this).parent();
			$(parent.attr("target")).html($(this).html());
			var target = parent.attr("target1");
			parent.siblings("div").children("span").html($(this).html());
			$("#"+$(this).parent().attr("targetVal")).val(val);
			parent.hide();
			$(this).parent().siblings().find(".arrow").css({
				"transform": "rotate(0deg)",
				"-moz-transform": "rotate(0deg)",
				"-webkit-transform": "rotate(0deg)"
			});
			var input=$("#note_increase");
			var day=$("#dayNum").val(),max=10*(parseInt($("#dayNum").val())+1);
			var min=0;
			if(day<=5&&day>0)
				min=Math.round(day*1.5);
			else if(day>5 && day<=20)
				min=Math.round(0.5*day+5);
			input.attr("min",min);
			input.attr("max",max);
			input.val("");
			input.prop("placeholder",min+"-"+max+"");
			input.attr("dialog_title","涨幅最小"+min+",最大"+max);
			getEndDate(val,$("#" + parent.attr("updateId")));
			$(".sel_mask").hide();
			$("body,html").removeClass("ovfHiden");
			window.event ? window.event.cancelBubble = true : evt.stopPropagation();
		}
		//绑定事件
	$(".daySel,.upSel").find("li").click(liClick);
	//隐藏弹出
	$(".sel_mask").click(function() {
		$(this).find(".arrow").css({
			"transform": "rotate(0deg)",
			"-moz-transform": "rotate(0deg)",
			"-webkit-transform": "rotate(0deg)"
		});
		$("body,html").removeClass("ovfHiden");
		$(this).hide();
	});
	//获得结束日期相关数据
	function getEndDate(val, id) {
       $.get("/weixin/note/getBetweenDate?d="+val, function (data) {
       		var dateArr=data.split(",");
            $(id).html("起算日"+dateArr[0].replace(/-/g,".")+"，截止"+dateArr[1].replace(/-/g,".").substring(5)+"收盘");
    	});
	}
	//验证
	document.getElementById("note_stock_code").addEventListener("input",note_stock_code);
	document.getElementById("note_open_money").addEventListener("input",note_open_money);
	document.getElementById("note_remark").addEventListener("input",note_remark);
	document.getElementById("note_title").addEventListener("input",note_remark);
	document.getElementById("note_increase").addEventListener("input",note_remark);

	//form提交
	$(".btnable a").bind("click",function() {
        var temp=0,index=0,flag=0;
        var fun={note_stock_code:"note_stock_code",note_open_money:"note_remark1",note_remark:"note_remark1",note_title:"note_remark1",note_increase:"note_remark1"};
		$("#note_stock_code,#note_open_money,#note_remark,#note_title,#note_increase").each(function(){
			if($(this).attr("id")=="note_open_money" && !$("#note_open_money").val()){
				$(this).val("0");
			}
			index=index+1;
			if(flag)
				return;
			window[fun[$(this).attr("id")]].apply($(this));
			if($(this).attr("flag")==1)
				temp=temp+1;
			else
				flag=1;
		});
		if (temp==index){
			$(this).addClass("btnDisdefalut");
			$(this).unbind("click");
			$(".outMoney").submit();
		}	
	});
	//付费切换
//	$("[name='note_type']").click(function(){
//		if($(this).val()==1){
//			$("[target='"+$(this).attr("name")+"']").show();
//			$("#footer").removeAttr("style");
//			setFooter();
//		}else{
//			$("[target='"+$(this).attr("name")+"']").hide();
//			$("#footer").removeAttr("style");
//			setFooter();
//		}
//	});
})(window, $);
$(function(){
	var html=$(".daySel [data-val='"+$("#dayNum").val()+"']").html();
	$("[targetVal='dayNum']").siblings("div").children("span").html(html);
	$("[targetVal='zf']").siblings("div").children("span").html($("#zf").val()?$("#zf").val()+"%":"预期涨幅");
	$(".day").html(html);
	$(".pro").html($("#zf").val()?$("#zf").val()+"%":"预期涨幅");
});
function note_stock_code(){
	if (!/^[0-9]+$/.test($(this).val()) && $(this).val().trim()!="")  {
		$(this).val("");
	}else if(/^[0,3,6][0-9]*/.test($(this).val().trim())){
		$(this).css("border", "none");
		$(this).attr("flag","1");
		return;
	}
	$(this).css("border", "1px solid #f15438");
	$(this).attr("flag","0");
}
function note_open_money(){
	var val=$(this).val().trim();
	if (val!="" && !(/^[1-9]\d+$/.test(val)||/^[0-9]$/.test(val))) {
		$(this).val(1);
		$(this).css("border", "none");
		$(this).attr("flag","1");
	} else {
		if (getBool.apply(this)) {
			$(this).css("border", "1px solid #f15438");
			$(this).attr("flag","0");
		} else {
			$(this).css("border", "none");
			$(this).attr("flag","1");
		}
	}
}
function getBool(){
	var val1=$(this).val();
//	val = $(this).val().replace(/[\u4e00-\u9fa5]/g, "aa"),
	var obj=$(this);
	if(!$(this).attr("max") && $(this).parent().attr("max"))
		obj=$(this).parent();
	if($(this).attr("datatype")=="number" || $(this).parent().attr("datatype")=="number"){
		if((!/^\d+$/.test(val1.trim()) && val1.trim()!="") && $(this).attr("id")=="note_increase"){
			$(this).val(val1=isNaN(parseInt(val1))?5:parseInt(val1));
		}
		return val1 < parseInt(obj.attr("min")) || val1 > parseInt(obj.attr("max"));
	}else
		return val1.length < obj.attr("min") || val1.length > obj.attr("max");
}
function note_remark(){
	if (getBool.apply(this)) {
		$(this).css("border", "1px solid #f15438");
		$(this).attr("flag","0");
	} else {
		$(this).css("border", "none");
		$(this).attr("flag","1");
	}
}
function note_remark1(){
	if (getBool.apply(this)) {
		$(this).attr("flag",0);
		$(this).css("border", "1px solid #f15438");
		dialog(($(this).attr("dialog_title")?$(this).attr("dialog_title"):$(this).parent().attr("dialog_title"))||"");
	}else{
		$(this).attr("flag",1);
	}
}
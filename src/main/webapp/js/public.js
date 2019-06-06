//百度可以检测所有页面
var _hmt = _hmt || [];
var obj={srollId:""};
(function() {
  var hm = document.createElement("script");
  //公司级别的推广账号
  //hm.src = "//hm.baidu.com/hm.js?88b67d395e7ccebf6c03f1f4aca8ae34";
  //财猫网的 账号
  hm.src = "//hm.baidu.com/hm.js?f6ef3ae4073dfd1c2a8e2d569660282b";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();

$(function(){
	if($(".dialog_footer")&&$(".dialog_footer").length>0){
		$(".dialog_footer a,.dialog").click(function(){
			$(".dialog").hide();
		});
		$(".dialog_cont").click(function(e){
			 e.stopPropagation();
		});
		$("#payBtn").click(function(){
			$(".dialog").show();
		});
	}

	//点击隐藏二维码弹出框
	$(".tcode_btn").click(function(){
		$(this).parents(".tcode_mask").hide();
	});

	//滚动加载
	if($(".loading").length>0)
		$(document).scroll(function(){
			if(document.body.scrollHeight-document.body.scrollTop<=document.body.clientHeight+100){
					$(".loading").show();
					typeof scrollLoad!="undefined"?scrollLoad.apply($(".loading").eq(0),$("#"+(typeof srollId!="undefined"?srollId:$("#default_id").val()))):"";
			}
		});
	//点赞功能
	if($(".defalut_good") && $(".defalut_good").length){
		$("#dz a").click(function(){
			var _this=$(this).children("i");
			if($(this).parent().attr("isBuy")==-1){
				return;
			}	
			var className=$(_this).attr("targetid"),type=0;
			var spanObj=$("#"+className);
			var sibClassName=className=="good"?"nogood":"good";
			if($(_this).hasClass(className)){
				$(_this).removeClass(className);
				//spanObj.html(parseInt(spanObj.html())-1);
				type=0;
			}else{
				var sibobj=$(this).siblings("a").children("i");
				if(sibobj.hasClass(sibClassName)){
					  sibobj.removeClass(sibClassName);
					  var spanObj1=$("#"+sibobj.attr("targetid"));
						//spanObj1.html(parseInt(spanObj1.html())-1);
				}
				$(_this).addClass(className);
			  //spanObj.html(parseInt(spanObj.html())+1);
			  type=className=="good"?1:2;
			}
			execGood?execGood.call(_this,type):"";
		});
	}
	//切换，如关注页面
	if($("#switch") && $("#switch").length){
			obj.srollId=$("#switch").children(".on").attr("target-id");
			$("#switch").children("span").click(function(){
				if(!$(this).hasClass("on")){
					var targetId=$(this).attr("target-id");
					$(this).siblings("span").removeClass("on");
					$(this).addClass("on");
					$("#"+targetId).show();
					obj.srollId=targetId;
					$("#"+$(this).attr("hide-id")).hide();
				}
			});
	}
	//添加点击效果
	if($(".clickHove") && $(".clickHove").length)
		$(".clickHove").click(function(){
			var _this=$(this);
			 _this.addClass("active");
			 setTimeout(function(){
			 		_this.removeClass("active");
			 },500);
		});
});
//弹出框
	function dialog(content,time,fun){
		if($(".dialog_tip_cont") && $(".dialog_tip_cont").length>0)
			$(".dialog_tip_cont").html(content).show();
		else{
			$(document.body).append('<div class="dialog_tip_cont">'+content+'</div>');
			$(".dialog_tip_cont").show();
		}
		setTimeout(function(){
			$(".dialog_tip_cont").hide();
			fun?fun():"";
		},time?time:3000);
	}
	//date添加格式化方法
	Date.prototype.format=function(formatDate,formatString){
		formatString=formatString||"yyyy-mm-dd hh:mm:ss";
		var date=new Date(formatDate);
		var dateArr=formatString.split(" ");
		var dateArrInfo=[];
		dateArrInfo.push(date.getFullYear());
		dateArrInfo.push(dateArr[0].substring(4,5));
		dateArrInfo.push(date.getMonth()+1);
		dateArrInfo.push(dateArr[0].substring(7,8));
	    dateArrInfo.push(date.getDate()+" ");
	    var hour=date.getHours();
		dateArrInfo.push(hour>9?hour:"0"+hour);
		dateArrInfo.push(dateArr[1].substring(2,3));
		var min=date.getMinutes();
		dateArrInfo.push(min>9?min:"0"+min);
		dateArrInfo.push(dateArr[1].substring(5,6));
		var sec=date.getSeconds();
		dateArrInfo.push(sec>9?sec:"0"+sec);
		return dateArrInfo.join("");
	}
	//底部弹出按钮
	function tip_dialog(fun,arrBtn){
		  var html=['<div class="sel_mask">'];
			html.push('<ul class="selBtn">');
			if(!arrBtn || typeof arrBtn=="number"){
					html.push('<li><a href="javascript:void(0);">确定</a></li>');
					if(typeof arrBtn=="undefined" || arrBtn!=1){
							html.push('<li><a href="javascript:void(0);">取消</a></li>');
					}
			}else{
				for(var i=arrBtn.length;i>0;i--){
						html.push('<li><a href="'+arrBtn[i][0]?arrBtn[i][0]:"javascript:void(0);"+'">'+arrBtn[i]+'</a></li>');
				}
			}
			html.push('</ul></div>');
			$(document.body).append(html.join(""));
			setTimeout(function(){
				 $(".selBtn").addClass("selBtnActive");
			});
			$(".selBtn li").click(function(e){
					$(this).children("a").addClass("colorf15438");
					var index=$(this).index();
					if((!arrBtn || typeof arrBtn=="number") && index==0){
						typeof fun=="function"?fun.call($(this),index):"";
					}else if(typeof arrBtn=="object" && arrBtn.length){
						typeof fun=="function"?fun.call($(this),index):"";
					}
			});
			$(".sel_mask").click(function(){
					$(".sel_mask").remove();
			});
	}

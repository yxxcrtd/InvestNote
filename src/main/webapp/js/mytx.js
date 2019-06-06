	FastClick.attach(document.body);
			(function(window,$){
				//按钮置为可用状态
				var btnFun=function(){
					var btn=$("#"+$(this).attr("target")).find("a");
				 	if($(this).val()&&$(this).val().trim()!=""&&$(this).val().trim()>0){
				 		btn.addClass("btnable");
				 		btn.attr("flag","1");
				 		$(this).siblings("i").show();
				 	}else{
				 		btn.removeClass("btnable");
				 		btn.removeAttr("flag");
				 		$(this).siblings("i").hide();
				 	}
				}
				document.getElementById("money").addEventListener("input",btnFun,false);
				document.getElementById("pass").addEventListener("input",btnFun,false);
				 //按钮可用以后处理逻辑
				 $(".btnDis a").bind("click",function(){
				 	if(!$(this).attr("flag"))return;
				 	if($(this).attr("data-btn")==1){//确认转出
				 		$(".mask").show();
				 		setTimeout(function(){
				 			$(".mask").children(".inputPass").css({"transform":"translate3d(0,0,0)","-webkit-transform":"translate3d(0,0,0)","-moz-transform":"translate3d(0,0,0)","-o-transform":"translate3d(0,0,0)"});
				 		},0);
				 	}else if($(this).attr("data-btn")==2){//确认
				 		
				 	}
		 		});
		 		/*实现动画效果*/
		 		var flag=0;
		 		$(".mask").click(function(){
					$(".mask").children(".inputPass").css({"transform":"translate3d(0,100%,0)","-webkit-transform":"translate3d(0,100%,0)","-moz-transform":"translate3d(0,100%,0)","-o-transform":"translate3d(0,100%,0)"});
		 			flag=1;
		 		});
		 		$(".mask").children(".inputPass").click(function(evt){
		 			window.event? window.event.cancelBubble = true : evt.stopPropagation();
		 		});
		 		//动画事件监听
		 		$(".mask").children(".inputPass")[0].addEventListener('webkitTransitionEnd', function(){
		 			if(flag)
		 				$(".mask").hide();
		 			flag=0;
		 		}, false);
		 		$(".clearCont").click(function(){
		 			$(this).siblings("input").val('');
		 			$(this).hide();
		 		});
		 		//获取验证码执行
		 		var getAuthCode=function(){
		 			if(!/^1[3|4|5|8][0-9]\d{4,8}$/.test($(".input_auth").children("input").val())){
		 				window.dialog("请输入正确的手机号");
		 				return;
		 			}
		 			$(this).addClass("disAuthCode");
		 			var _this=$(this),time=$(this).attr("time")-1;
		 			_this.children("span").html(time+1);
		 			getAuthCodeFun.apply(_this);
		 			var id=setInterval(function(){
		 				_this.children("span").html(time);
		 				if(time==0){
		 					_this.children("span").html("获取验证码");
		 					_this.removeClass("disAuthCode");
		 					clearInterval(id);
		 				}	
		 				time--;
		 			},1000);
		 		}
		 		//点击验证码的执行逻辑
		 		function getAuthCodeFun(){}
		 		$("#getAuthCode").click(getAuthCode);
		 		window.dialog("123",100);
			})(window,$); 
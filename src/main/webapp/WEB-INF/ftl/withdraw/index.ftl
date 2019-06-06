<!DOCTYPE html>
<html>
<head>
<#include "Header.ftl"/>
    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/mytx.css?version=${version}" />
</head>

<body>
<span class="mgup"></span>

<form action="/weixin/note/withdraw/index" method="post" id="formWithdraw">
    <div class="card_infoShow clearfix">
        <img src="${staticUrl}${request.contextPath}/weixin/note/img/bank_logo/${bank.bank_code}.jpg" alt="银行信息"/>
        <div class="bank_cont">
            <p>${bank.bank_name}</p>
            <p>${bank.bank_user_code}（储蓄卡）</p>
            <input type="hidden" name="withdraw_bank_id" value="${bank.bank_id}" />
        </div>
    </div>
    <!--文本框start-->
    <div class="outMoney">
        <label>转出金额</label>
        <div class="txt">
            <input type="text" placeholder="可转出:${user.user_available_money}" id="money" name="withdraw_money" target="btnOut" />
            <i class="clearCont"></i>
        </div>
    </div>

    <!--end-->
    <p class="tip">手续费0.00</p>

    <div class="txt_list">
        <div class="input_txt input_auth clearfix">
            <label>+86<i></i></label>
            <input type="text" name="user_phone" value="${user.user_phone}" readonly/>
        </div>
        <div class="input_txt input_authVal clearfix">
            <input type="text" id="authCode" name="smsCode" value=""/>
            <label id="getAuthCode" time="60" today="${todayWithdraw}"><i></i><span>获取验证码</span></label>
        </div>
    </div>

    <p class="btnDis mt24" id="btnOut"><a class="btn btn1 <#if todayWithdraw == true>btnDisdefalut</#if>" data-btn="1" <#if todayWithdraw == false>onclick="$('#formWithdraw').submit();"</#if> >确认转出</a></p>
    <#if todayWithdraw == true>
        <p class="rule_tip" style="color: #FF0000;">每天可提现一次,T+1到账</p>
    <#else>
        <p class="rule_tip">每天可提现一次,T+1到账</p>
    </#if>


</form>
<script>
    FastClick.attach(document.body);
    (function(){
        //按钮置为可用状态
        var btnFun=function(){
            var btn=$("#"+$(this).attr("target")).find("a");
            if($(this).val()&&$(this).val().trim()!=""){
                btn.addClass("btnable");
                btn.attr("flag","1");
                $(this).siblings("i").show();
            }else{
                btn.removeClass("btnable");
                btn.removeAttr("flag");
                $(this).siblings("i").hide();
            }
        };
        document.getElementById("money").addEventListener("input",btnFun,false);
//        //按钮可用以后处理逻辑
//        $(".btnDis a").bind("click",function(){
//            if(!$(this).attr("flag"))return;
//            if($(this).attr("data-btn")==1){//确认转出
//                $(".mask").show();
//                setTimeout(function(){
//                    $(".mask").children(".inputPass").css({"transform":"translate3d(0,0,0)","-webkit-transform":"translate3d(0,0,0)","-moz-transform":"translate3d(0,0,0)","-o-transform":"translate3d(0,0,0)"});
//                },0);
//            }else if($(this).attr("data-btn")==2){//确认
//
//            }
//        });
        /*实现动画效果*/
        var flag=0;

        $(".clearCont").click(function(){
            $(this).siblings("input").val('');
            $(this).hide();
        });
        //获取验证码执行
        var getAuthCode=function(){
            if ($(this).attr("today") == "true") {
                dialog("每天仅可提现一次");
                return;
            }

            $(this).addClass("disAuthCode");
            var _this=$(this),time=$(this).attr("time")-1;
            _this.html(time+1);
            getAuthCodeFun.apply(_this);
            var id=setInterval(function(){
                _this.html(time);
                if(time==0){
                    _this.html("获取验证码");
                    _this.removeClass("disAuthCode");
                    clearInterval(id);
                }
                time--;
            },1000);
        }
        //点击验证码的执行逻辑
        function getAuthCodeFun(){
            $.get("/weixin/note/withdraw/send_sms",function(){});
        }
        $("#getAuthCode").click(getAuthCode);
    })();
//    $.get("http://172.32.3.133/weixin/note/getStock",{code:"123456"},function(){
//        alert(1);
//    });
</script>

<#include "Footer.ftl" />
</body>
</html>
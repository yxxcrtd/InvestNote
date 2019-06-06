<!DOCTYPE html>
<html>
<head>
    <#include "Header.ftl"/>
    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/pay.css?version=${version}" />
</head>

    <body>
        <div class="page-cont">
            <div class="payInfo">
                <h3>账户余额(${user.user_available_money})</h3>
                <p>待支付<em>${money}</em>元</p>
            </div>
            <#if yuepay == true>
                <p class="btnable"><a class="btn btn1" href="/weixin/note/pay/yuePay?noteId=${note.note_id}&optType=${optType}">余额支付</a></p>
            <#else/>
                <p class="btnable"><a class="btn btn1" href="/weixin/note/pay/wxPay?noteId=${note.note_id}&optType=${optType}">微信支付</a></p>
                <br />
                <p class="btnable"><a class="btn btn1" href="/weixin/note/pay/h5Pay?noteId=${note.note_id}&optType=${optType}">银行卡支付</a></p>
            </#if>

            <#--<p class="tip_footer">每日首次提现免手续费 <em>详细规则</em></p>-->
        </div>

        <#include "Footer.ftl" />
    </body>
</html>

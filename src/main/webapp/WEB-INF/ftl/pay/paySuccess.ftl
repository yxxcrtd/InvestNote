<!DOCTYPE html>
<html>
    <head>
        <#include "Header.ftl" />
        <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/createNoteSuccess.css?version=${version}" />
    </head>

    <body>
    <div class="noteSuccess">
        <#if verifiRes == true && payResult == "1">
            <p>笔记生成</p>
            <p>转发好友或微信群，您的好友可付费阅读</p>
            <a href="/weixin/note/reader/view/${note.note_id}">
                <div class="getInfo clearfix">
                    <img src="${user.user_header_img?substring(0, user.user_header_img?last_index_of("/0")) + "/96"}" alt="头像"/>
                    <div>${user.user_nickname}写了一篇投资笔记，股票代码${note.note_stock_code}，${note.note_target_day}日内目标收益${note.note_increase}%</div>
                </div>
            </a>
            <div class="moreBtn">
                <a id="share" href="javascript:void(0);">分享好友</a>
                <a href="/weixin/note/reader/view/${note.note_id}">预览</a>
            </div>
        <#else>
            <p>支付失败，请稍后再试</p>
        </#if>
    </div>
    <div class="share_tip">
        <i></i>
    </div>

    <#include "Footer.ftl" />

    <script src="${staticUrl}${request.contextPath}/weixin/note/js/createNoteSuccess.js"></script>
    </body>
</html>
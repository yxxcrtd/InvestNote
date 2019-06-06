<!doctype html>
<html lang="en">
    <head>
        <#include "Header.ftl" />
        <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/index.css?version=${version}" />
    </head>

    <body>
        <div class="page-content">
            <img src="${staticUrl}${request.contextPath}/weixin/note/img/index/note.png" alt="投资笔记">
            <h3>欢迎来到投资笔记</h3>
            <p>已诞生<em>${noteCount}</em>条投资笔记   已产生<em>${readCount}</em>次阅读</p>
            <#--<a href="javascript:void(0);">查看规则</a>-->
            <p><a class="btn" href="${request.contextPath}/weixin/note/save">创建</a></p>
            <p>创建笔记，分享到微信群，好友可付费阅读</p>
        </div>
        <script>
            FastClick.attach(document.body);
        </script>
        <#include "Footer.ftl" />
    </body>
</html>

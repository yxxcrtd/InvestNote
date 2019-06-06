<nav class="navbar navbar-default navbar-fixed-top">
    <div class="navbar-header">
        <img src="${staticUrl}${request.contextPath}/weixin/note/admin/img/login_logo.png" class="header_logo">
        <a class="navbar-brand" href="${request.contextPath}/weixin/note/weixin/note/admin/welcome">财猫投资笔记后台</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <#--<c:forEach items="${ruleMap['nav_top']}" var="navRule">-->
                <#--<li id="top_nav_${navRule.getValue().getId()}"<c:if test="${ruleMap['parentIds'].contains(navRule.getValue().getId())}"> class="active"</c:if>><a href="javascript:left_show(${navRule.getValue().getId()});">${navRule.getValue().getRuleName()}</a></li>-->
            <#--</c:forEach>-->
        </ul>
        <p class="navbar-text navbar-right user_right">
            Hi! 投资笔记
            <#--<a href="/admin/userPassword">修改密码</a>-->
            <a href="/weixin/note/admin/logout">退出</a>
        </p>
    </div>
</nav>
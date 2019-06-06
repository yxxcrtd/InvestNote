<!DOCTYPE html>
<html>
<head>
    <title>财猫投资笔记管理系统</title>
    <#include "/admin/include/tpl_menu_head.ftl" />
</head>
<body class="login-body">
<div class="container-login">
    <div class="login_logo">
        <img src="${staticUrl}/weixin/note/admin/img/login_logo.png" width="200">
    </div>
    <form class="form-signin" action="/weixin/note/admin/login" method="post">
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
            <input type="text" class="form-control" placeholder="用户名" name="username" autocomplete="off" autofocus="autofocus">
        </div>
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
            <input type="password" class="form-control" placeholder="密码" name="password" autocomplete="off">
        </div>
        <p>${errorMsg}</p>
        <button class="btn btn-lg btn-success btn-block" type="submit" style="margin-top: 10px;">登录</button>
    </form>
</div>
</body>
</html>
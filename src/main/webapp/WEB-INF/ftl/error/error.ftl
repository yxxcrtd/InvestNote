<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/error.css" />
    <script src="${staticUrl}${request.contextPath}/weixin/note/js/flexible.js"></script>
    <script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/zepto.min.js" ></script>
    <script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/fastclick.js" ></script>
    <title>投资笔记</title>
</head>
<body>
<div class="page_cont">
    <img src="${staticUrl}${request.contextPath}/weixin/note/img/systemError.png" alt="系统错误"/>
    <p>系统出错啦，正在为您诊断</p>
    <#--<p>${ex}</p>-->
    <div class="btnable"><a class="btn btn1" href="javascript:history.back();">返回</a></div>
</div>

<#include "Footer.ftl" />
</body>
</html>
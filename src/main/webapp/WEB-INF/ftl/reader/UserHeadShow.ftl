<!DOCTYPE html>
<html>
	<head>
	    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
	    <#include "Header.ftl"/>
	    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/userHeadShow.css" />
	    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/js/flexible.js" />
	</head>

	<body>
		<dl>
			<dt>${reader_count}人阅读</dt>
			<dd>
			<div class="textalign plpd">
	            <ul class="user-list clearfix">
		            <#list readerList as l>
					     <li><img src="${l.user.user_header_img?substring(0, l.user.user_header_img?last_index_of("/0")) + "/46"}"/></li>
		            </#list>
	            </ul>
            </div>
			</dd>
		</dl>
		<p class="btnable"><a class="btn" href="javascript:history.back();">返回</a></p>
		
		<#include "Footer.ftl" />
	</body>
</html>
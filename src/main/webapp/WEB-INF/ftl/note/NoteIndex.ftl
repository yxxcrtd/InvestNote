<!DOCTYPE html>
<html>
<head>
    <#include "Header.ftl"/>
    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/mynote.css?version=${version}" />
</head>

<body>
<section>
		<header class="head">
			<div class="head_content clearfix">
				<img src="${user.user_header_img?substring(0, user.user_header_img?last_index_of("/0")) + "/96"}" alt="头像"/>
				<dl class="headInfo">
					<dt>${user.user_nickname}</dt>
					<dd><a href="javascript:void(0);">关注${focus}</a><a href="javascript:void(0);">粉丝${fans}</a></dd>
				</dl>
				<#if (userId != uId)><a id="focus" type="1" targetId="${userId}" href="javascript:;" class="atten_btn" id="atten_btn">关注</a></#if>
			</div>
			<div class="success_detail">
				<span>${user.user_note_count}篇<br/><em>历史笔记</em></span>
				<span>${user.user_success}%<br/><em>成功率</em></span>
				<span>${user.user_yield}%<br/><em>平均收益</em></span>
			</div>
		</header>
    
    <div class="page_content">
        <div class="list clearborderT">
            <a href="/weixin/note/capital/index">
                <div class="item-left">
                    <i class="icon1"></i>
                    <span>我的资金</span>
                </div>
                <div class="item-right">
                    <span>${user.user_available_money}元</span>
                    <i class="icon4"></i>
                </div>
            </a>
        </div>
        <div class="list mt24">
            <a href="/weixin/note/reader/my_read">
                <div class="item-left">
                    <i class="icon2"></i>
                    <span>我的阅读</span>
                </div>
                <div class="item-right">
                    <span></span>
                    <i class="icon4"></i>
                </div>
            </a>
            <a href="/weixin/note/my_note">
                <div class="item-left">
                    <i class="icon3"></i>
                    <span>我的创作</span>
                </div>
                <div class="item-right">
                    <span></span>
                    <i class="icon4"></i>
                </div>
            </a>
        </div>
    </div>
    <#include "Footer.ftl"/>
</section>
<script src="${staticUrl}${request.contextPath}/weixin/note/js/mynote.js"></script>


</body>
</html>

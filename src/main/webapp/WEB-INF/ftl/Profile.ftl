<header class="head">
	<div class="head_content clearfix">
		<a href="/weixin/note/author?userId=${authorUser.user_id}">
			<img src="${authorUser.user_header_img?substring(0, authorUser.user_header_img?last_index_of("/0")) + "/96"}" alt="头像"/>
		</a>
		<dl class="headInfo">
			<dt>${authorUser.user_nickname}</dt>
			<dd><a href="javascript:void(0);">关注${focus}</a><a href="javascript:void(0);">粉丝${fans}</a></dd>
		</dl>
		
		<#if (userId != uId)><a id="focus" type="<#if isFocus>1<#else>0</#if>" targetId="${userId}" href="javascript:;" class="atten_btn"><#if isFocus>取消关注<#else>关注</#if></a></#if>
	</div>
	<div class="success_detail">
		<span>${authorUser.user_note_count}篇<br/><em>历史笔记</em></span>
		<span>${authorUser.user_success}%<br/><em>成功率</em></span>
		<span>${authorUser.user_yield}%<br/><em>平均收益</em></span>
	</div>
</header>
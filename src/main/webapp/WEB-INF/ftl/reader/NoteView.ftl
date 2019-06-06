<!DOCTYPE html>
<html>
<head>
<#include "Header.ftl"/>
    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/homePage.css?version=${version}" />
</head>
<body>
<section>
    <header class="header_cont">
        <div class="head_content clearfix" onclick="window.location.href='/weixin/note/author?userId=${noteUser.user_id}'">
            <img src="${noteUser.user_header_img?substring(0, noteUser.user_header_img?last_index_of("/0")) + "/96"}" alt="头像"/>
            <dl>
                <dt>${noteUser.user_nickname}</dt>
                <dd><span>历史笔记${noteUser.user_note_count}篇</span><span>成功率${noteUser.user_success}%</span><span>平均收益${noteUser.user_yield}%</span></dd>
            </dl>
        </div>
    </header>
    <div class="page_content">
        <!--股票代码，目标涨幅 实现时间部分-->
        <!--start-->
        <div class="thign-Info">
					<span>
						${note.note_stock_code}<br/>
						<strong>股票代码</strong>
					</span>
					<span>
						${note.note_increase}%<br />
						<strong>目标涨幅<em id="payBtn" class="why"></em></strong>
					</span>
					<span>
						${note.note_target_day}日<br/>
						<strong>实现时间</strong>
					</span>
        </div>
        <!--end-->
        <div class="thign-Invest">
            <p>
						<span class="thign-InvestIntro">
							<span><#if note.note_title?? && 0 < note.note_title?length>${note.note_title}<#else>投资逻辑</#if>&nbsp;&nbsp;${(note.note_remark?length)}字</span><br />
							<span>
                            <#if note.note_status == 0>
								<#if note.note_start_time?string("HH:mm:ss") lt "12:55:00"?date("HH:mm:ss")>
									 ${note.note_start_time?string("MM-dd")}&nbsp;&nbsp;开盘起算
								<#else>
									${note.note_start_time?string("MM-dd")}&nbsp;&nbsp;下午开盘起算
								</#if>
                            <#elseif note.note_status == 1>
                    			<#if note.note_start_time?string("HH:mm:ss") lt "12:55:00"?date("HH:mm:ss")>
									 ${note.note_start_time?string("MM-dd")}&nbsp;&nbsp;开盘起算
								<#else>
									${note.note_start_time?string("MM-dd")}&nbsp;&nbsp;下午开盘起算
								</#if>
                            <#else>
                                已结束
                            </#if>
                            </span>
						</span>
            </p>
        </div>
        <p class="readNum">已有<em>${readerList?size}</em>人阅读</p>

        <#--进行中笔记不能购买-->
        <#if (1 == note.note_status)>
            <p class="mt17 textcenter mb16"><a class="btn btnDisdefalut" href="javascript:void(0);">已停售</a></p>
        <#else>
            <p class="mt17 textcenter mb16">
            <#--不是待开盘，不能点击购买，如果是作者自己是可以点击的-->
                <a class="btn <#if note.note_user_id != user.user_id && note.note_status != 0>btnDisdefalut</#if>" <#if note.note_status == 0>href="/weixin/note/pay/pay?noteId=${note.note_id}&optType=2" <#else>href="javascript:void(0);"</#if>>
                    阅读费 <font class="fontSize">¥</font>&nbsp;${note.note_open_money}
                </a>
            </p>
        </#if>

        <p class="mone-tip textcenter"><img src="${staticUrl}${request.contextPath}/weixin/note/img/homePage/tip.png"/>若不达目标，阅读费退回，作者额外发${note.note_packet_money}元红包</p>
        
        <p class="textcenter mone-tip1"><#if (note.note_open_money > 0)>如：${(readerList?size + 1)}人阅读，每人收回 ${note.note_open_money?string('0.00')} + ${(note.note_packet_money / (readerList?size + 1))?string('0.00')} = ${(note.note_open_money + (note.note_packet_money / (readerList?size + 1)))?string('0.00')}元 </#if></p>
	   <div class="textalign">
	   		<!--赞和踩view-->
	        <div class="readPeopleInfo textalign" ><a><i class="showLike"></i><span class="readPeopleInfo1">${paise}</span></a><a><i class="showLike transform180deg"></i><span class="readPeopleInfo1">${step}</span></a></div>
	        <ul class="user-list clearfix">
	            <#list readerList as l>
	                <li><img src="${l.user.user_header_img?substring(0, l.user.user_header_img?last_index_of("/0")) + "/46"}"/></li>
	            </#list>
	        </ul>
	    </div>
    </div>
</section>


        <#-- 弹出提示框 -->
		<#include "Dialog.ftl" />
		
<script src="${staticUrl}${request.contextPath}/weixin/note/js/homePage.js"></script>

<#include "Footer.ftl" />
</body>
</html>

<!DOCTYPE html>
<html>
	<head>
	    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
	    <#include "Header.ftl"/>
	    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/noteDetail.css?version=${version}" />
	</head>

	<body>
		<section  class="footAdap">
		    <header class="header_cont">
		        <div class="head_content clearfix" onclick="window.location.href='/weixin/note/author?userId=${noteUser.user_id}'">
		            <img src="${noteUser.user_header_img?substring(0, noteUser.user_header_img?last_index_of("/0")) + "/96"}" alt="头像"/>
		            <dl>
		                <dt>${noteUser.user_nickname}</dt>
		                <dd><span>历史笔记${noteUser.user_note_count}篇</span><span>成功率${noteUser.user_success}%</span><span>平均收益${noteUser.user_yield}%</span></dd>
		            </dl>
		        </div>
		    </header>
		
		    <#-- 添加defalut下面部分将使用默认页，无图章页面 -->
		    <div class="detail_list clearfix default">
		        <dl>
		            <dt>股票</dt>
		            <dd><strong class="colorf15438">${note.note_stock_name}</strong><!--<b>科大讯飞</b>-->&nbsp;&nbsp;<em class="gpcode">${note.note_stock_code}</em></dd>
		        </dl>
		        <dl>
		            <dt>逻辑</dt>
		            <dd><#if note.note_title?? && 0 < note.note_title?length>${(note.note_title)}。</#if>${note.note_remark}</dd>
		        </dl>
		        <dl>
		            <dt>目标</dt>
		            <dd>${note.note_target_day}日涨${note.note_increase}%<em class="color2e54a5 why" id="payBtn"></em><!--<b>涨35%[?]</b>--></dd>
		        </dl>
		        <dl>
		            <dt>起始</dt>
		            <dd>
		            ${note.note_start_time?string("MM-dd HH:mm")}（起算价&nbsp;<em class="color2e54a5"><#if (0 == note.note_status)>待开盘<#else>${startPrice?string('0.00')}</#if></em>）
		            </dd>
		        </dl>
		        <dl>
		            <dt>截止</dt>
		            <dd class="pl">
		                ${note.note_end_time?string("MM-dd HH:mm")}（目标价&nbsp;<em class="color2e54a5"><#if (0 == note.note_status)>待开盘<#else>${targetPrice?string('0.00')}</#if></em><!--<b>待开盘</b>-->）
		                <#if note.note_status == 2>
		                    <#if note.note_target_status == 0><div class="chapter_success chapter_fail"><#-- 失败图章 -->
		                    <#elseif note.note_target_status == 1><div class="chapter_success"></div><#-- 成功图章 -->
		                    <#elseif note.note_target_status == 2><div class="chapter_success chapter_disable"></div><#-- 无效图章 -->
		                    </#if>
		                </#if>
		            </dd>
		        </dl>
		    </div>
		
	        <div class="readInfo">
	            <#if note.note_status == 2>
	                <#if note.note_target_status == 0>
	                    <#-- 失败页面 -->
	                    <p class="mt45 mb122">阅读费${note.note_open_money}元 红包<#if (0 == readerList?size)>${note.note_packet_money?string('0.00')}<#else>${(note.note_packet_money / readerList?size)?string('0.00')}</#if>元 阅读${readerList?size}人<br/>
	
	                    <#-- 自己不用看到：已退回xxx， 查看流水 -->
	                    <#if (note.note_user_id != user.user_id)>
	                    	已退回<strong class="colorf15438 fsize"><#if (0 == readerList?size)>${(note.note_open_money)?string('0.00')}<#else>${(note.note_open_money + (note.note_packet_money / readerList?size))?string('0.00')}</#if></strong>元 <a class="color2e54a5 fsize" href="/weixin/note/capital/index">查看流水</a></p>
	                    </#if>
	                <#elseif note.note_target_status == 1>
	                    <#-- 成功 -->
	                    <p class="mt45 mb158">阅读费${note.note_open_money}元 阅读${(readerList?size)}人</p>
	                </#if>
	            <#else>
	                <#-- 可分享的底部 -->
	                <em class="newMoney">最新价: ${currentPrice}</em>
	                <p>阅读费${note.note_open_money}元 阅读${readerList?size}人<br/>若不达目标，阅读费退回，作者额外发红包</p>
	                
	            </#if>
	            <#if (2 > note.note_status && note.note_user_id == user.user_id)>
	                <div class="moreBtn mg68">
	                    <a id="share">分享好友</a>
	                    <a href="/weixin/note/reader/view/${note.note_id}">预览</a>
	                </div>
	            </#if>
	        </div>
  			<div class="textalign plpd">
				<div class="readPeopleInfo textalign" isBuy="${type}" id="dz">
					<a href="javascript:void(0);">
						<i class="defalut_good  showLike <#if type == 1 > good</#if>" targetid="good"></i>
						<span  id="good" class="color969696">${paise}</span>
					</a>
					<a href="javascript:void(0);">
						<i class="default_nogood  showLike <#if type == 2> nogood</#if>"  targetid="nogood"></i>
						<span id="nogood" class="color969696">${step}</span>
					</a>
				</div>
	            <ul class="user-list clearfix">
	         	    <#if readerList?? && readerList?size lt 21>
			            <#list readerList as l>
							     <li><img src="${l.user.user_header_img?substring(0, l.user.user_header_img?last_index_of("/0")) + "/46"}"/></li>
			            </#list>
		            <#else>
		            	<#list readerList as l>
		            	    <#if l_index lt 19>
							     <li><img src="${l.user.user_header_img?substring(0, l.user.user_header_img?last_index_of("/0")) + "/46"}"/></li>
			             	</#if>
		             	 </#list>
		             	 <li><a href="/weixin/note/reader/initReader/${note.note_id}"><div class="showMore">...</div></a></li>
		            </#if>	
	            </ul>
			</div>
						
	    <!--评论-->
		<div class="commontDetail">
			<header class="commontHead">
				评论<a id="writeCom" href="javascript:void(0);">写评论...</a>
			</header>
			<ul class="commontCont">
			</ul>
		</div>
			<div id="load" class="loading">
				<input type="hidden" id="p" value="0" />
				<a href="javascript:void(0);"><img src="${staticUrl}${request.contextPath}/weixin/note/img/loading.gif"/><span>正在加载</span></a>
			</div>
			<!--评论结束	-->
			<#include "Footer.ftl" />
		</section>
		<div class="share_tip"><i></i></div>

        <#-- 弹出提示框 -->
		<#include "Dialog.ftl" />
    	<!--写评论-->
    	<div class="writeCom">
			<textarea rows="5" cols="5" placeholder="写评论..." id="commontCont"></textarea>
			<div class="commentBtn">
				<span id="cancel">取消</span><a href="javascript:void(0);" class="hover" hoverClass="hoverClass" id="writeCom_btn">发表</a>
			</div>
		</div>
		<!--写评论结束-->
		<#-- 二维码弹出框 -->
		<#if (0 == subscribe && pay == 1)>
		    <div class="tcode_mask" style="display: block;">
		        <div class="tcode_img">
		            <div>
		                <img src="${staticUrl}${request.contextPath}/weixin/note/img/twocode.png">
		                <p>关注“笔记”公众号</p>
		            </div>
		            <p  class="tcode_btn">关闭</p>
		        </div>
		    </div>
		</#if>
		
		<script src="${staticUrl}${request.contextPath}/weixin/note/js/noteDetail.js"></script>
		<script>
			function execGood(type){
				$.get("${request.contextPath}/weixin/note/reader/reCounting/${note.note_id}/"+type , function(data){
					var data=JSON.parse(data);
					$("#good").html(data.paise);
					$("#nogood").html(data.step);
				});
			}
			
		    var c =1;
			function scrollLoad() {
				var This = $(this);
                var p = $("#p").val();
                var html = [];
                if (c == p) {
                    $("#load").hide();
                    return;
                }
				$("#p").val(parseInt(p) + 1);
				$.get("${request.contextPath}/weixin/note/comment/asyncQuery/${note.note_id}/"+(parseInt(p) + 1), function(data){
					var html=[];
					data=JSON.parse(data);
					c=data.count?data.count:1;
				 	$.each(data.list, function(i, o) {
				 		html.push('<li><img src="'+o.comment_user_header_img+'" alt="头像"/><div class="commontWord"><p>'+o.comment_user_nickname+'<em>'+o.comment_create_time+'</em></p><p>'+o.comment_content+'</p></div></li>');
				 	})
				 	$(".commontCont").append(html.join(""));
				});
			}
			
			$("#writeCom_btn").click(function(){
				var val=$("#commontCont").val();
				if(val){
					$.post("${request.contextPath}/weixin/note/comment/asyncSave",{noteId:${note.note_id},content:val},function(data){
						window.location.reload();
					});
				}else {
					dialog("评论内容不能为空");
				}
			});
			
			scrollLoad();
		</script>
	</body>
</html>

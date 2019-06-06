<!doctype html>
<html lang="en">
    <head>
        <#include "Header.ftl" />
        <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/attenList.css?version=${version}" />
    </head>
    
    <body>
		<div class="pageCont">
			<div class="attenList_head" id="switch">
				<span class="on" target-id="attenList" hide-id="goodList">关注</span>
				<span target-id="goodList" hide-id="attenList">精选</span>
			</div>
			
			<div id="attenList">
				<ul class="attenList">
					<#if list?? && 0 < list?size>
						<#list list as l>
							<li class="atten_item clearfix">
								<img src="${l.user.user_header_img}" alt="头像">
								<div class="item_head">
									<div class="clearfix">
										<span class="itemHeadCont">
											<p>${l.user.user_nickname}</p>
											<span>${l.note_start_time?string('MM-dd')}</span><span>下午计算</span>
										</span>
										<span class="itemHead_right">${l.note_open_money}元</span>
									</div>
									<div class="item_detail"><#if (l.note_title?? && 0 < l.note_title?length)>${l.note_title}。</#if>${l.note_remark}</div>
								</div>
							</li>
						</#list>
		            <#else>
		            	<ul><li></li><li>没有数据！</li><li></li></ul>
					</#if>
				</ul>
			</div>
            <div id="load" class="loading">
                <input type="hidden" id="p" value="${p}" />
                <script>$("#p").val(1);</script>
                <a href="javascript:void(0);"><img src="${staticUrl}${request.contextPath}/weixin/note/img/loading.gif"/><span>正在加载</span></a>
            </div>
	        <script type="text/javascript">
	            // 滚动加载方法
	            var c = ${count};
	            function scrollLoad(id) {
	                var This = $(this);
	                var p = This.children("#p").val();
	                var html = [];
	                if (c == p) {
	                    $("#load").hide();
	                    return;
	                }
	                $.get("${request.contextPath}/weixin/note/ajax?p=" + (parseInt(p) + 1), function (data) {
	                    $.each(JSON.parse(data).list, function(i, o) {
	                        var ul_class = "";
	                        var status = "";
	                        var icon2 = "";
	                        var icon1 = "";
	                        var icon3 = "";
	                        var color="colorf15438";
	                        if ("0" == o.note_status) {
	                            status = "未开盘";
	                        }
	                        if ("1" == o.note_status) {
	                            status = "进行中";
	                        }
	                        if ("2" == o.note_status) {
	                            ul_class = "class='colorb4b4b4 Fsize20'";
	                            status = "已结束";
	                            color="colorb4b4b4";
	                        }
	                        if ("2" == o.note_status && "0" == o.note_target_status) {
	                            icon2 = "<i class='icon2'></i>";
	                        }
	                        if ("2" == o.note_status && "1" == o.note_target_status) {
	                            icon1 = "<i class='icon1'></i>";
	                        }
	                        if ("2" == o.note_status && "2" == o.note_target_status) {
	                            icon3 = "<i class='icon3'></i>";
	                        }
	                        html.push("<div class='ml30'><ul " + " onclick=\"window.location.href='/weixin/note/reader/read/" + o.note_id + "'\">");
	                        html.push("<li>" + o.note_stock_name + "<br/><em class=\""+color+" Fsize20\">" + status + "</em></li>");
	                        html.push("<li>" + o.note_target_day + "日&nbsp;&nbsp;" + o.note_increase + "%" + icon2 + icon1 + icon3 + "<br/><span class=\""+color+" Fsize20\">"+getDateInfo(o.note_start_time)+"起</span></li>");
	                        html.push("<li>" + o.note_amount + "</li>");
	                        html.push("</ul></div>");
	                    });
	                    This.siblings("#attenList").append(html.join(""));
	                    $("#load").hide();
	                });
	                $("#p").attr("value", parseInt(p) + 1);
	            }
	            function getDateInfo(str){
	            	var date=new Date(str);
	            	var month=date.getMonth()+1;
	            	month=month>9?month:"0"+month;
	            	var day=date.getDate();
	            	day=day>9?day:"0"+day;
	            	return month+"-"+day;
	            }
	        </script>
			
			
		<div id="goodList" class="dn">
			<ul class="attenList">
				<li class="atten_item clearfix">
					<img src="" alt="头像">
					<div class="item_head">
						<div class="clearfix">
							<span class="itemHeadCont">
								<p>李建泽1</p>
								<span>05-03</span><span>下午计算</span>
							
							</span>
							<span class="itemHead_right">
								2.00元
							</span>
						</div>
						<div class="item_detail">
							发布新笔记。股票代码002xxx，4日预期收益率6%，新能源汽车概念股,新能源
						</div>
					</div>
				</li>
				
				<li class="atten_item clearfix">
					<img src="" alt="头像">
					<div class="item_head">
						<div class="clearfix">
							<span class="itemHeadCont">
								<p>李建泽1</p>
								<span>05-03</span><span>下午计算</span>
							</span>
							<span class="itemHead_right color969696">
								已停售
							</span>
						</div>
						<div class="item_detail">
							发布新笔记。股票代码002xxx，4日预期收益率6%，新能源汽车概念股,新能源
						</div>
					</div>
				</li>
				<li class="atten_item clearfix">
					<img src="" alt="头像">
					<div class="item_head">
						<div class="clearfix">
							<span class="itemHeadCont">
								<p>李建泽1</p>
								<span>05-03</span><span>下午计算</span>
							</span>
							<span class="itemHead_right color969696">
								已停售
							</span>
						</div>
						<div class="item_detail">
							发布新笔记。股票代码002xxx，4日预期收益率6%，新能源汽车概念股,新能源
						</div>
					</div>
				</li>
				<li class="atten_item clearfix">
					<img src="" alt="头像">
					<div class="item_head">
						<div class="clearfix">
							<span class="itemHeadCont">
								<p>李建泽1</p>
								<span>05-03</span><span>下午计算</span>
							</span>
							<span class="itemHead_right color969696">
								已停售
							</span>
						</div>
						<div class="item_detail">
							发布新笔记。股票代码002xxx，4日预期收益率6%，新能源汽车概念股,新能源
						</div>
					</div>
				</li>
				<li class="atten_item clearfix">
					<img src="" alt="头像">
					<div class="item_head">
						<div class="clearfix">
							<span class="itemHeadCont">
								<p>李建泽1</p>
								<span>05-03</span><span>下午计算</span>
							</span>
							<span class="itemHead_right color969696">
								已停售
							</span>
						</div>
						<div class="item_detail">
							发布新笔记。股票代码002xxx，4日预期收益率6%，新能源汽车概念股,新能源
						</div>
					</div>
				</li>
				<li class="atten_item clearfix">
					<img src="" alt="头像">
					<div class="item_head">
						<div class="clearfix">
							<span class="itemHeadCont">
								<p>李建泽1</p>
								<span>05-03</span><span>下午计算</span>
							</span>
							<span class="itemHead_right">
								达到目标
							</span>
						</div>
						<div class="item_detail1">
							<h3 class="detail_head"><span>中核科技</span>&nbsp;<span>002126</span><span class="fr">5日上涨10%</span></h3>
							<p>
								新能源汽车概念股，新能源已经成为现阶新能源汽车概念股新能源已经沉
							</p>
							<ul class="priceList clearfix">
								<li class="textLeft">起算价 10.00</li>
								<li class="textCenter">目标价 11.00</li>
								<li class="textRight">最新价 11.00</li>
							</ul>
						</div>
					</div>
				</li>
			</ul>
			</div>
			<div class="loading">
				<a href="javascript:void(0);"><img src="${staticUrl}${request.contextPath}/weixin/note/img/loading.gif"/><span>正在加载</span></a>
			</div>
		</div>
		<#include "Footer.ftl"/>
		<script src="${staticUrl}${request.contextPath}/weixin/note/js/attenList.js"></script>
	</body>
</html>

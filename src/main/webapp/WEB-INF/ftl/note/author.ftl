<!DOCTYPE html>
<html>
<head>
<#include "Header.ftl"/>
    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/authorsuccess.css?version=${version}" />
</head>

	<body>
		<section class="footAdap">
    <header class="header_cont">
        <#include "Profile.ftl" />     
        
        <ul class="listHead">
            <li>名称</li>
            <li>目标涨幅</li>
            <li>阅读量</li>
        </ul>
    </header>
    <div class="list_suce borderclear">
    	<#if list?? && 0 < list?size>
			<#list list as l>
	            <ul onclick="window.location.href='/weixin/note/reader/read/${l.note_id}'">
	                <li>
	                    <#if l.note_status != 2>
	                        ${l.note_stock_code}<br/><em class="colorf15438 Fsize20"><#if l.note_status == 0>待开盘<#elseif l.note_status == 1>进行中</#if></em>
	                    <#else>
	                        ${l.note_stock_code}<br/><em class="colorb4b4b4 Fsize20">${l.note_stock_name}</em>
	                    </#if>
	                </li>
		            <li><label class="increase">${l.note_target_day}日&nbsp;&nbsp;${l.note_increase}%<br/><span class="<#if l.note_status == "2">colorb4b4b4<#else>colorf15438</#if> Fsize20">${l.note_start_time?string('MM-dd')}起</span></label>	                                    
	                            <#if l.note_status == "2" && l.note_target_status == "0"><i class="icon2"></i></#if>
	                            <#if l.note_status == "2" && l.note_target_status == "1"><i class="icon1"></i></#if>
	                            <#if l.note_status == "2" && l.note_target_status == "2"><i class="icon3"></i></#if>
	                 </li>
	                <li>${l.note_amount}</li>
	            </ul>
			</#list>
        <#else>
        	<ul><li></li><li>没有数据！</li><li></li></ul>
        </#if>
    </div>
    <div id="load" class="loading">
        <input type="hidden" id="p" value="${p}" />
        <input type="hidden" id="userId" value="${userId}" />
        <script>$("#p").val(1);</script>
        <a href="javascript:void(0);"><img src="${staticUrl}${request.contextPath}/weixin/note/img/loading.gif"/><span>Loading......</span></a>
    </div>
    
    <#include "Footer.ftl" />
</section>

<script src="${staticUrl}${request.contextPath}/weixin/note/js/authorsuccess.js"></script>
<script src="${staticUrl}${request.contextPath}/weixin/note/js/attention.js"></script>
<script type="text/javascript">
    // 滚动加载方法
    var c = ${count};
    function scrollLoad() {
        var This = $(this);
        var p = This.children("#p").val();
        var uid= This.children("#userId").val();
        var html = [];
        if (c == p) {
            $("#load").hide();
            return;
        }
        $.get("${request.contextPath}/weixin/note/author/ajax?p=" + (parseInt(p) + 1) + "&userId=" + uid, function (data) {
            $.each(JSON.parse(data).list, function(i, o) {
                var ul_class = "";
                var status = "";
                var icon2 = "";
                var icon1 = "";
                var icon3 = "";
                var color="colorf15438";
                if ("2" == o.note_status && "0" == o.note_target_status) {
                    icon1 = "<i class='icon2'></i>";
                }
                if ("2" == o.note_status && "1" == o.note_target_status) {
                    icon2 = "<i class='icon1'></i>";
                }
                if ("2" == o.note_status && "2" == o.note_target_status) {
                    icon3 = "<i class='icon3'></i>";
                    
                }

                html.push("<ul " + ul_class + " onclick=\"window.location.href='/weixin/note/reader/read/" + o.note_id + "'\">");
                if ("0" == o.note_status) {
                    html.push("<li>" + o.note_stock_code + "<br/><em class=\"colorf15438 Fsize20\">待开盘</em></li>");
                }
                if ("1" == o.note_status) {
                    html.push("<li>" + o.note_stock_code + "<br/><em class=\"colorf15438 Fsize20\">进行中</em></li>");
                }
                if ("2" == o.note_status) {
                    html.push("<li>" + o.note_stock_code + "<br/><em class=\"colorb4b4b4 Fsize20\">" + o.note_stock_name + "</em></li>");
                    color="colorb4b4b4";
                }
                
                html.push("<li>" + o.note_target_day + "日&nbsp;&nbsp;" + o.note_increase + "%" + icon2 + icon1 + icon3 + "<br/><span class=\""+color+" Fsize20\">"+getDateInfo(o.note_start_time)+"起</span></li>");
                html.push("<li>" + o.note_amount + "</li>");
                html.push("</ul>");
            });
            This.siblings(".list_suce").append(html.join(""));
            $("#load").hide();
        });
        $("#p").attr("value", parseInt(p) + 1);
    }
    
	    function getDateInfo(str){
			var date=new Date(str);
			var month=date.getMonth()+1;
			month=month>9?month:"0"+month;
			var day=date.getDay();
			day=day>9?day:"0"+day;
			return month+"-"+day;
	    }
</script>


	</body>
</html>

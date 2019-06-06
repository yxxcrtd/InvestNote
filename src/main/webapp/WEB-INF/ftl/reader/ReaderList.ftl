<!DOCTYPE html>
<html>
    <head>
        <#include "Header.ftl"/>
        <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/tabPage.css?version=${version}" />
    </head>

    <body>
        <div class="tab_cont">
            <ul class="listHead">
                <li>名称</li>
                <li>目标涨幅</li>
                <li>作者</li>
            </ul>
            <div>
                <div class="list_suce">
            		<#if list?? && 0 < list?size>
	                    <#list list as l>
	                        <div class="ml30">
	                            <ul onclick="window.location.href='/weixin/note/reader/read/${l.note.note_id}'">
	                                <li>${l.note.note_stock_name}<br/><em  class="<#if l.note.note_status == "2">colorb4b4b4<#else>colorf15438</#if>  Fsize20"><#if l.note.note_status == "1">进行中<#elseif l.note.note_status == "2">已结束<#else>待开盘</#if></em></li>
	                                <li><label class="increase">${l.note.note_target_day}日&nbsp;&nbsp;${l.note.note_increase}%<br/><span class="<#if l.note.note_status == "2">colorb4b4b4<#else>colorf15438</#if> Fsize20">${l.note.note_start_time?string('MM-dd')}起</span></label>	                                    
	                                    <#if l.note.note_status == "2" && l.note.note_target_status == "0"><i class="icon2"></i></#if>
	                                    <#if l.note.note_status == "2" && l.note.note_target_status == "1"><i class="icon1"></i></#if>
	                                    <#if l.note.note_status == "2" && l.note.note_target_status == "2"><i class="icon3"></i></#if>
	                                 </li>
	                                <li>${l.user.user_nickname}</li>
	                            </ul>
	                        </div>
	                    </#list>
		            <#else>
		            	<ul><li></li><li>没有数据！</li><li></li></ul>
		            </#if>
                </div>
                <div id="load" class="loading">
                    <input type="hidden" id="p" value="${p}" />
                    <script>$("#p").val(1);</script>
                    <a href="javascript:void(0);"><img src="${staticUrl}${request.contextPath}/weixin/note/img/loading.gif"/><span>正在加载</span></a>
                </div>
            </div>
        </div>

<script src="${staticUrl}${request.contextPath}/weixin/note/js/tabPage.js"></script>
<script type="text/javascript">
    //滚动加载方法
    var c = ${count};
    function scrollLoad() {
        var This = $(this);
        var p = This.children("#p").val();
        var html = [];
        if (c == p) {
            $("#load").hide();
            return;
        }
        $.get("${request.contextPath}/weixin/note/reader/my_read/ajax?p=" + (parseInt(p) + 1), function (data) {
            $.each(JSON.parse(data).list, function(i, o) {
                var ul_class = "";
                var status = "";
                var icon2 = "";
                var icon1 = "";
                var icon3 = "";
                var color="colorf15438";
                if ("0" == o.note.note_status) {
                    status = "未开盘";
                }
                if ("1" == o.note.note_status) {
                    status = "进行中";
                }
                if ("2" == o.note.note_status) {
                    ul_class = "class='colorb4b4b4'";
                    status = "已结束";
                     color="colorb4b4b4";
                }
                if ("2" == o.note.note_status && "0" == o.note.note_target_status) {
                    icon2 = "<i class='icon2'></i>";
                }
                if ("2" == o.note.note_status && "1" == o.note.note_target_status) {
                    icon1 = "<i class='icon1'></i>";
                }
                if ("2" == o.note.note_status && "2" == o.note.note_target_status) {
                    icon3 = "<i class='icon3'></i>";
                    
                }
                
                
                html.push("<div class='ml30'><ul onclick=\"window.location.href='/weixin/note/reader/read/" + o.note.note_id + "'\">");
                html.push("<li>" + o.note.note_stock_name + "<br/><em class=\""+color+" Fsize20\">" + status + "</em></li>");
                html.push("<li>" + o.note.note_target_day + "日&nbsp;&nbsp;" + o.note.note_increase + "%" + icon2 + icon1 + icon3 + "<br/><span class=\""+color+" Fsize20\">"+getDateInfo(o.note.note_start_time)+"起</span></li>");
                html.push("<li>" + o.user.user_nickname + "</li>");
                html.push("</ul></div>");
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

        <#include "Footer.ftl" />
</body>

</html>

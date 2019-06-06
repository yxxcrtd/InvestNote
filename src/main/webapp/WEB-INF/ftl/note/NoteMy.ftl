<#include "Macro.ftl" />
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
                <li>阅读量</li>
            </ul>
            <div>
                <div class="list_suce">
            		<#if list?? && 0 < list?size>
	                    <#list list as l>
	                        <div class="ml30">
	                            <ul onclick="window.location.href='/weixin/note/reader/read/${l.note_id}'">
	                                <li>${l.note_stock_name}<br/><em class="<#if l.note_status == "2">colorb4b4b4<#else>colorf15438</#if> Fsize20"><#if (1 == l.note_pay_status)>未支付<#else><@compress single_line=true><@getNoteStatus l.note_status /></@compress></#if></em></li>
	                                <li>
	                                    <label class="increase">${l.note_target_day}日&nbsp;&nbsp;${l.note_increase}%<br/><span class="<#if l.note_status == "2">colorb4b4b4<#else>colorf15438</#if> Fsize20">${l.note_start_time?string('MM-dd')}起</span></label>
	                                    <#if l.note_status == "2" && l.note_target_status == "0"><i class="icon2"></i></#if><#-- icon2 未达标的图标 -->
	                                    <#if l.note_status == "2" && l.note_target_status == "1"><i class="icon1"></i></#if><#-- icon1 达标的图标 -->
	                                    <#if l.note_status == "2" && l.note_target_status == "2"><i class="icon3"></i></#if><#-- icon3 无效的图标 -->
	                                </li>
	                                <li>${l.note_amount}</li>
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
            // 滚动加载方法
            var c = ${count};
            function scrollLoad() {
                var This = $(this);
                var p = This.children("#p").val();
                var html = [];
                if (c == p) {
                    $("#load").hide();
                    return;
                }
                $.get("${request.contextPath}/weixin/note/my_note/ajax?p=" + (parseInt(p) + 1), function (data) {
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
                    This.siblings(".list_suce").append(html.join(""));
                    $("#load").hide();
                });
                $("#p").attr("value", parseInt(p) + 1);
            }
            function getDateInfo(str) {
            	var date = new Date(str);
            	var month = date.getMonth()+1;
            	month = month > 9? month : "0" + month;
            	var day = date.getDate();
            	day = day > 9? day : "0" + day;
            	return month + "-" + day;
            }
        </script>

        <#include "Footer.ftl" />
    </body>
</html>
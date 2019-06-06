<!DOCTYPE html>
<html>
<head>
    <title>财猫投资笔记管理系统</title>
    <#include "/admin/include/tpl_menu_head.ftl" />
</head>
<body>
<#include "/admin/include/tpl_menu_top.ftl" />
<div class="body_container">
    <#include "/admin/include/tpl_menu_left.ftl" />
    <div class="main_content">
        <div class="container">
            <h4 class="heading">笔记列表</h4>
            <form method="post" class="form-inline" action="/weixin/note/admin/note/list" name="searchForm">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>昵称：</label>
                    <input type="text" class="form-control input-sm" name="nickName" value="${nickName}" placeholder="昵称支持模糊查询" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>股票代码：</label>
                    <input type="text" class="form-control input-sm" name="code" value="${code}" placeholder="昵称支持模糊查询" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>投资时间：</label>
                    <input type="date" class="form-control input-sm" name="startDate" id="beginDateTime" value="${startDate}"> -
                    <input type="date" class="form-control input-sm" name="endDate" id="endDateTime" value="${endDate}">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
                <button type="button" class="btn btn-primary btn-sm" onClick="window.open('${request.contextPath}/weixin/note/admin/user/excel')">导出</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 100%;">
                <thead>
	                <tr>
	                    <th>作者</th>
	                    <th>股票代码</th>
	                    <th>股票名称</th>
	                    <th>天数</th>
	                    <th>预期涨幅</th>
	                    <th>起算价</th>
	                    <th>目标价</th>
	                    <th>创建时间</th>
	                    <th>起算时间</th>
	                    <th>实际终止时间</th>
	                    <th>未达目标时收盘价</th>
	                    <th>未达标实际涨跌幅</th>
	                    <th>运行中收盘价</th>
	                    <th>运行中涨跌幅</th>
	                    <th>阅读费</th>
	                    <th>阅读人数</th>
	                    <th>红包费</th>
	                    <th>状态</th>
	                    <th>成功状态</th>
	                    <th>操作</th>
	                </tr>
                </thead>
                <#list noteList as n>
                    <tr>
                        <td><a href="/weixin/note/admin/user/info?userId=${n.note_user_id}">${n.user.user_nickname}</a></td>
                        <td>${n.note_stock_code}</td>
                        <td>${n.note_stock_name}</td>
                        <td>${n.note_target_day}</td>
                        <td>${n.note_increase}%</td>
                        <td><#if (n.note_target_status == 0)  &&  (n.note_status == 2)>${n.note_earnest_money}</#if></td>
                        <td><#if (n.note_target_status == 0)  &&  (n.note_status == 1)>${n.note_earnest_money}</#if></td>
                        <td>${n.note_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                        <td>${n.note_start_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                        <td>${n.note_end_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <#--<td><script>
	                        var arrDate="${n.note_start_time?string("yyyy-MM-dd HH:mm:ss")}".split(" ");
	                        if(new Date("1990-1-1 "+arrDate[1])<new Date("1990-1-1 "+"09:25:00") || new Date("1990-1-1 "+arrDate[1])>new Date("1990-1-1 "+"12:55:00")){
	                        	document.write(arrDate[0]+" 09:25");
	                        }else if(new Date("1990-1-1 "+arrDate[1])<new Date("1990-1-1 "+"12:55:00")){
	                        	document.write(arrDate[0]+" 13:00");
	                        }
                        </script></td>-->
                        <td>${n.note_stock_open_price}</td>
                        <td>${n.note_open_money}</td>
                        <td>
                            <#if (0 < n.note_amount)><a target_nid="${n.note_id}" href="javascript:;" class="users">${n.note_amount}</a><#else>${n.note_amount}</#if>
                        </td>
                        <td>${n.note_packet_money} = <#if (0 == n.note_target_Stauts)>${n.note_earnest_money}</#if></td>
                        <td>
                            <#if n.note_status == 0> 待开盘
                            <#elseif n.note_status == 1> 进行中
                            <#elseif n.note_status == 2> 已结束
                            </#if>
                        </td>
                        <td>
                            <#if n.note_target_status == 0> 未达标
                            <#elseif n.note_target_status == 1> 达标
                            <#elseif n.note_target_status == 2> 无效
                            </#if>
                        </td>
                        <td>
                            <a href="/weixin/note/admin/note/info?noteId=${n.note_id}">查看</a>&nbsp;&nbsp;
                            <a id="copy${n.note_id}" href="javascript:;" data-clipboard-target="noteUrl${n.note_id}">复制地址</a>
                            <input id="noteUrl${n.note_id}" value="${domain}${request.contextPath}/weixin/note/reader/read/${n.note_id}" />
                            <script>var clip = new ZeroClipboard($("#copy${n.note_id}"));</script>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>
<#include "/admin/include/tpl_menu_footer.ftl" />
<script>
    $(function() {
        $(".users").on("click", function () {
            var cur = $(this);
            $.get("${request.contextPath}/weixin/note/admin/note/getUsers?noteId=" + cur.attr("target_nid"), function (data) {
                var img = "";
                $.each(JSON.parse(data).list, function(i, o) {
                    img += "<a href='${request.contextPath}/weixin/note/admin/user/info?userId=" +  o.user.user_id + "' target='_blank'><img src=" + o.user.user_header_img + " width='30' /></a>&nbsp;&nbsp;";
                });
                cur.parents("tr").after("<tr><td colspan='20' style='text-align: center;'>" + img + "</td></tr>");
            });
            // 取消click绑定事件
            cur.off("click");
        });
    });
</script>
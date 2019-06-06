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
            <h4 class="heading">充值列表</h4>
            <form method="post" class="form-inline" action="/weixin/note/admin/deposit/list" name="searchForm">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>昵称：</label>
                    <input type="text" class="form-control input-sm" name="nickName" value="${nickName}" placeholder="昵称支持模糊查询" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>订单号：</label>
                    <input type="text" class="form-control input-sm" name="billNo" value="${billNo}" placeholder="输入订单号" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>充值时间：</label>
                    <input type="date" class="form-control input-sm" name="startDate" id="beginDateTime" value="${startDate}"> -
                    <input type="date" class="form-control input-sm" name="endDate" id="endDateTime" value="${endDate}">
                </div>
                <div class="form-group">
                    <select name="status">
                        <option value="0">全部</option>
                        <option value="1" <#if status == 1>selected</#if>>等待支付</option>
                        <option value="2" <#if status == 2>selected</#if>>支付成功</option>
                        <option value="3" <#if status == 3>selected</#if>>支付失败</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>昵称</th>
                    <th>订单号</th>
                    <th>订单金额</th>
                    <th>充值状态</th>
                    <th>支付笔记</th>
                    <th>充值类型</th>
                    <th>充值时间</th>
                </tr>
                </thead>
                <#list list as l>
                    <tr>
                        <td><a href="/weixin/note/admin/user/info?userId=${l.user_id}">${l.user_nickname}</a></td>
                        <td>${l.deposit_bill_id}</td>
                        <td>${l.deposit_money}</td>
                        <td>
                            <#switch l.deposit_status>
                                <#case 1>等待支付<#break />
                                <#case 2>支付成功<#break />
                                <#case 3>支付失败<#break />
                            </#switch>
                        </td>
                        <td><a href="/weixin/note/admin/note/info?noteId=${l.deposit_note_id}">${l.deposit_note_id}</a></td>
                        <td>
                            <#switch l.deposit_action>
                                <#case 1>发布笔记<#break />
                                <#case 2>阅读笔记<#break />
                            </#switch>
                        </td>
                        <td>${l.deposit_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>
<#include "/admin/include/tpl_menu_footer.ftl" />
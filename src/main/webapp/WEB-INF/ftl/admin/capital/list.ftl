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
            <h4 class="heading">流水列表</h4>
            <form method="post" class="form-inline" action="/weixin/note/admin/capital/list" name="searchForm">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>昵称：</label>
                    <input type="text" class="form-control input-sm" name="nickname" value="${nickname}" placeholder="昵称支持模糊查询" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>投资时间：</label>
                    <input type="date" class="form-control input-sm" name="startDate" id="beginDateTime" value="${startDate}"> -
                    <input type="date" class="form-control input-sm" name="endDate" id="endDateTime" value="${endDate}">
                </div>
                <div class="form-group">
                    <label>流水类型：</label>
                    <select name="type">
                        <option value="">全部</option>
                        <option value="1" <#if type=="1">selected</#if>>充值</option>
                        <option value="2" <#if type=="2">selected</#if>>提现</option>
                        <option value="3" <#if type=="3">selected</#if>>红包费</option>
                        <option value="4" <#if type=="4">selected</#if>>红包费退回</option>
                        <option value="5" <#if type=="5">selected</#if>>发红包</option>
                        <option value="6" <#if type=="6">selected</#if>>阅读费</option>
                        <option value="7" <#if type=="7">selected</#if>>阅读费退回</option>
                        <option value="8" <#if type=="8">selected</#if>>阅读费收益</option>
                        <option value="9" <#if type=="9">selected</#if>>技术服务费</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
                <button type="button" class="btn btn-primary btn-sm" onClick="window.open('${request.contextPath}/weixin/note/admin/user/excel')">导出</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>用户</th>
                    <#--<th>头像</th>-->
                    <th>变动类型</th>
                    <th>可用余额</th>
                    <th>阅读费冻结</th>
                    <th>发红包冻结</th>
                    <th>冻结总额</th>
                    <th>提现处理中</th>
                    <th>提现失败</th>
                    <th>涉及股票</th>
                    <th>变动时间</th>
                   <#-- 
                    <th>变动金额</th>
                    <th>变动后可用</th>
                    <th>变动后冻结</th>-->
                </tr>
                </thead>
            <#list list as c>
                <tr>
                    <td><a href="/weixin/note/admin/user/info?userId=${c.capital_user_id}">${c.user.user_nickname}</a></td>
                    <#--<td><img href="${c.user.user_header_img}" width="56px" height="56px" /></td>-->
                    <td>
                        <#switch c.capital_type>
                            <#case 1>充值额<#break >
                            <#case 2>提现额<#break >
                            <#case 3>押红包<#break >
                            <#case 4>退红包<#break >
                            <#case 5>赚阅读费<#break >
                            <#case 6>付阅读费<#break >
                            <#case 7>退阅读费<#break >
                            <#case 8>收割红包<#break >
                            <#case 9>技术服务费<#break >
                        </#switch>
                    </td>
                    <td>${c.capital_available}</td>
                    <td>${c.capital_mount}</td>
                    <td></td>
                    <td>${c.capital_freeze}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>${c.capital_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                </tr>
            </#list>
                </tbody>
            </table>
        ${pageHtml}
        </div>
    </div>
</div>
<#include "/admin/include/tpl_menu_footer.ftl" />
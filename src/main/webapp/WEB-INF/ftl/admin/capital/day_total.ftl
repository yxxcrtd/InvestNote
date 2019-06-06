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
            <h4 class="heading">充值、提现按日汇总列表</h4>
            <div>
                <button type="button" class="btn <#if type == 1>btn-primary</#if> btn-sm" onclick="window.location.href='/weixin/note/admin/capital/day_total?type=1'">充值总额</button>
                <button type="button" class="btn <#if type == 2>btn-primary</#if> btn-sm" onclick="window.location.href='/weixin/note/admin/capital/day_total?type=2'">提现总额</button>
                <button type="button" class="btn <#if type == 3>btn-primary</#if> btn-sm" onclick="window.location.href='/weixin/note/admin/capital/day_total?type=3'">总可用金额</button>
                <button type="button" class="btn <#if type == 4>btn-primary</#if> btn-sm" onclick="window.location.href='/weixin/note/admin/capital/day_total?type=4'">总阅读费冻结</button>
                <button type="button" class="btn <#if type == 5>btn-primary</#if> btn-sm" onclick="window.location.href='/weixin/note/admin/capital/day_total?type=5'">总发红包冻结</button>
            </div>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>日期</th>
                    <th>金额</th>
                </tr>
                </thead>
            <#list list as c>
                <tr>
                    <td>${c.ct}</td>
                    <td>${c.money}</td>
                </tr>
            </#list>
                </tbody>
            </table>
            <div>
                <button type="button" class="btn btn-primary btn-sm" onClick="window.open('${request.contextPath}/weixin/note/admin/user/excel')">导出</button>
            </div>
        </div>
    </div>
</div>
<#include "/admin/include/tpl_menu_footer.ftl" />
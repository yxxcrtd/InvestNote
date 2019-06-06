<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <#include "/admin/include/tpl_menu_head.ftl" />
</head>
<body>
<#include "/admin/include/tpl_menu_top.ftl" />
<div class="body_container">
    <#include "/admin/include/tpl_menu_left.ftl" />
    <div class="main_content">
        <div class="container">
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="4">用户详情</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="25%">用户昵称：${user.user_nickname}</td>
                    <td width="25%">手机号：${user.user_phone}</td>
                    <td width="25%">笔记数：${user.user_note_count}</td>
                    <td width="25%">成功率：${user.user_success}</td>

                </tr>
                <tr>
                    <td width="25%">平均收益：${user.user_yield}</td>
                    <td>可用资金：${user.user_available_money}</td>
                    <td>冻结资金：${user.user_freeze_money}</td>
                    <td>注册时间：${user.user_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                </tr>
                </tbody>
            </table>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="4">绑定银行卡信息</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="25%">银行名称：${bank.bank_name}</td>
                    <td width="25%">姓名：${bank.bank_user_name}</td>
                    <td width="25%">卡号：${bank.bank_user_code}</td>
                    <td width="25%">手机号：${bank.bank_phone}</td>
                </tr>
                <tr>
                    <td>省：${bank.bank_add_province}</td>
                    <td>市：${bank.bank_add_city}</td>
                    <td>开户行：${bank.bank_open_bank}</td>
                    <td>绑定时间：${bank.bank_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                </tr>
                </tbody>
            </table>
            <div>
                <a href="/weixin/note/admin/note/list?userId=${user.user_id}">笔记记录</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="/weixin/note/admin/capital/list?userId=${user.user_id}">资金流水</a>
            </div>
        </div>
    </div>
</div>
<#include "/admin/include/tpl_menu_footer.ftl" />
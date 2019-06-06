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
                    <td width="25%">用户昵称：<a href="/weixin/note/admin/user/info?userId=${user.user_id}">${user.user_nickname}</a></td>
                    <td width="25%">手机号：${user.user_phone}</td>
                    <td width="25%">笔记数：${user.user_note_count}</td>
                    <td width="25%">成功率：${user.user_success}</td>

                </tr>
                <tr>
                    <td width="25%">平均收益：${user.user_yield}</td>
                    <td>可用资金：${user.user_available_money}</td>
                    <td>冻结资金：${user.user_freeze_money}</td>
                    <td>注册时间：${user.user_create_time}</td>
                </tr>
                </tbody>
            </table>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="6">笔记详情</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="16%">股票代码：${note.note_stock_code}</td>
                    <td width="16%">股票名称：${note.note_stock_code}</td>
                    <td width="16%">开盘价：${note.note_stock_open_price}</td>
                    <td width="16%">最高价：${note.note_stock_high_price}</td>
                    <td width="16%">目标天数：${note.note_target_day}</td>
                    <td width="16%">预计涨幅：${note.note_increase}%</td>
                </tr>
                <tr>
                    <td>阅读费：${note.note_open_money}</td>
                    <td>红包费：${note.note_packet_money}</td>
                    <td>参与人数：${note.note_amount}</td>
                    <td>笔记状态：<#if note.note_status == 0> 待开盘
                    <#elseif note.note_status == 1> 进行中
                    <#elseif note.note_status == 2> 已结束
                    </#if></td>
                    <td>达标状态：<#if note.note_target_status == 0> 未达标
                    <#elseif note.note_target_status == 1> 达标
                    <#elseif note.note_target_status == 2> 无效
                    </#if></td>
                    <td>创建时间：${note.note_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                </tr>
                <tr>
                    <td colspan="6">${note.note_remark}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<#include "/admin/include/tpl_menu_footer.ftl" />
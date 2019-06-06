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
            <h4 class="heading">用户列表</h4>
            <form method="post" class="form-inline" action="/weixin/note/admin/user/list" name="searchForm">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>昵称：</label>
                    <input type="text" class="form-control input-sm" name="nickName" value="${nickName}" placeholder="昵称支持模糊查询" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>注册时间：</label>
                    <input type="date" class="form-control input-sm" name="startDate" id="beginDateTime" value="${startDate}"> -
                    <input type="date" class="form-control input-sm" name="endDate" id="endDateTime" value="${endDate}">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
                <button type="button" class="btn btn-primary btn-sm" onClick="window.open('${request.contextPath}/weixin/note/admin/user/excel')">导出</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>用户编号</th>
                    <th>昵称</th>
                    <th>头像</th>
                    <th>手机号</th>
                    <th>注册时间</th>
                    <th>充值总额</th>
                    <th>提现总额</th>
                    <th>可用余额</th>
                    <th>写笔记总额</th>
                    <th>阅读总额</th>
                    <th>写笔记冻结额</th>
                    <th>阅读冻结额</th>
                    <th>冻结总额</th>
                    <th>被读者收割总额</th>
                    <th>收割读者总额</th>
                    <th>收割牛人总额</th>
                    <th>被牛人收割总额</th>
                    <th>写笔记总数</th>
                    <th>成功数</th>
                    <th>无效数</th>
                    <th>失败数</th>
                    <th>成功率</th>
                    <th>阅读总数</th>
                    <th>阅读达标数</th>
                    <th>阅读无效数</th>
                    <th>阅读失败数</th>
                    <th>操作</th>
                </tr>
                </thead>
                <#list userList as u>
                    <tr>
                        <td>${u.user_id}</td>
                        <td>${u.user_nickname}</td>
                        <td><img src="${u.user_header_img?substring(0, u.user_header_img?last_index_of("/0")) + "/96"}" width="64px" height="64px" /> </td>
                        <td>${u.user_phone}</td>
                        <td>${u.user_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>${u.user_freeze_money}</td>
                        <td>${u.user_freeze_money}</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>${u.user_note_count}</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>${u.user_success}</td>
                        <td>${userReadCount.totalRecharge+"${u.user_id}"}</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td><a href="/weixin/note/admin/user/info?userId=${u.user_id}">查看</a></td>
                    </tr>
                </#list>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>
<#include "/admin/include/tpl_menu_footer.ftl" />
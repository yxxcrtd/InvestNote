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
            <h4 class="heading">提现列表</h4>
            <form method="post" class="form-inline" action="/weixin/note/admin/withdraw/list" name="searchForm">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>提现状态：</label>
                    <select name="status">
                        <option value="">全部</option>
                        <option value="1" <#if status == 1>selected</#if>>未处理</option>
                        <option value="2" <#if status == 2>selected</#if>>处理中</option>
                        <option value="3" <#if status == 3>selected</#if>>成功</option>
                        <option value="4" <#if status == 4>selected</#if>>失败</option>
                        <option value="5" <#if status == 5>selected</#if>>不予处理</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>提现号</th>
                    <th>昵称</th>
                    <th>手机号</th>
                    <th>提现金额</th>
                    <th>银行</th>
                    <th>详细信息</th>
                    <th>提现状态</th>
                    <th>提现申请时间</th>
                    <th>操作</th>
                </tr>
                </thead>
            <#list list as l>
                <tr id="withdraw_${l.withdraw_id}">
                    <td>${l.withdraw_batch_no}</td>
                    <td>${l.user.user_nickname}</td>
                    <td>${l.user.user_phone}</td>
                    <td>${l.withdraw_money}</td>
                    <td>${l.bank.bank_name}</td>
                    <td>
                    ${l.bank.bank_user_name} (${l.bank.bank_user_code})<br>
                    ${l.bank.bank_add_province} - ${l.bank.bank_add_city} - ${l.bank.bank_open_bank}
                    </td>
                    <td>
                        <#if l.withdraw_status == 1>未处理
                        <#elseif l.withdraw_status == 2>处理中
                        <#elseif l.withdraw_status == 3>成功
                        <#elseif l.withdraw_status == 4><span style="color: #F00; font-weight: bold;">失败!</span>
                        <#elseif l.withdraw_status == 5>不予处理
                        </#if>
                    </td>
                    <td>${l.withdraw_create_time?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>
                        <#if l.withdraw_status == 1>
                            <a href="javascript:void(0);" onclick="doWithdraw(${l.withdraw_id}, 2)">确认提现</a>
                            <br /><br />
                            <a href="javascript:void(0);" onclick="doWithdraw(${l.withdraw_id}, 5)">不予处理</a>
                        </#if>
                        <#if l.withdraw_status == 2>
                            <a href="javascript:void(0);" onclick="doCheck(${l.withdraw_id})">查看状态</a>
                        </#if>
                    </td>
                </tr>
            </#list>
                </tbody>
            </table>
        ${pageHtml}
        </div>
    </div>
</div>
<script>
function doWithdraw(id, status) {
    var msg = "确认要 " + (status == 2? "提现" : "不予处理") + " 这笔提现记录吗？";
    if (!confirm(msg)) {
        return false;
    }
    $.ajax({
        url : "/weixin/note/admin/withdraw/do",
        data : {wId: id, status: status},
        type : "POST",
        dataType : "json",
        success : function(d) {
            if (d.res) {
                $('#withdraw_'+id).hide();
            }
        }
    });
}
    function doCheck(id) {
        $.ajax({
            url : "/weixin/note/admin/withdraw/doCheck",
            data : {wId: id, status: status},
            type : "POST",
            dataType : "json",
            success : function(d) {
                if (d.res) {
                    $('#withdraw_'+id).hide();
                } else {
                    alert("提现还未到账，再等等");
                }
            }
        });
    }
</script>

<#include "/admin/include/tpl_menu_footer.ftl" />
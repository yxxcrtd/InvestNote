<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/>
    <#include "Header.ftl"/>
    <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/createBank.css?version=${version}" />
</head>
<body target="submit">

<form action="/weixin/note/withdraw/bindBank" method="post" id="createBank">
    <div class="txt_list">
        <div class="input_txt clearfix">
            <label>姓名</label>
            <input type="text" name="bank_user_name" value=""/>
        </div>
        <div class="input_txt clearfix">
            <label>选择银行</label>
            <input type="text" id="select" readonly="">

            <div data-role="fieldcontain" class="demo-cont" id="demo_cont_select" style="display:none;">
                <select name="bank_code" id="demo_select" data-role="none">
                    <option value="1">工商银行</option>
                    <option value="2">建设银行</option>
                    <option value="3">农业银行</option>
                    <option value="4">邮政储蓄银行</option>
                    <option value="5">中国银行</option>
                    <option value="6">交通银行</option>
                    <option value="7">招商银行</option>
                    <option value="8">光大银行</option>
                    <option value="9">浦发银行</option>
                    <option value="10">华夏银行</option>
                    <option value="11">广东发展银行</option>
                    <option value="12">中信银行</option>
                    <option value="13">兴业银行</option>
                    <option value="14">民生银行</option>
                    <option value="15">杭州银行</option>
                    <option value="16">上海银行</option>
                    <option value="17">宁波银行</option>
                    <option value="18">平安银行</option>
                </select>
            </div>
        </div>
        <div class="input_txt clearfix">
            <label>卡号</label>
            <input type="text" name="bank_user_code" value=""/>
        </div>
        <div class="input_txt clearfix">
            <label>省市</label>
            <input type="text"  id="treelist_dummy" readonly="">
            <input type="hidden" name="bank_add_province" id="Province"/>
            <input type="hidden" name="bank_add_city" id="city"/>
            <ul id="treelist" style="display:none;">
            </ul>
        </div>
        <div class="input_txt clearfix">
            <label>开户行</label>
            <input type="text" name="bank_open_bank" value="" tip="开户行不能为空"/>
        </div>
    </div>
    <div class="txt_list">
        <div class="input_txt input_auth clearfix">
            <label>+86<i></i></label>
            <input type="text" placeholder="请输入手机号码" id="bank_phone" name="bank_phone" value="${user.user_phone}" <#if user.user_phone != "">readonly</#if>/>
        </div>
        <div class="input_txt input_authVal clearfix">
            <input type="text" name="smsCode" value=""/>
            <label id="getAuthCode" time="60"><i></i><span>获取验证码</span></label>
        </div>
    </div>
    <!--提示框-->
    <div class="dialog_tip_cont">您的号码有误，请重新输入</div>
    <a id="submit" class="footer_btn" onclick="$('#createBank').submit();">提交</a>
</form>

<script src="${staticUrl}${request.contextPath}/weixin/note/js/mobiscroll/mobiscroll.min.js"></script>
<link href="${staticUrl}${request.contextPath}/weixin/note/css/mobiscroll/mobiscroll.min.css" rel="stylesheet" type="text/css" />
<script src="${staticUrl}${request.contextPath}/weixin/note/js/createBank.js"></script>

<#include "Footer.ftl" />
</body>
</html>

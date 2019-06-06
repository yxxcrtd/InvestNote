<#include "Macro.ftl" />
<!doctype html>
<html lang="en">
    <head>
        <#include "Header.ftl" />
        <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/mymoney.css?version=${version}" />
    </head>

    <body>
        <p class="overf"></p>
        <div class="list">
            <a href="/weixin/note/withdraw/index">
                <div class="item-left"><i class="icon1"></i><span>账户余额（提现）</span></div>
                <div class="item-right"><span>${user.user_available_money}元</span><i class="icon4"></i></div>
                <#--（冻结:${user.user_freeze_money}元）-->
            </a>
        </div>

        <p class="btnDis"><a class="btn btn1" target="sel_mask" id="moneyTypeSel" defaultVal="${t}"><span id="selTitle">资金明细</span><i class="arrow"></i></a></p>

        <div class="list_suce list_suce_list">

        <#if (t == null || t == 2) && withdrawList?? && 0 < withdrawList?size>
            <#list withdrawList as w>
            <ul>
                <li>提现<br><em class="colorcfcfcf">${w.withdraw_create_time}</em></li>
                <li><em class="color1da158">${w.withdraw_money}</em><br><em class="colorcfcfcf">
                <#if w.withdraw_status == 1>
                    待审核
                    <#elseif w.withdraw_status == 2>
                        处理中
                    </#if>
                </em></li>
                </ul>
            </#list>
        </#if>

            <#if list?? && 0 < list?size>
                <#list list as l>
                    <ul>
                        <li><@compress single_line=true><@getCapitalType l.capital_type /></@compress><br/><em class="colorcfcfcf">${l.capital_create_time}</em></li>
                        <li><em class="<#if (0 > l.capital_mount)>color1da158</#if>"><#if (0 < l.capital_mount)>+<#else></#if>${l.capital_mount}</em><br/><em class="colorcfcfcf"></em></li>
                    </ul>
                </#list>
            <#else>
            	<ul><li></li><li>没有数据！</li><li></li></ul>
            </#if>
        </div>
        <div id="load" class="loading">
            <input type="hidden" id="p" value="${p}" />
            <input type="hidden" id="t" value="${t}" />
            <a href="javascript:void(0);"><img src="${staticUrl}${request.contextPath}/weixin/note/img/loading.gif"/><span>正在加载</span></a>
        </div>

        <div class="sel_mask">
            <div class="sel">
                <div class="title"><span>资金明细</span><i class="arrow"></i></div>
                <ul class="selItem" target="#selTitle">
                    <#--<@typeList types=[""] /> -->
                    <li index="-1" data-val="" <#if t??><#else>selected="true"</#if>>资金明细</li>
                    <li index="0" data-val="1" <#if (1 == t)>selected="true"</#if>>充值</li>
                    <li index="1" data-val="2" <#if (2 == t)>selected="true"</#if>>提现</li>
                    <li index="2" data-val="3" <#if (3 == t)>selected="true"</#if>>红包费</li>
                    <li index="3" data-val="4" <#if (4 == t)>selected="true"</#if>>红包费退回</li>
                    <li index="4" data-val="5" <#if (5 == t)>selected="true"</#if>>发红包</li>
                    <li index="5" data-val="6" <#if (6 == t)>selected="true"</#if>>阅读费</li>
                    <li index="6" data-val="7" <#if (7 == t)>selected="true"</#if>>阅读费退回</li>
                    <li index="7" data-val="8" <#if (8 == t)>selected="true"</#if>>阅读费收益</li>
                    <li index="8" data-val="9" <#if (9 == t)>selected="true"</#if>>技术服务费</li>
                </ul>
            </div>
        </div>

        <input type="hidden" id="ctx" value="${request.contextPath}" />
        <script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/mymoney.js"></script>
        <script type="text/javascript">
            //滚动加载方法
            var c = ${count};
            function scrollLoad() {
                var This = $(this);
                var p = This.children("#p").val();
                var html = [];
                if (c == p) {
                    $("#load").hide();
                    return;
                }
                $.get("${request.contextPath}/weixin/note/capital/ajax?p=" + (parseInt(p) + 1) + "&t=" + This.children("#t").val(), function (data) {
                    $.each(JSON.parse(data).list, function(i, o) {
                        var type = "";
                        switch (o.capital_type) {
                            case 1: type = '充值'; break;
                            case 2: type = '提现'; break;
                            case 3: type = '红包费'; break;
                            case 4: type = '红包费退回'; break;
                            case 5: type = '发红包'; break;
                            case 6: type = '阅读费'; break;
                            case 7: type = '阅读费退回'; break;
                            case 8: type = '阅读费收益'; break;
                            case 9: type = '技术服务费'; break;
                        }
                        var plus = "+";
                        var c = "";
                        if (0 > parseFloat(o.capital_mount)) {
                            plus = "";
                            c = "color1da158";
                        }
                        html.push("<ul>");
                        html.push("<li>" + type + "<br/><em class='colorcfcfcf'>" + new Date().format(o.capital_create_time) + "</em></li>");
                        html.push("<li><em class='" + c + "'>" + plus + o.capital_mount + "</em><br/><em class='colorcfcfcf'></em></li>");
                        html.push("</ul>");
                    });
                    This.siblings(".list_suce_list").append(html.join(""));
                    $("#load").hide();
                });
                $("#p").attr("value", parseInt(p) + 1);
            }
        </script>

        <#include "Footer.ftl" />
    </body>
</html>
<#macro typeList types>
    <#list types as t>
        <li index="${t_index}" data-val="${t_index + 1}">${t}</li>
    </#list>
</#macro>

<#macro getCapitalType obj>
    <#if obj??>
        <#if (1 == obj)>充值
        <#elseif (2 == obj)>提现
        <#elseif (3 == obj)>红包费
        <#elseif (4 == obj)>红包费退回
        <#elseif (5 == obj)>发红包
        <#elseif (6 == obj)>阅读费
        <#elseif (7 == obj)>阅读费退回
        <#elseif (8 == obj)>阅读费收益
        <#elseif (9 == obj)>技术服务费
        <#else>Oops！
        </#if>
    </#if>
</#macro>

<#macro getNoteStatus obj>
    <#if obj??>
        <#if (0 == obj)>未开盘
        <#elseif (1 == obj)>进行中
        <#elseif (2 == obj)>已结束
        <#else>Oops！
        </#if>
    </#if>
</#macro>

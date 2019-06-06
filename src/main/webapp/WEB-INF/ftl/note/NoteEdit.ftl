<!doctype html>
<html lang="en">
    <head>
        <#include "Header.ftl" />
        <link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/createNote.css?version=${version}" />
    </head>

    <body>
        <p class="overf"></p>
        <div class="page_cont">
            <form action="${request.contextPath}/weixin/note/save" method="post" class="outMoney">
                <ul class="mg24">
                    <li class="text_info">
                        <label>股票</label>
                        <div class="txt">
                            <@s.formInput "obj.note_stock_code" "placeholder='输入6位股票代码' class='' maxLength='6' onKeyUp='this.value=this.value.replace(/\\D/g, \"\")' onAfterPaste='this.value=this.value.replace(/\\D/g, \"\")'" />
                        </div>
                    </li>
                    <li class="tip color2190e7" id="stockName">----</li>
                    <li class="text_info">
                        <label>预期</label>
                        <div class="txt">
                            <span class="upbtn"><a class="upInfo" index="0"><span class="day">2日</span><i class="arrow"></i></a></span>
                            <input type="hidden" name="note_target_day" id="dayNum" value="2" />
                            <span class="upbtn fSize">上涨<input type="text" placeholder="3-30" min="3" max="30" datatype="number" id="note_increase" name="note_increase" dialog_title="涨幅最小3,最大30"/>%</span>
                        </div>
                    </li>
                    <li class="tip colorb0b0b0"><span id="sp">起算日2016.2.28，截止3.1收盘</span></li>
                    <li class="text_info">
                        <label>标题</label>
                        <div class="txt"  max="12" min="0" dialog_title="0-12字，不付费可见">
                            <@s.formInput "obj.note_title" "placeholder='0-12字，不付费可见'" />
                        </div>
                    </li>
                    <li class="tip color2e54a5" id="stockName"></li>
                    <li class="text_info textarea_info">
                        <label>逻辑</label>
                        <div class="txt" max="140" min="5" dialog_title="5-140字，建议写上止损价">
                            <textarea placeholder="5-140字，建议写上止损价" id="note_remark" name="note_remark"></textarea>
                        </div>
                    </li>
				    <!--<li class="text_info">
						<label>&nbsp;</label>
						<div class="txt">
							<input type="radio" id="note_type0" class="ml30"  name="note_type"  value="0" /><label for="note_type0">&nbsp;免费笔记</label>
							<input type="radio" id="note_type1"  class="ml30"  name="note_type"  value="1" checked="true" /><label for="note_type1">&nbsp;付费查看</label>
						</div>
					</li>-->
                    <li class="text_info" target="note_type">
                        <label>标价</label>
                        <div class="txt" max="200" min="0" dialog_title="0-200元，整数金额" datatype="number">
                            <@s.formInput "obj.note_open_money" "placeholder='0-200元，整数金额' maxLength='6' onKeyUp='if(this.value==this.value2)return;if(this.value.search(/^\\d*(?:\\.\\d{0,2})?$/)==-1)this.value=(this.value2)?this.value2:\"\";else this.value2=this.value;if(\"\"==value)value=\"\"' onAfterPaste='this.value=this.value.replace(/\\D/g, \"\")'" />
                            <script>$("#note_open_money").val("");</script>
                        </div>
                    </li>
                    <li class="tip colorb0b0b0 readMoney" target="note_type">您同时需准备一等额红包，未达目标时分给读者</li>
                </ul>
                <div class="clearfix"> <span class="rule_check color2190e7" id="payBtn">查看规则</span></div>
                <p class="btnable"><a class="btn btn1" href="javascript:void(0);">发布</a></p>
                <#--<p class="tip_footer">信息服务费 = 总阅读费 * 5%，仅达到目标时收取</p>-->
                <@s.formHiddenInput "obj.note_id" />
                <@s.formHiddenInput "obj.note_user_id" />
                <@s.formHiddenInput "obj.note_stock_name" />
                <@s.formHiddenInput "obj.note_amount" />
                <@s.formHiddenInput "obj.note_status" />
                <@s.formHiddenInput "obj.note_target_status" />
                <@s.formHiddenInput "obj.note_start_time" />
                <@s.formHiddenInput "obj.note_end_time" />
                <@s.formHiddenInput "obj.note_create_time" />
            </form>
        </div>

        <!--下拉start-->
        <div class="sel_mask">
            <div class="on sel">
                <div class="title"><span>2日</span><i class="arrow"></i></div>
                <ul class="daySel" target=".day" target1="upSel" updateId="sp" targetVal="dayNum">
                    <li index="0" data-val="2">2日</li>
                    <li index="1" data-val="3">3日</li>
                    <li index="2" data-val="4">4日</li>
                    <li index="3" data-val="5">5日</li>
                    <li index="4" data-val="6">6日</li>
                    <li index="5" data-val="7">7日</li>
                    <li index="6" data-val="8">8日</li>
                    <li index="7" data-val="9">9日</li>
                    <li index="8" data-val="10">10日</li>
                    <li index="9" data-val="11">11日</li>
                    <li index="10" data-val="12">12日</li>
                    <li index="11" data-val="13">13日</li>
                    <li index="12" data-val="14">14日</li>
                    <li index="13" data-val="15">15日</li>
                    <li index="14" data-val="16">16日</li>
                    <li index="15" data-val="17">17日</li>
                    <li index="16" data-val="18">18日</li>
                    <li index="17" data-val="19">19日</li>
                    <li index="18" data-val="20">20日</li>
                </ul>
            </div>
            <!--<div class="sel">
                <div class="title"><span>预期涨幅</span><i class="arrow"></i></div>
                <ul class="upSel" target=".pro" targetVal="zf">
                    <li index="0" data-val="3">3%</li>
                    <li index="1" data-val="5">5%</li>
                    <li index="2" data-val="8">8%</li>
                    <li index="3" data-val="10">10%</li>
                    <li index="4" data-val="15">15%</li>
                    <li index="5" data-val="20">20%</li>
                    <li index="6" data-val="25">25%</li>
                    <li index="7" data-val="30">30%</li>
                </ul>
            </div>-->
        </div>
        <!--end-->
        <script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/createNote.js"></script>
        <script type="text/javascript">
        $(function() {
            <#if stockName??>$("#stockName").html("${stockName}");</#if>

            $("#note_stock_code").keyup(function () {
                if (6 == $(this).val().length) {
                    $.get("${request.contextPath}/weixin/note/getStock?code="+$(this).val(), function (data) {
                        $("#stockName").html(data);
                    });
                }
            });
        });
        </script>

        <#-- 弹出提示框 -->
		<#include "Dialog.ftl" />

        <#include "Footer.ftl" />
    </body>
</html>

package com.caimao.weixin.note.util.heepay.Common;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.caimao.weixin.note.util.HttpHelper;
import com.caimao.weixin.note.util.execption.BusinessException;
import com.caimao.weixin.note.util.heepay.HeepayEntity.BatchPayEntity;
import com.caimao.weixin.note.util.heepay.HeepayEntity.GatewayEntity;
import com.caimao.weixin.note.util.heepay.HeepayEntity.GatewayResEntity;

/**
 * 汇付宝签名合成相关的方法
 */
@Component
public class HeepayHelper {

	private static final Logger logger = LoggerFactory.getLogger(HeepayHelper.class);

	@Value("${heepayAgentId}")
	private String heepayAgentId;
	@Value("${heepayBatchKey}")
	private String heepayBatchKey;
	@Value("${heepayTixianKey}")
	private String heepayTixianKey;
	@Value("${heepayPaySyncUrl}")
	private String heepayPaySyncUrl;
	@Value("${heepayPayAsyncUrl}")
	private String heepayPayAsyncUrl;


	@Value("${heepayBatchNotifyUrl}")
	private String heepayBatchNotifyUrl;

	public String getHeepayAgentId() {
		return heepayAgentId;
	}

	public String getHeepayBatchKey() {
		return heepayBatchKey;
	}

	public String getHeepayBatchNotifyUrl() {
		return heepayBatchNotifyUrl;
	}

	public String getHeepayTixianKey() {
		return heepayTixianKey;
	}

	/**
	 * 填充一些基本信息
	 * @param gatewayEntity
     */
	public void fullGateway(GatewayEntity gatewayEntity) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String time = df.format(new Date());
		gatewayEntity.setVersion("1");
		gatewayEntity.setAgent_id(this.heepayAgentId);
		gatewayEntity.setIs_phone("1");
		gatewayEntity.setNotify_url(heepayPayAsyncUrl);
		gatewayEntity.setReturn_url(heepayPaySyncUrl);
		gatewayEntity.setAgent_bill_time(time);
		gatewayEntity.setGoods_name("投资笔记");
		gatewayEntity.setGoods_num("1");
		gatewayEntity.setRemark("TZBJ");
		gatewayEntity.setGoods_note("note");
	}

	/**
	 * 生成签名
	 * @param gatewayEntity
	 * @return
     */
	public String SignMd5(GatewayEntity gatewayEntity) {
		//注意拼接顺序,详情请看《汇付宝支付网关开发指南》3.4签名
		String str = String.format("version=%s&agent_id=%s&agent_bill_id=%s&agent_bill_time=%s&pay_type=%s&pay_amt=%s&notify_url=%s&return_url=%s&user_ip=%s&key=%s",
				gatewayEntity.getVersion(),
				gatewayEntity.getAgent_id(),
				gatewayEntity.getAgent_bill_id(),
				gatewayEntity.getAgent_bill_time(),
				gatewayEntity.getPay_type(),
				gatewayEntity.getPay_amt(),
				gatewayEntity.getNotify_url(),
				gatewayEntity.getReturn_url(),
				gatewayEntity.getUser_ip(),
				this.heepayBatchKey);
		return Md5Tools.MD5(str).toLowerCase();
	}

	/**
	 * 拼接成请求url并返回
	 * @param sign
	 * @param gatewayEntity
     * @return
     */
	public String GatewaySubmitUrl(String sign, GatewayEntity gatewayEntity) {
		StringBuilder sbURL = new StringBuilder();
		sbURL.append("https://pay.heepay.com/Payment/Index.aspx?");//此为测试地址，商户应使用文档中的正式地址
		sbURL.append("version=" + gatewayEntity.getVersion());
		sbURL.append("&is_phone=" + gatewayEntity.getIs_phone());
		if (gatewayEntity.getIs_frame() != null) sbURL.append("&is_frame=" + gatewayEntity.getIs_frame());
		sbURL.append("&pay_type=" + gatewayEntity.getPay_type());
		if (gatewayEntity.getPay_code() != null ) sbURL.append("&pay_code=" + gatewayEntity.getPay_code());
		sbURL.append("&agent_id=" + gatewayEntity.getAgent_id());
		sbURL.append("&agent_bill_id=" + gatewayEntity.getAgent_bill_id());
		sbURL.append("&pay_amt=" + gatewayEntity.getPay_amt());
		sbURL.append("&notify_url=" + gatewayEntity.getNotify_url());
		sbURL.append("&return_url=" + gatewayEntity.getReturn_url());
		sbURL.append("&user_ip=" + gatewayEntity.getUser_ip());
		sbURL.append("&agent_bill_time=" + gatewayEntity.getAgent_bill_time());
		sbURL.append("&goods_name=" + gatewayEntity.getGoods_name());
		sbURL.append("&goods_num=" + gatewayEntity.getGoods_num());
		sbURL.append("&remark=" + gatewayEntity.getRemark());
		sbURL.append("&goods_note=" + gatewayEntity.getGoods_note());
		sbURL.append("&sign=" + sign);
		return sbURL.toString();
	}

	//汇付宝查询构建签名
	public String SignQueryMd5(GatewayEntity gatewayEntity) {
		String _StringSign = ("version=" + gatewayEntity.getVersion()) +
				"&agent_id=" + gatewayEntity.getAgent_id() +
				"&agent_bill_id=" + gatewayEntity.getAgent_bill_id() +
				"&agent_bill_time=" + gatewayEntity.getAgent_bill_time() +
				"&return_mode=1" + //默认值
				"&key=" + this.heepayBatchKey;
		return Md5Tools.MD5(_StringSign).toLowerCase();
	}

	//汇付宝查询构建提交url地址
	@SuppressWarnings("deprecation")
	public String GatewaySubmitQuery(String sign, GatewayEntity gatewayEntity) {
		String sbURL = "version=1" +
				"&agent_id=" + gatewayEntity.getAgent_id() +
				"&agent_bill_id=" + gatewayEntity.getAgent_bill_id() +
				"&agent_bill_time=" + gatewayEntity.getAgent_bill_time() +
				"&remark=" + URLEncoder.encode(gatewayEntity.getRemark()) +
				"&return_mode=1" +
				"&sign=" + sign;
		return sbURL;
	}

	public boolean checkPayRes(String billId) throws Exception {
		boolean result = false;
		GatewayEntity gatewayEntity = new GatewayEntity();
		fullGateway(gatewayEntity);
		gatewayEntity.setAgent_bill_id(billId);
		
		String sign = SignQueryMd5(gatewayEntity);
		String returnString = GatewaySubmitQuery(sign, gatewayEntity);

		System.out.println("https://query.heepay.com/Payment/Query.aspx?" + returnString);

		String requestUrl = "https://query.heepay.com/Payment/Query.aspx";
		Map<String, Object> heepayBatchParams = new HashMap<>();
		heepayBatchParams.put("version", 1);
		heepayBatchParams.put("agent_id", gatewayEntity.getAgent_id());
		heepayBatchParams.put("agent_bill_id", gatewayEntity.getAgent_bill_id());
		heepayBatchParams.put("agent_bill_time", gatewayEntity.getAgent_bill_time());
		heepayBatchParams.put("remark", gatewayEntity.getRemark());
		heepayBatchParams.put("return_mode", "1");
		heepayBatchParams.put("sign", sign);
		String heepayReturnStr = HttpHelper.doPost(requestUrl, heepayBatchParams);

		if (heepayReturnStr.contains("result=1")) {
			result = true;
		}

		return result;
	}

	/**
	 * 验证返回的签名是否正确
	 * @param gatewayResEntity
	 * @return
     */
	public boolean VerifiSign(GatewayResEntity gatewayResEntity) {
		String _str = String.format("result=%s&agent_id=%s&jnet_bill_no=%s&agent_bill_id=%s&pay_type=%s&pay_amt=%s&remark=%s&key=%s",
				gatewayResEntity.getResult(), gatewayResEntity.getAgent_id(), gatewayResEntity.getJnet_bill_no(), gatewayResEntity.getAgent_bill_id(),
				gatewayResEntity.getPay_type(), gatewayResEntity.getPay_amt(), gatewayResEntity.getRemark(), this.heepayBatchKey);
		String mySign = Md5Tools.MD5(_str).toLowerCase();
		return mySign.equals(gatewayResEntity.getSign());
	}

	/**
	 * 批付提现接口
	 * @param batchPayEntity
	 * @return
     */
	public boolean batchPay(BatchPayEntity batchPayEntity) throws Exception {
		String _str = String.format(
				"agent_id=%s&batch_amt=%s&batch_no=%s&batch_num=%s&detail_data=%s&ext_param1=%s&key=%s&notify_url=%s&version=%s",
				this.heepayAgentId, batchPayEntity.getBatch_amt(), batchPayEntity.getBatch_no(), batchPayEntity.getBatch_num(),
				batchPayEntity.getDetail_data(), batchPayEntity.getExt_param1(), this.heepayTixianKey, this.getHeepayBatchNotifyUrl(), batchPayEntity.getVersion()
		);
		String sign = Md5Tools.MD5(_str.toLowerCase()).toLowerCase();
		batchPayEntity.setSign(sign);
		String requestUrl = "https://Pay.heepay.com/API/PayTransit/PayWithSmallAll.aspx";

		Map<String, Object> heepayBatchParams = new HashMap<>();
		heepayBatchParams.put("version", 2);
		heepayBatchParams.put("agent_id", batchPayEntity.getAgent_id());
		heepayBatchParams.put("batch_no", batchPayEntity.getBatch_no());
		heepayBatchParams.put("batch_amt", batchPayEntity.getBatch_amt());
		heepayBatchParams.put("batch_num", batchPayEntity.getBatch_num());
		heepayBatchParams.put("detail_data", batchPayEntity.getDetail_data());
		heepayBatchParams.put("notify_url", this.getHeepayBatchNotifyUrl());
		heepayBatchParams.put("ext_param1", batchPayEntity.getExt_param1());
		heepayBatchParams.put("sign", sign);
		String heepayReturnStr = HttpHelper.doPost(requestUrl, heepayBatchParams);

		Document document = null;
		try {
			document = DocumentHelper.parseText(heepayReturnStr);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw e;
		}
		Element rootElement = document.getRootElement();
		String retCode = rootElement.elementTextTrim("ret_code");
		if (!"0000".equals(retCode)) {
			throw new BusinessException("打款失败，原因：" + rootElement.elementTextTrim("ret_msg"), 888888);
		}
		return true;
	}

	/**
	 * 查询批付请求结果
	 * @param batchNo
	 * @return
	 * @throws Exception
     */
	public List<Map<String, Object>> checkHeepayBatchResult(String batchNo) throws Exception {
		Map<String, Object> heepayCheckParams = new HashMap<>();
		heepayCheckParams.put("version", 2);
		heepayCheckParams.put("agent_id", this.heepayAgentId);
		heepayCheckParams.put("batch_no", batchNo);
		String signStr = String.format("agent_id=%s&batch_no=%s&key=%s&version=%s",
				heepayCheckParams.get("agent_id"),
				heepayCheckParams.get("batch_no"),
				this.heepayTixianKey,
				heepayCheckParams.get("version")
		);
		heepayCheckParams.put("sign", Md5Tools.MD5(signStr.toLowerCase()).toLowerCase());

		logger.info("汇付宝批付结果查询请求参数：{}", heepayCheckParams);
		String heepayResultStr = HttpHelper.doPost("https://Pay.heepay.com/API/PayTransit/Query.aspx", heepayCheckParams);
		logger.info("汇付宝批付结果查询返回结果：{}", heepayResultStr);
		Document document = null;
		try {
			document = DocumentHelper.parseText(heepayResultStr);
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.error("解析XML失败！", e);
			throw new BusinessException("汇付宝解析XML失败！", 831410);
		}
		Element rootElement = document.getRootElement();
		if (!"0000".equals(rootElement.elementTextTrim("ret_code"))) {
			throw new BusinessException("汇付宝查询批付接口返回失败", 888888);
		}
		String detailData = rootElement.elementTextTrim("detail_data");
		if (detailData == null) {
			throw new BusinessException("汇付宝查询批付接口未返回 detail_data 字段", 888888);
		}
		List<Map<String, Object>> detailList = new ArrayList<>();
		String[] detailArr = detailData.split("\\|");
		for (String aDetailArr : detailArr) {
			Map<String, Object> map = new HashMap<>();
			String[] subDetailArr = aDetailArr.split("\\^");
			map.put("orderNo", subDetailArr[0]);
			map.put("bankCardNo", subDetailArr[1]);
			map.put("bankCardName", subDetailArr[2]);
			map.put("amount", subDetailArr[3]);
			map.put("status", subDetailArr[4]);
			detailList.add(map);
		}
		return detailList;

	}

	/**
	 * 生成订单ID，规则：年月日时分秒 + "N" + 笔记ID + "U" + 用户ID + "T" + 线程ID
	 * <br>
	 * 备注1：因提现没有笔记ID，默认为：0<br>
	 * 备注2：汇付宝最长支持50字符<br>
	 * 
	 * @return
	 */
	public String createOrderNo(Integer noteId, Integer userId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = df.format(new Date());
		return time + "N" + noteId + "U" + userId + "T" + Thread.currentThread().getId();
	}


}

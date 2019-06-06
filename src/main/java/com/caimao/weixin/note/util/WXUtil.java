package com.caimao.weixin.note.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.caimao.weixin.note.util.aes.SHA1;
import com.caimao.weixin.note.util.execption.CustomerException;

@Service
public class WXUtil {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WXUtil.class);

	@Value("${wx.token}")
	private String token;

	@Value("${wx.encodingAESKey}")
	private String encodingAESKey;

	@Value("${wx.appId}")
	private String appId;

	@Value("${wx.appSecret}")
	private String appSecret;

	@Value("${wx.domain}")
	private String domain;

	/** Redis缓存 openId 用户信息的时间 */
	public static Long redisCacheUserInfoExpires = 300L;

	@Autowired
	private RedisUtil redisUtil;

	/** 根据授权返回的code，获取用户的 accessToken 与 openId，并且保存到session中，进行记录 */
	public void saveAccessTokenAndOpenIdByCode(String code, String scope) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, String> resMap = this.getWebAccessToken(code);
		if (null != resMap && null != resMap.get("access_token")) {
			request.getSession().setAttribute("accessToken" + scope, resMap.get("access_token"));
			request.getSession().setAttribute("openId" + scope, resMap.get("openid"));
		}
	}

	/** 如果当前页面需要获取用户的基本信息，那就需要调用这个方法获取网页的accessToken，从session中获取当前访问用户的信息 */
	public Map<String, String> verificationAccessToken(String scope) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		Object accessToken = request.getSession().getAttribute("accessToken" + scope);
		Object openId = request.getSession().getAttribute("openId" + scope);
		if (null == accessToken || null == openId) {
			// 没有获取授权，需要跳转到专门的页面进行跳转
			String authUrl = this.createdAuthUrl(this.getNowUrl(true), scope, scope);
			response.sendRedirect(authUrl);
			return null;
		}
		Map<String, String> resMap = new TreeMap<>();
		resMap.put("accessToken", accessToken.toString());
		resMap.put("openId", openId.toString());
		return resMap;
	}

	// 获取用户当前访问网址链接
	public String getNowUrl(Boolean clear) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String url = request.getRequestURL().toString();
		if (request.getQueryString() != null && !Objects.equals(request.getQueryString(), "")) {
			url += "?" + request.getQueryString();
		}
		if (clear) {
			// 删除链接中的code与 state 参数
			String codeRegex = "[?&]{1}code=[0-9a-zA-Z]{32}";
			String stateRegex = "[?&]{1}state=[a-zA-Z_]{11,15}";
			url = url.replaceAll(codeRegex, "");
			url = url.replaceAll(stateRegex, "");
		}
		return url;
	}

	/**
	 * 创建授权的跳转链接
	 * @param redirectUrl
	 * @param scope
	 * @param state
	 * @return
	 */
	public String createdAuthUrl(String redirectUrl, String scope, String state) throws Exception {
		return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", appId, URLEncoder.encode(redirectUrl, "UTF-8"), scope, state);
	}

	/**
	 * 获取accessToken
	 * @return
	 * @throws Exception
	 */
	public String getAccessToken() throws Exception {
		Object accessTokenObj = this.redisUtil.get("wx_note_access_token");
		if (accessTokenObj != null) {
			LOGGER.info("获取accessToken，redis直接返回 ：{}", accessTokenObj.toString());
			return accessTokenObj.toString();
		}
		String accessToken = null;
		String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", this.appId, this.appSecret);
		String httpRes = HttpHelper.doGet(url);
		LOGGER.info("获取accessToken，http返回json：{}", httpRes);
		Map<String, String> resMap = WXMessageUtil.parseJson(httpRes);
		LOGGER.info("json结息返回的map值：{}", resMap);
		if (resMap == null) {
			throw new RuntimeException("获取accessToken错误");
		}
		accessToken = resMap.get("access_token");
		Long expires = Long.valueOf(resMap.get("expires_in"));
		this.redisUtil.set("wx_note_access_token", accessToken, expires - 200);
		return accessToken;
	}


	/**
	 * 网页授权的accessToken获取
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getWebAccessToken(String code) throws Exception {
		String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", this.appId, this.appSecret, code);
		String httpRes = HttpHelper.doGet(url);
		LOGGER.info("根据code获取accessToken返回值：{}", httpRes);
		return WXMessageUtil.parseJson(httpRes);
	}

	/**
	 * 获取用户信息
	 * @param openId
	 * @return
	 */
	public Map<String, String> getUserInfoByOpenId(String openId) throws Exception {
		String redisKey = "wx-" + openId;
		// redis进行缓存
		Object httpRes = this.redisUtil.get(redisKey);
		if (httpRes == null) {
			String url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", this.getAccessToken(), openId);
			httpRes = HttpHelper.doGet(url);
			try {
				// 验证返回值是否正确，错误的话会抛出异常，正确的话，加到redis cache中
				WXMessageUtil.verifiedResults(httpRes.toString());
				this.redisUtil.set(redisKey, JSON.toJSONString(httpRes.toString(), SerializerFeature.BrowserCompatible), redisCacheUserInfoExpires);
			} catch (Exception e) {
				LOGGER.error("获取微信用户信息错误 {}", e);
				this.redisUtil.del("wx_note_access_token");
				return null;
			}
		} else {
			httpRes = JSON.parseObject(httpRes.toString(), String.class);
		}
		LOGGER.info("根据openId获取userInfo返回值：{}", httpRes.toString());
		return WXMessageUtil.parseJson(httpRes.toString());
	}

	/**
	 * 获取web用户信息
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	public Map<String, String> getWebUserInfoByOpenId(String accessToken, String openId, String scope) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		String redisKey = "web-" + openId;
		// redis进行缓存
		Object httpRes = this.redisUtil.get(redisKey);
		if (httpRes == null) {
			String url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN", accessToken, openId);
			httpRes = HttpHelper.doGet(url);
			try {
				// 验证返回值是否正确，错误的话会抛出异常，正确的话，加到redis cache中
				WXMessageUtil.verifiedResults(httpRes.toString());
				this.redisUtil.set(redisKey, JSON.toJSONString(httpRes.toString(), SerializerFeature.BrowserCompatible), redisCacheUserInfoExpires);
			} catch (Exception e) {
				LOGGER.error("获取微信web用户信息错误 {}", e);
				// accessToken 失效了，重新获取 TODO
				request.getSession().removeAttribute("accessToken" + scope);
				this.verificationAccessToken(scope);
				return null;
			}
		} else {
			httpRes = JSON.parseObject(httpRes.toString(), String.class);
		}

		LOGGER.info("根据web openId获取userInfo返回值：{}", httpRes.toString());
		Map<String, String> ret = WXMessageUtil.parseJson(httpRes.toString());
		if (ret == null || ret.get("nickname") == null) {
			this.redisUtil.del(redisKey);
		}
		return ret;
	}


	/**
	 * 获取JSAPI TICKET 那些信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getJsApiTicket(String reqUrl) throws Exception {
		Object jsApiTicket = this.redisUtil.get("wx_note_js_api_ticket");
		if (jsApiTicket == null) {
			// 重新获取ticket
			String apiUrl = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi", this.getAccessToken());
			String httpRes = HttpHelper.doGet(apiUrl);
			LOGGER.info("获取JSAPI Ticket http返回信息：{}", httpRes);
			Map<String, String> resMap = WXMessageUtil.parseJson(httpRes);
			if (resMap == null) {
				throw new CustomerException("获取jsApiTicket失败", 888888);
			}
			jsApiTicket = resMap.get("ticket");
			Long expires = Long.valueOf(resMap.get("expires_in"));
			this.redisUtil.set("wx_note_js_api_ticket", jsApiTicket.toString(), expires - 200);
		}
        String noncestr = UUID.randomUUID().toString();;
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		//String reqUrl = this.request.getRequestURL().toString();
		if (reqUrl.contains("#")) {
			reqUrl = reqUrl.substring(0, reqUrl.indexOf("#"));
		}
		String sha1Str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", jsApiTicket.toString(), noncestr, timestamp, reqUrl);
		LOGGER.info("JSSDK signature STR : {}", sha1Str);
		String signature = SHA1.getSHA1(sha1Str);

		Map<String, Object> ticketInfo = new TreeMap<>();
		ticketInfo.put("appId", this.appId);
		ticketInfo.put("timestamp", timestamp);
		ticketInfo.put("nonceStr", noncestr);
		ticketInfo.put("signature", signature);

		LOGGER.info("JSTicket 返回的信息 ：{}", ticketInfo);

		return ticketInfo;
	}

	/**
	 * 发送模板消息
	 * 
	 * @param noteStatus - 笔记状态
	 * @param noteCreateTime - 笔记创建时间
	 * @param message - 发送的消息主体
	 * @param toOpenId - 给谁发
	 * @param type - 模板类型
	 * @param jumpUrl - 点击后的地址
	 * @return 成功 - true，失败 - false
	 */
	public void sendTemplateMsg(String noteStatus, String noteCreateTime, String message, String toOpenId, String type, String jumpUrl) throws Exception {
		Map<String, TemplateData> map = new HashMap<>();
		TemplateData first = new TemplateData();
		first.setColor("#091960");
		first.setValue("个人消息通知");
		map.put("first", first);

		TemplateData name = new TemplateData();
		name.setColor("#091960");
		name.setValue("财猫投资笔记");
		map.put("HandleType", name);

		TemplateData status = new TemplateData();
		status.setColor("#091960");
		status.setValue(noteStatus);
		map.put("Status", status);

		TemplateData date = new TemplateData();
		date.setColor("#091960");
		date.setValue("笔记发布于：" + noteCreateTime);
		map.put("RowCreateDate", date);

		TemplateData logType = new TemplateData();
		logType.setColor("#091960");
		logType.setValue(message);
		map.put("LogType", logType);

		String apiUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", getAccessToken());
		Map<String, Object> reqMap = new TreeMap<>();
		reqMap.put("touser", toOpenId);
		reqMap.put("template_id", type);
		reqMap.put("url", jumpUrl);
		reqMap.put("data", map);
		String reqJson = WXMessageUtil.mapToJson(reqMap);
		String httpRes = HttpHelper.doPostSSL(apiUrl, reqJson);
		try {
			if (WXMessageUtil.verifiedResults(httpRes)) {
				LOGGER.info("【微信返回：{}，给{}推送消息成功！！】", httpRes, toOpenId);
			}
		} catch (Exception e) {
			LOGGER.error("【微信返回：{}，给{}推送消息失败！！】", httpRes, toOpenId);
		}
		// return WXMessageUtil.verifiedResults(httpRes);
	}

}

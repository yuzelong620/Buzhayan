package com.kratos.game.herphone.sms.service;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.sms.entity.SmsEntity;

import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j;

/**
 * 短信服务
 *
 */
@Log4j
@Component
public class SmsService extends BaseService {

	static final String appId = "LTAI4FcrmkGxYyVbhaQkw2Zk";
	static final String appSecret = "JSXGN9kDP3AznnF64fHICsJoFWlW06";
	static final String serverUrl = "http://gw.api.taobao.com/router/rest";
	static final String smsFreeSignName = "不眨眼";
	static final String smsType = "normal";
	static final String smsTemplateCode = "SMS_147970129";
	static final String overseasSmsTemplateCode = "SMS_152208641";
	static final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
	static final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）

	static final String MINUTE_LIMIT = "isv.BUSINESS_LIMIT_CONTROL";
	static final String CHINA_REGION = "86";
	static final String CACHE_NAME = "verify_code";

	public boolean sendVerifyCode(String mobile, String areaCode) {
		String verifyCode = generateVerifyCode();
		long validTime = System.currentTimeMillis() + 1000L * 60 ;// 有效时间
		boolean result = false;
		try {
			if (CHINA_REGION.equals(areaCode) || StringUtil.isNullOrEmpty(areaCode)) {
				if (send(mobile, verifyCode)) {
					SmsEntity smsentity = new SmsEntity(mobile, verifyCode, validTime);
					smsDao.save(smsentity);
					result = true;
				}
			} else {
				mobile = areaCode + mobile;
				if (sendOverseas(mobile, verifyCode)) {
					SmsEntity smsentity = new SmsEntity(areaCode + mobile, verifyCode, validTime);
					smsDao.save(smsentity);
					result = true;
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return result;

	}

	public Boolean validateVerifyCode(String mobile, String areaCode, String verifyCode) {

		String jedisVerifyCode = "";
		long validTime = 0;
		if (CHINA_REGION.equals(areaCode) || StringUtil.isNullOrEmpty(areaCode)) {
			SmsEntity entity = smsDao.findByID(mobile);
			if(entity==null){
				throw new BusinessException("手机号不存在");
			}
			validTime = entity.getValidTime();
			if (entity != null) {
				jedisVerifyCode = entity.getCode();
			}
		} else {
			SmsEntity entity = smsDao.findByID(areaCode + mobile);
			validTime = entity.getValidTime();
			if (entity != null) {
				jedisVerifyCode = entity.getCode();
			}
		}

		if (jedisVerifyCode != null) {
			long nowTime = System.currentTimeMillis();
			if (nowTime > validTime) {
				return false;
			}
			return verifyCode.equals(jedisVerifyCode);
		}
		return false;
	}

	/**
	 * 生成短信验证码
	 *
	 * @return 验证码
	 */
	private String generateVerifyCode() {
		Random r = new Random();
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			code.append(r.nextInt(10));
		}
		return code.toString();
	}

	public boolean send(String mobile, String verifyCode) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("\n 短信 配置:smsType=" + smsType);
		sb.append("\n smsFreeSignName=" + smsFreeSignName);
		sb.append("\n smsTemplateCode=" + smsTemplateCode);
		sb.append("\n serverUrl=" + serverUrl);
		sb.append("\n appId=" + appId);
		sb.append("\n appSecret=" + appSecret);
		sb.append("\n 发送短信 dest:" + mobile + " code:" + verifyCode);
       log.info(sb.toString()); 
		SendSmsRequest request = getSmsRequest(mobile, verifyCode);
		IAcsClient acsClient = getAcsClient();
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			// 请求成功
			return true;
		} else if (MINUTE_LIMIT.equals(sendSmsResponse.getCode())) {
			// throw new BusinessException("验证码请求过于频繁,请稍后再试");
		} else {
			// throw new IllegalArgumentException("短信发送失败请联系管理员");
		}
		return false;
	}

	/**
	 * @param mobile
	 *            （mobile 是 区号+手机号！areaCode + mobile）
	 * @param verifyCode
	 * @return
	 * @throws Exception
	 */
	public boolean sendOverseas(String mobile, String verifyCode) throws Exception {
		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		IAcsClient acsClient = getAcsClient();
		SendSmsRequest request = getSmsRequest(mobile, verifyCode);
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			return true;
		} else if (MINUTE_LIMIT.equals(sendSmsResponse.getCode())) {
			// throw new BusinessException("验证码请求过于频繁,请稍后再试");
		}
		return false;
		// throw new IllegalArgumentException("短信发送失败请联系管理员");
	}

	// ------------------------------------------------------------------------------------------
	private IAcsClient getAcsClient() throws ClientException {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appId, appSecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		return acsClient;
	}

	private SendSmsRequest getSmsRequest(String mobile, String verifyCode) {
		// 准备参数
		SendSmsRequest request = new SendSmsRequest();
		request.setMethod(MethodType.POST);
		request.setPhoneNumbers(mobile);
		request.setSignName(smsFreeSignName);
		request.setTemplateCode(smsTemplateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\"" + verifyCode + "\"}");
		return request;
	}

	public static void main(String[] args) throws Exception {
		String mobile = "13940517130";
		SmsService smsService = new SmsService();
		smsService.sendVerifyCode(mobile, "");
	}
}

package com.kratos.game.herphone.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @version 1.0
 * @ClassName: HttpClientUtils
 * @Description: HttpClient工具类(这里用一句话描述这个类的作用)
 * @date 2019/2/21 17:20
 */
public class HttpClientUtils {

	// 默认字符集
	private static String encoding = "utf-8";

	// 日志处理
	private static Logger log = Logger.getLogger(HttpClientUtils.class);

	/**
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求头
	 * @param data
	 *            请求实体
	 * @param encoding
	 *            字符集
	 * @return String
	 * @throws @Title:
	 *             sendPost
	 * @Description: TODO(发送post请求)
	 * @author wangxy
	 * @date 2018年5月10日 下午4:36:17
	 */
	public static String sendPost(String url, Map<String, String> headers, JSONObject data, String encoding) {
		log.info("进入post请求方法...");
		log.info("请求入参：URL= " + url);
		log.info("请求入参：headers=" + JSON.toJSONString(headers));
		log.info("请求入参：data=" + JSON.toJSONString(data));
		// 创建Client
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建HttpPost对象
		HttpPost httpPost = new HttpPost();
		try {
			// 设置请求地址
			httpPost.setURI(new URI(url));
			// 设置请求头
			if (headers != null) {
				Header[] allHeader = new BasicHeader[headers.size()];
				int i = 0;
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					allHeader[i] = new BasicHeader(entry.getKey(), entry.getValue());
					i++;
				}
				httpPost.setHeaders(allHeader);
			}
			// 设置实体
			httpPost.setEntity(new StringEntity(JSON.toJSONString(data)));
			// 发送请求,返回响应对象
			CloseableHttpResponse response = client.execute(httpPost);
			return parseData(response);

		} catch (Exception e) {
			log.error("发送post请求失败", e);
		} finally {
			httpPost.releaseConnection();
		}
		return null;
	}

	/**
	 * @param url
	 *            请求地址
	 * @param data
	 *            请求实体
	 * @return String
	 * @throws @Title:
	 *             sendPost
	 * @Description: TODO(发送post请求 ， 请求数据默认使用json格式 ， 默认使用UTF - 8编码)
	 * @author wangxy
	 * @date 2018年5月10日 下午4:37:28
	 */
	public static String sendPost(String url, JSONObject data) {
		// 设置默认请求头
		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application/json");

		return sendPost(url, headers, data, encoding);
	}

	/**
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求实体
	 * @return String
	 * @throws @Title:
	 *             sendPost
	 * @Description: TODO(发送post请求 ， 请求数据默认使用json格式 ， 默认使用UTF - 8编码)
	 * @author wangxy
	 * @date 2018年5月10日 下午6:11:05
	 */
	public static String sendPost(String url, Map<String, Object> params) {
		// 设置默认请求头
		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application/json");
		// 将map转成json
		JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
		return sendPost(url, headers, data, encoding);
	}

	/**
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求头
	 * @param data
	 *            请求实体
	 * @return String
	 * @throws @Title:
	 *             sendPost
	 * @Description: TODO(发送post请求 ， 请求数据默认使用UTF - 8编码)
	 * @author wangxy
	 * @date 2018年5月10日 下午4:39:03
	 */
	public static String sendPost(String url, Map<String, String> headers, JSONObject data) {
		return sendPost(url, headers, data, encoding);
	}

	/**
	 * @param url
	 *            请求地址
	 * @param headers
	 *            请求头
	 * @param params
	 *            请求实体
	 * @return String
	 * @throws @Title:
	 *             sendPost
	 * @Description:(发送post请求，请求数据默认使用UTF-8编码)
	 * @author wangxy
	 * @date 2018年5月10日 下午5:58:40
	 */
	public static String sendPost(String url, Map<String, String> headers, Map<String, String> params) {
		// 将map转成json
		JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
		return sendPost(url, headers, data, encoding);
	}

	/**
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码
	 * @return String
	 * @throws @Title:
	 *             sendGet
	 * @Description: TODO(发送get请求)
	 * @author wangxy
	 * @date 2018年5月14日 下午2:39:01
	 */
	public static String sendGet(String url, Map<String, Object> params, String encoding) {
//		log.info("进入get请求方法!请求入参：URL= " + url+"!请求入参：params=" + JSON.toJSONString(params));
		// 创建client
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建HttpGet
		HttpGet httpGet = new HttpGet();
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			// 封装参数
			if (params != null) {
				for (String key : params.keySet()) {
					builder.addParameter(key, params.get(key).toString());
				}
			}
			URI uri = builder.build();
//			log.info("请求地址：" + uri);
			// 设置请求地址
			httpGet.setURI(uri);
			// 发送请求，返回响应对象
			CloseableHttpResponse response = client.execute(httpGet);
			return parseData(response);
		} catch (Exception e) {
			log.error("发送get请求失败:"+url);
		} finally {
			httpGet.releaseConnection();
		}
		return null;
	}

	/**
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return String
	 * @throws @Title:
	 *             sendGet
	 * @Description: TODO(发送get请求)
	 * @author wangxy
	 * @date 2018年5月14日 下午2:32:39
	 */
	public static String sendGet(String url, Map<String, Object> params) {
		return sendGet(url, params, encoding);
	}

	/**
	 * @param url
	 *            请求地址
	 * @return String
	 * @throws @Title:
	 *             sendGet
	 * @Description: TODO(发送get请求)
	 * @author wangxy
	 * @date 2018年5月14日 下午2:33:45
	 */
	public static String sendGet(String url) {
		return sendGet(url, null, encoding);
	}

	/**
	 * 解析response
	 *
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static String parseData(CloseableHttpResponse response) throws Exception {
		// 获取响应状态
		int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			// 获取响应数据
			return EntityUtils.toString(response.getEntity(), encoding);
		} else {
			log.error("响应失败，状态码：" + status);
		}
		return null;
	}
	
	public static void main(String[] args) {
		JSONObject params=new JSONObject();
		params.put("userId", "123");
		
		sendPost("http://192.168.1.74:8080/Comic/qpp/comic/get/banner.do",params);
		System.out.println("發送完成");
	}

}

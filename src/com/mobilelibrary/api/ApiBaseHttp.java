package com.mobilelibrary.api;

import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import example.mobilelibrary.entity.D;

/**
 * 网络请求
 * 
 */
public class ApiBaseHttp {
	
	public static String HOST = "http://elib.njutcm.edu.cn:8080/";
//	public static String HOST = "http://192.168.1.107:8889/";

	/**
	 * 
	 * get 请求
	 * 
	 * @param URL
	 * @return
	 * @throws Exception
	 */
	public static String getURL(String url) throws Exception {
		String URL = HOST + "/" + url;
		HttpGet request = new HttpGet(URL);
		String strResult = null;
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			HttpResponse httpResponse = defaultHttpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
			defaultHttpClient.getConnectionManager().shutdown();
		} catch (SocketTimeoutException e) {
			throw new Exception("连接超时，请稍候重试");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return strResult;
	}

	/**
	 * 
	 * post 请求
	 * 
	 * @param URL
	 * @param paramNames
	 * @param paramValues
	 * @return
	 * @throws Exception
	 */
	/**1、创建HttpPost实例，设置需要请求服务器的url。

	2、为创建的HttpPost实例设置参数，参数设置时使用键值对的方式用到NameValuePair类。

	3、发起post请求获取返回实例HttpResponse

	4、使用EntityUtils对返回值的实体进行处理（可以取得返回的字符串，也可以取得返回的byte数组）*/
	
	public static String doPost(String url, String[] paramNames,
			String[] paramValues) throws Exception {
		String URL = HOST + url;
		// 打印
		D.out("ApiBaseHttp.doPost.URL>>>>" + URL);
		//输出参数值和参数名
		String pString = "";
		
		for (int i = 0; i < paramValues.length; i++) {
			pString += paramNames[i] + "=" + URLEncoder.encode(paramValues[i]) + ",";
		}
		D.out("ApiBaseHttp.doPost.params>>>" + pString);

		// http请求
		HttpPost post = new HttpPost(URL);
		//如果Post提交的是中文字符，需要加上相应的编码格式
		post.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		
		//  Post运作传送变数必须用NameValuePair[]阵列储存
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		for (int i = 0; i < paramNames.length; i++) {
			param.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
		}
		//  //设置httpPost请求参数  发出HTTP request
		post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));

		// 发送请求，获取响应结果
		String strResult = null;
		try {
			//使用execute方法发送HTTP GET请求，并返回HttpResponse对象
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			HttpResponse httpResponse = defaultHttpClient.execute(post);
			System.out.println("getStatusCode()="+httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
			defaultHttpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return strResult;
	}
}

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
 * ��������
 * 
 */
public class ApiBaseHttp {
	
	public static String HOST = "http://elib.njutcm.edu.cn:8080/";
//	public static String HOST = "http://192.168.1.107:8889/";

	/**
	 * 
	 * get ����
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
			throw new Exception("���ӳ�ʱ�����Ժ�����");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return strResult;
	}

	/**
	 * 
	 * post ����
	 * 
	 * @param URL
	 * @param paramNames
	 * @param paramValues
	 * @return
	 * @throws Exception
	 */
	/**1������HttpPostʵ����������Ҫ�����������url��

	2��Ϊ������HttpPostʵ�����ò�������������ʱʹ�ü�ֵ�Եķ�ʽ�õ�NameValuePair�ࡣ

	3������post�����ȡ����ʵ��HttpResponse

	4��ʹ��EntityUtils�Է���ֵ��ʵ����д�������ȡ�÷��ص��ַ�����Ҳ����ȡ�÷��ص�byte���飩*/
	
	public static String doPost(String url, String[] paramNames,
			String[] paramValues) throws Exception {
		String URL = HOST + url;
		// ��ӡ
		D.out("ApiBaseHttp.doPost.URL>>>>" + URL);
		//�������ֵ�Ͳ�����
		String pString = "";
		
		for (int i = 0; i < paramValues.length; i++) {
			pString += paramNames[i] + "=" + URLEncoder.encode(paramValues[i]) + ",";
		}
		D.out("ApiBaseHttp.doPost.params>>>" + pString);

		// http����
		HttpPost post = new HttpPost(URL);
		//���Post�ύ���������ַ�����Ҫ������Ӧ�ı����ʽ
		post.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		
		//  Post�������ͱ���������NameValuePair[]���д���
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		for (int i = 0; i < paramNames.length; i++) {
			param.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
		}
		//  //����httpPost�������  ����HTTP request
		post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));

		// �������󣬻�ȡ��Ӧ���
		String strResult = null;
		try {
			//ʹ��execute��������HTTP GET���󣬲�����HttpResponse����
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

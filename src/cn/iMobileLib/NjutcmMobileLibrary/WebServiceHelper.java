package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Xml;
import example.mobilelibrary.entity.BookReservationApplication;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.NewsBean;
import example.mobilelibrary.entity.NewsDetails;

public class WebServiceHelper {
	// ������ͨ��������������������ӵĳ�ʱʱ��
	private static final int TIMEOUT_CONNECTION = 32000;
	// ����Socket�����ݵĳ�ʱʱ�䣬���ӷ�������ȡ��Ӧ������Ҫ�ȴ���ʱ��
	private static final int TIMEOUT_SOCKET = 32000;

	private static HttpParams httpParameters = new BasicHttpParams();
	public static final String SERVER_URL = "http://elib.njutcm.edu.cn:8080/newsService.asmx/";
	public static final String LINK = "http://elib.njutcm.edu.cn:5001/newsService.asmx/";
	//public static final String LINK = "http://210.45.60.102:8888/newsService.asmx/";
	public static final String GETIMAGEINFO = "http://elib.njutcm.edu.cn:8081/getInitInfo.asmx/getLastImageInfo?";
	public static final String GETINITINFO = "http://elib.njutcm.edu.cn:8081/getInitInfo.asmx/";

	/**
	 * �жϵ�ǰ�����Ƿ�����������
	 * 
	 * @param act
	 * @return
	 */
	// ��ȡ��ҳ��Ӧ��Ϣ
	public static String getURL(String URL) throws Exception {

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
		HttpGet request = new HttpGet(URL);
		String strResult = null;
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
					httpParameters);
			HttpResponse httpResponse = defaultHttpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == 200) { // ����200��ʾ�ɹ�
				strResult = EntityUtils.toString(httpResponse.getEntity());
				// strResult = new String(strResult.getBytes("ISO-8859-1"),
				// "UTF-8");
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
	public static String doPost(String URL, String[] paramNames,
			String[] paramValues) throws Exception {

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
		HttpPost post = new HttpPost(URL);
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		for (int i = 0; i < paramNames.length; i++) {
			param.add(new BasicNameValuePair(paramNames[i], paramValues[i]));
		}
		post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));

		String strResult = null;
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
					httpParameters);
			HttpResponse httpResponse = defaultHttpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				// strResult = new String(strResult.getBytes("ISO-8859-1"),
				// "UTF-8");
			}
			defaultHttpClient.getConnectionManager().shutdown();
			log(URL);
			log(strResult);
		} catch (SocketTimeoutException e) {
			throw new Exception("���ӳ�ʱ�����Ժ�����");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return strResult;
	}

	public static void log(String i) {
		Log.i("API", i);
	}

	public static boolean getNetWorkInfo(Context context) {
		try {

			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getApplicationContext().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			if (connectivityManager == null) {
				return false;
			}
			NetworkInfo networkinfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkinfo == null || !networkinfo.isAvailable()
					|| !networkinfo.isConnectedOrConnecting()) {
				return false;
			} else { // ������������ӣ����ж�һ���Ƿ��������������
				if (openUrl()) { // ���������ҳ����������
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean openUrl() {
		String requestResult = "";
		try {
			URL url = new URL("http://www.baidu.com/index.html");
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(3500);
			InputStream is = urlConnection.getInputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					is);
			// ��ByteArrayBuffer����
			ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bufferedInputStream.read()) != -1) {
				byteArrayBuffer.append((byte) current);
			}
			requestResult = EncodingUtils.getString(
					byteArrayBuffer.toByteArray(), "UTF-8");
			bufferedInputStream.close();
			is.close();
			Boolean contain = requestResult.contains("�ٶ�");
			return contain;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ��ȡͼ������� �����ݿ��ȡ����
	 * */
	public static List<NewsBean> searchNews(G.NewsType type, int page)
			throws Exception {
		String URL = LINK;
		//String socialUrl = LINK;
		String typeStr = "";
		switch (type) {
		case SCHOOL:
			URL += "getSchoolNews?page=" + page;
			//ͷ��ǩ
			typeStr = "schoolNews";
			break;
		case LIBRARY:
			URL += "getLibNews?page=" + page;
			//ͷ��ǩ
			typeStr = "libraryNews";
			break;
		/*case SOCIAL:
			URL = socialUrl + "getSocialNews?page=" + page;
			typeStr = "socialNews";
			break;*/

		default:
			break;
		}
		System.out.println("----->>>url=" + URL);
		String ret = "";
		ret = getURL(URL);
		System.out.println("----ret>>>" + ret);

		List<NewsBean> items = new ArrayList<NewsBean>();
		NewsBean item = null;
		XmlPullParser xmlParser = Xml.newPullParser();
		xmlParser.setInput(new StringReader(ret));
		int evtType = xmlParser.getEventType();
		while (evtType != XmlPullParser.END_DOCUMENT) {
			switch (evtType) {
			case XmlPullParser.START_TAG:// ��ǩ��ʼ
				String tag = xmlParser.getName();
				// һ����¼
				String txt = "";
				if (tag.equalsIgnoreCase(typeStr)) {
					item = new NewsBean();
				} else if (tag.matches("Title|title")) {
					System.out.println("�������������"+txt);
					txt = xmlParser.nextText();
					item.setTitle(txt);
				} else if (tag.matches("PubDate|time|pubdate")) {
					System.out.println("�������������"+txt);
					txt = xmlParser.nextText();
					item.setDate(txt);
				} else if (tag.matches("NewsID|link|href")) {
					System.out.println("�������������"+txt);
					txt = xmlParser.nextText();
					item.setLink(txt);
					
				} else if (tag.equalsIgnoreCase("Abstract")) {
					System.out.println("�������������"+txt);
					txt = xmlParser.nextText();
					item.setContent(txt);
					
				}
				break;

			case XmlPullParser.END_TAG:// ��ǩ����
				if (xmlParser.getName().equalsIgnoreCase(typeStr)
						&& item != null) {
					items.add(item);
					item = null;
				}
				break;
			default:
				break;
			}
			evtType = xmlParser.next();
		}
		return items;
	}

	// ��������XML�ĵ�
	public static NewsDetails pullParseXmlDetails(G.NewsType type, String link)
			throws Exception {
		String URL = LINK;
		@SuppressWarnings("unused")
		String typeStr = "";
		@SuppressWarnings("unused")
		String paramKey = "";
		switch (type) {
		case SCHOOL:
			URL += "getSchoolNewsDetail?urlDetail=";
			typeStr = "SchoolNewsDetail";
			paramKey = "id";
			break;
		case LIBRARY:
			URL += "getLibNewsDetail?urlDetail=";
			typeStr = "libraryNewsDetail";
			paramKey = "id";
			break;
		case SOCIAL:
			URL += "getSocialNewsDetail";
			typeStr = "socialNewsDetail";
			paramKey = "url";
			break;

		default:
			break;
		}
		link= URLEncoder.encode(link, "utf-8");
		System.out.println("url:"+URL+link);
		String ret = getURL(URL+link);
		System.out.println("------->>GET ret=" + ret);
		NewsDetails news = new NewsDetails();
		if(ret.contains("SchoolNewsDetail")){
			news.title=ret.split("title")[1].replace(">", "").replace("</", "").replace("&nbsp;", "");
			news.pubdate=ret.split("pubdate")[1].replace(">", "").replace("</", "");
			news.content=ret.split("content")[1].replace(">", "").replace("</", "").replace("&nbsp;", "").replace("&mdash;", "");
			news.pageview=ret.split("count")[1].replace(">", "").replace("</", "");
		}else  {
			news.title=ret.split("title")[1].replace(">", "").replace("</", "").replace("&nbsp;", "");
			news.pubdate=ret.split("pubdate")[1].replace(">", "").replace("</", "");
			news.content=ret.split("content")[1].replace(">", "").replace("</", "").replace("&nbsp;", "").replace("&mdash;", "");
			news.pageview=ret.split("count")[1].replace(">", "").replace("</", "");
		}
		/*XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(ret));
		int eventType = parser.getEventType();
		System.out.println("��ʼeventType="+eventType);
		System.out.println("XmlPullParser.END_DOCUMENT="+XmlPullParser.END_DOCUMENT);
		while (eventType != XmlPullParser.END_DOCUMENT) {
			System.out.println("eventType="+eventType);
			if (eventType == XmlPullParser.START_DOCUMENT) {
				// sb.append("**********�ĵ���ʼ");
			} else if (eventType == XmlPullParser.START_TAG) {
				String tag = parser.getName();
				System.out.println("XmlPullParser.START_TAG="+XmlPullParser.START_TAG);
				System.out.println("tag="+tag);
				if (tag.equalsIgnoreCase(typeStr)) {
					System.out.println(typeStr+"�½�����ʵ��");
					news = new NewsDetails();
				} else if (tag.matches("title")) {
					System.out.println("-------------1");
					System.out.println("�������������"+parser.nextText());
					news.title = parser.nextText();
				} else if (tag.matches("pubdate")) {
					System.out.println("-------------2");
					System.out.println("�������������"+parser.nextText());
					news.pubdate = parser.nextText();
				} else if (tag.matches("pageview|count")) {
					System.out.println("-------------3");
					System.out.println("�����������Ŀ"+parser.nextText());
					news.pageview = parser.nextText();
				} else if (tag.matches("content"))
					System.out.println("-------------4");
					System.out.println("�������������"+parser.nextText());
					news.content = parser.nextText();
			} else if (eventType == XmlPullParser.END_TAG) {
				System.out.println("XmlPullParser.END_TAG="+XmlPullParser.END_TAG);
				if (parser.getName().equalsIgnoreCase(typeStr) && news != null) {
				}
			}
			eventType = parser.next();
			System.out.println("ĩβeventType="+eventType);
		}*/
		return news;
	}

	// ��ȡ����ͼƬ
	public static Bitmap getImagePicture(String urlStr) throws IOException {
		Bitmap bitmap = null;
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		// conn.connect();
		InputStream is = conn.getInputStream();
		bitmap = BitmapFactory.decodeStream(is);

		return bitmap;
	}

	public static String download() { // urlStrΪ�����ļ��ĵ�ַ
		URL url = null;
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null; // ÿ�ζ�ȡһ������
		try {
			// ����һ��URL����
			url = new URL(GETIMAGEINFO);
			// ����һ��Http����
			// System.out.println("------------>>"+url);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// ʹ��IO����ȡ����
			// inputStream���������ֽ���������InputStreamReader����ת��Ϊ�ַ�����һ��һ�����ַ�����
			// ��ʹ��BufferedReader����ȡһ��
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line); // ��ÿ�ζ�ȡ��һ����ӵ�StringBuffer����sb��
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	// �������ݿ�ͼƬ��Ϣ
	public static String searchImageInfo() throws Exception {
		String URL = GETIMAGEINFO;
		System.out.println("--->>" + URL);
		String ret = download();
		System.out.println("------>>download=" + ret);
		String result = "";
		XmlPullParser xmlParser = Xml.newPullParser();
		xmlParser.setInput(new StringReader(ret));
		int evtType = xmlParser.getEventType();
		while (evtType != XmlPullParser.END_DOCUMENT) {
			switch (evtType) {
			case XmlPullParser.START_TAG:
				String tag = xmlParser.getName();
				if (tag.equalsIgnoreCase("string")) {
					result = xmlParser.nextText();
				}
				break;

			case XmlPullParser.END_TAG:
				if (xmlParser.getName().equalsIgnoreCase("string")
						&& result != null) {
				}
				break;
			default:
				break;
			}
			evtType = xmlParser.next();
		}
		System.out.println("--------->>result=" + result);
		return result;
	}

	// ��ȡԤԼ��Ϣ���
	public static String getReservationResult(String marc_no, String indexNo,
			String password, String userCode) throws Exception {
		String URL = "http://elib.njutcm.edu.cn:8080/mainService.asmx/getReservartionResult?callno1="
				+ indexNo
				+ "&marc_no="
				+ marc_no
				+ "&password="
				+ password
				+ "&userCode=" + userCode;
		String ret = getURL(URL);
		System.out.println("----ret>>>" + ret);
		String result = "";
		XmlPullParser xmlParser = Xml.newPullParser();
		xmlParser.setInput(new StringReader(ret));
		int evtType = xmlParser.getEventType();
		while (evtType != XmlPullParser.END_DOCUMENT) {
			switch (evtType) {
			case XmlPullParser.START_TAG:
				String tag = xmlParser.getName();
				if (tag.equalsIgnoreCase("string")) {
					result = xmlParser.nextText();
				}
				break;
			case XmlPullParser.END_TAG:
				if (xmlParser.getName().equalsIgnoreCase("string")
						&& result != null) {
				}
				break;
			default:
				break;
			}
			evtType = xmlParser.next();
		}
		System.out.println("--------->>result=" + result);
		return result;
	}

	// ԤԼ����
	public static BookReservationApplication getReservationApplicationInfo(String marc_no,
			String password, String userCode) throws Exception {
		String URL = "http://elib.njutcm.edu.cn:8080/mainService.asmx/getReservationApplicationInfo?marc_no="
				+ marc_no + "&password=" + password + "&userCode=" + userCode;
		String ret = getURL(URL);
		System.out.println("----ret>>>" + ret);
		BookReservationApplication news = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(ret));
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				// sb.append("**********�ĵ���ʼ");
			} else if (eventType == XmlPullParser.START_TAG) {
				String tag = parser.getName();
				if (tag.equalsIgnoreCase("bookReservationApplication")) {
					news = new BookReservationApplication();
				} else if (tag.equalsIgnoreCase("indexNo")) {
					news.indexNo = parser.nextText();
				} else if (tag.equalsIgnoreCase("storePlace")) {
					news.storePlace = parser.nextText();
				} else if (tag.equalsIgnoreCase("canBorrowNum")) {
					news.canBorrowNum = parser.nextText();
				} else if (tag.equalsIgnoreCase("stayLibrary")) {
					news.stayLibrary = parser.nextText();
				} else if (tag.equalsIgnoreCase("waitNum")) {
					news.waitNum = parser.nextText();
				}else if (tag.equalsIgnoreCase("isReservation")) {
					news.isReservation = parser.nextText();
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equalsIgnoreCase("bookReservationApplication") && news != null) {
				}
			}
			eventType = parser.next();
		}
		return news;
	}
	//ȡ��ԤԼ
		public static String CancelReservation(String marc_no,String indexNo,
				String password, String userCode) throws Exception {
			String URL = "http://elib.njutcm.edu.cn:8080/mainService.asmx/cancelReservation?callno1="+indexNo+"&marc_no="+marc_no+"&password="+password+"&userCode="+userCode;
			String ret = getURL(URL);
			System.out.println("----ret>>>" + ret);
			String result = "";
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(new StringReader(ret));
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				case XmlPullParser.START_TAG:
					String tag = xmlParser.getName();
					if (tag.equalsIgnoreCase("string")) {
						result = xmlParser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if (xmlParser.getName().equalsIgnoreCase("string")
							&& result != null) {
					}
					break;
				default:
					break;
				}
				evtType = xmlParser.next();
			}
			System.out.println("--------->>result=" + result);
			return result;
		}
		//�������ӵ�ַ
		@SuppressWarnings("incomplete-switch")
		public static String DownFile(G.HostType hostType,String url) throws Exception {
			System.out.println("url="+url);
			String ret=null;
			String URL = "http://elib.njutcm.edu.cn:8080/";
			switch (hostType) {
			case WANFANG:
				URL += "wanfangService.asmx/getLocalDownLink?url=" + URLEncoder.encode(url);
				ret = getURL(URL);
				break;
			case BMJ:
				URL += "bmjService.asmx/getLocalDownLink?url=" + url;
				ret = getURL(URL);
				break;
			}
			System.out.println("DownFile URL="+URL);
			
			System.out.println("----ret>>>" + ret);
			String result = "";
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(new StringReader(ret));
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				case XmlPullParser.START_TAG:
					String tag = xmlParser.getName();
					if (tag.equalsIgnoreCase("string")) {
						result = xmlParser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if (xmlParser.getName().equalsIgnoreCase("string")
							&& result != null) {
					}
					break;
				default:
					break;
				}
				evtType = xmlParser.next();
			}
			System.out.println("--------->>result=" + result);
			return result;
		}
		public static String download(String urlStr) { // urlStrΪ�����ļ��ĵ�ַ
			URL url = null;
			StringBuffer sb = new StringBuffer();
			String line = null;
			BufferedReader buffer = null; // ÿ�ζ�ȡһ������
			try {
				// ����һ��URL����
				url = new URL(urlStr);
				// ����һ��Http����
				// System.out.println("------------>>"+url);
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				// ʹ��IO����ȡ����
				// inputStream���������ֽ���������InputStreamReader����ת��Ϊ�ַ�����һ��һ�����ַ�����
				// ��ʹ��BufferedReader����ȡһ��
				buffer = new BufferedReader(new InputStreamReader(
						urlConn.getInputStream()));
				while ((line = buffer.readLine()) != null) {
					sb.append(line); // ��ÿ�ζ�ȡ��һ����ӵ�StringBuffer����sb��
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					buffer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}
}

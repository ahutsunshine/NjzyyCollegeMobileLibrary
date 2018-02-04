package mars.getdata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class GetDataFromWeb {
	public static String getXML(String urlStr)throws Exception{
		StringBuffer sb=new StringBuffer();
		BufferedReader buffer=null;
		String line=null;
		URL url=new URL(urlStr);
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		buffer=new BufferedReader(new  InputStreamReader(conn.getInputStream()));
		while((line=buffer.readLine())!=null){
			sb.append(line);
		}
		return sb.toString();
	}
	public static InputStream getInputStream(String urlStr)throws Exception{
		URL url=new URL(urlStr);
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		InputStream inputStream=conn.getInputStream();
		return inputStream;
	}
//	public static Bitmap getImage(String urlStr){
//		Bitmap bitmap=null;
//		URL url=null;
//		try {
//			url=new URL(urlStr);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		HttpURLConnection conn;
//		try {
//			conn = (HttpURLConnection)url.openConnection();
//			conn.setDoInput(true);
//			conn.connect();
//			bitmap=BitmapFactory.decodeStream(conn.getInputStream());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return bitmap;
//	}
//	@SuppressWarnings("unchecked")
//	public static String postXML(String SERVER_URL,@SuppressWarnings("rawtypes") List params){
//		HttpPost request=new HttpPost(SERVER_URL);
//		String result=null;
//		try{
//			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
//			HttpResponse httpResponse=new DefaultHttpClient().execute(request);
//			if(httpResponse.getStatusLine().getStatusCode()!=404){
//				result=EntityUtils.toString(httpResponse.getEntity());
//			}
//		}
//		catch(Exception e){
//			return e.toString();
//		}
//		return result;
//	}
}

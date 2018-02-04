package example.mobilelibrary.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Xml;
import cn.iMobileLib.NjutcmMobileLibrary.R;
import cn.iMobileLib.NjutcmMobileLibrary.SearchBookActivity;
import example.mobilelibrary.entity.BookDetail;
import example.mobilelibrary.entity.BookInformation;
import example.mobilelibrary.entity.BookList;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.NewBookList;
import example.mobilelibrary.entity.News;
import example.mobilelibrary.entity.NewsDetail;

public class XmlWebService {
	private static HttpParams httpParameters = new BasicHttpParams();
	private static String FILE_NAME="example.mobilelibrary";
	public static String get(String urlTemp) { 		 		
		URL url; 		 		 
		BufferedReader bin = null; 		 		 
		StringBuilder result = new StringBuilder(); 		 		 
		try { 		 		 		 
		
		url=new URL(urlTemp);
		InputStream in=url.openStream();
		bin=new BufferedReader(new InputStreamReader(in,"UTF-8"));
		String s=null;
		while((s=bin.readLine())!=null){
			result.append(s);
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=bin){
				try{
					bin.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return result.toString();
		
	}
	public static String getURL(String URL,Context context) throws ClientProtocolException, IOException{
		SharedPreferences sharePre=context.getSharedPreferences(FILE_NAME, 0);
		if(!sharePre.getBoolean(R.id.gprscontrol+"",true)&&G.getNetworkType(context).equalsIgnoreCase("2G/3G"))
        	G.Network=false;
		if(G.Network){
		HttpGet request = new HttpGet(URL);
		String strResult = null;
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
				httpParameters);
		HttpResponse httpResponse = defaultHttpClient.execute(request);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			strResult = EntityUtils.toString(httpResponse.getEntity());
		}
		defaultHttpClient.getConnectionManager().shutdown();
		return strResult;}
		else return null;
	}
	public static String get(String urlTemp,Context context) { 	
		SharedPreferences sharePre=context.getSharedPreferences(FILE_NAME, 0);
		if(!sharePre.getBoolean(R.id.gprscontrol+"",true)&&G.getNetworkType(context).equalsIgnoreCase("2G/3G"))
        	G.Network=false;
		if(G.Network){
		URL url; 		 		 
		BufferedReader bin = null; 		 		 
		StringBuilder result = new StringBuilder(); 		 		 
		try { 		 		 		 
		url=new URL(urlTemp);
		InputStream in=url.openStream();
		bin=new BufferedReader(new InputStreamReader(in,"UTF-8"));
		String s=null;
		while((s=bin.readLine())!=null){
			result.append(s);
		}
		}catch(Exception e){
			System.out.println("Request exception!");
			return null;			
		}finally{
			if(null!=bin){
				try{
					bin.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}		
		System.out.println(G.getNetworkType(context));
		return result.toString();}
		else return null;
		
	}
	public static List<News> parse(int page) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/getNews?page="+
				URLEncoder.encode(Integer.toString(page),"UTF-8");
		List<News> list=new ArrayList<News>();
		String s=get(urlTemp);
		News news=null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			 if (eventType == XmlPullParser.START_DOCUMENT){ 
              
             }else if (eventType == XmlPullParser.START_TAG){ 
            	 String tag=parser.getName();
            	 if(tag.equalsIgnoreCase("news")){
            		 news=new News();
            	 }
            	 else if(tag.equalsIgnoreCase("title"))
            	 {
            		 news.title=parser.nextText();
            	 }
            	 else if(tag.equalsIgnoreCase("date")){
            		 news.date=parser.nextText();
            	 }else if(tag.equalsIgnoreCase("link")){
            		 news.link=parser.nextText();
            	 }
             
             }else if (eventType == XmlPullParser.END_TAG){ 
              
               if(parser.getName().equalsIgnoreCase("news")&&news!=null){
            	   list.add(news);
            	   news=null;
               }
            	   
             }  
             eventType = parser.next();   
         } 
		 return list;
		 }
	public static NewsDetail Newsdetail(String link) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/getNewsDetail?link="+
				URLEncoder.encode(link,"UTF-8");
		NewsDetail list=new NewsDetail();
		String s=get(urlTemp);
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			if (eventType == XmlPullParser.START_DOCUMENT){ 
	              
            }else if (eventType == XmlPullParser.START_TAG){ 
           	 String tag=parser.getName();
           	 if(tag.equalsIgnoreCase("content")){
           		 list.detail=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("title")){
           		 list.title=parser.nextText();
           	 }
            }else if (eventType == XmlPullParser.END_TAG){ 
                              
            }
			eventType = parser.next();   
		}
		return list;
	}
	public static List<NewBookList> newbooklist(int page,Context context) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/newBookList?page=" + page;
		List<NewBookList> list=new ArrayList<NewBookList>();
		String s=get(urlTemp,context);
		NewBookList newbook=null;
		if(G.Network){
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			 if (eventType == XmlPullParser.START_DOCUMENT){ 
	              
             }else if (eventType == XmlPullParser.START_TAG){ 
            	 String tag=parser.getName();
            	 if(tag.equalsIgnoreCase("newBook")){
            		 newbook=new NewBookList();
            	 }else if(tag.equalsIgnoreCase("title")){
            		 newbook.title=parser.nextText();
            	 }else if(tag.equalsIgnoreCase("marc_no")){
            		 newbook.marc_no=parser.nextText();
            	 }else if(tag.equalsIgnoreCase("detail")){
            		 newbook.information=parser.nextText();
            	 }
             }else if(eventType == XmlPullParser.END_TAG){
            	 if(parser.getName().equalsIgnoreCase("newBook")&&newbook!=null){
              	   list.add(newbook);
              	   newbook=null;
                 }
             }
			 eventType = parser.next();   
		}
		return list;
		}
		else 		
			return null;	
	}
	
	public static ArrayList<String> getBookLink(int page,Context context) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/newBookList?page="+
				URLEncoder.encode(Integer.toString(page),"UTF-8");
		ArrayList<String> list=new ArrayList<String>();
		String s=get(urlTemp,context);		
		if(G.Network){
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(s)); 
			int eventType=parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT){
				 if (eventType == XmlPullParser.START_TAG){ 
	            	 String tag=parser.getName();
	            	if(tag.equalsIgnoreCase("marc_no")){
	            		list.add("item.php?marc_no="+parser.nextText());
	            	 }
	             }
				 eventType = parser.next();   
			}
		}
		return list;	
	}
	
	public static List<BookList> searchBook(String keyword,int page,Context context) throws XmlPullParserException, IOException{
		String urlTemp ="http://elib.njutcm.edu.cn:8080/mainService.asmx/moreBooklist?keyWord="+
				URLEncoder.encode(keyword,"UTF-8")+
				"&page="+
				URLEncoder.encode(Integer.toString(page),"UTF-8")+"&totalNum=";
		List<BookList> list=new ArrayList<BookList>();
		int i=0;
		String s=null;
		//∂‡¥Œ«Î«Û
		while(i!=3&&s==null){
			s=get(urlTemp,context);
		}
		BookList book=null;
		if(G.Network){
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			 if (eventType == XmlPullParser.START_DOCUMENT){ 
	              
             }else if (eventType == XmlPullParser.START_TAG){ 
            	 String tag=parser.getName();
            	 if(tag.equalsIgnoreCase("totalNum")){
            		 SearchBookActivity.totalNum=Integer.valueOf(parser.nextText());
            	 }else if(tag.equalsIgnoreCase("thisPage")){
            		 SearchBookActivity.thisPage=Integer.valueOf(parser.nextText());
            	 }else if(tag.equalsIgnoreCase("thisPageNum")){
            		 SearchBookActivity.thisPageNum=Integer.valueOf(parser.nextText());
            	 }
            	 if(tag.equalsIgnoreCase("storeBookInfo")){
            		 book=new BookList();
            	 }else if(tag.equalsIgnoreCase("listName")){
            		 book.listName=parser.nextText();
            	 }else if(tag.equalsIgnoreCase("language")){
            		 book.language=parser.nextText();
            	 }else if(tag.equalsIgnoreCase("storeNum")){
            		 book.storeNum=parser.nextText();
            	 }else if(tag.equalsIgnoreCase("outNum")){
            		 book.outNum=parser.nextText();
            	 }else if(tag.equalsIgnoreCase("urlDetail")){
            		 book.urlDetail=parser.nextText();
            	 }
             }else if(eventType == XmlPullParser.END_TAG){
            	 if(parser.getName().equalsIgnoreCase("storeBookInfo")&&book!=null){
                	   list.add(book);
                	   book=null;
                   }
             }
			 eventType = parser.next();  
		}
		}
		return list;
	}
	
	public static ArrayList<String>getLink(String keyword,int page) throws XmlPullParserException, IOException{
		String urlTemp ="http://elib.njutcm.edu.cn:8080/mainService.asmx/moreBooklist?keyWord="+
				URLEncoder.encode(keyword,"UTF-8")+
				"&page="+
				URLEncoder.encode(Integer.toString(page),"UTF-8")+"&totalNum=";
		ArrayList<String> list=new ArrayList<String>();
		String s=get(urlTemp);
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			if (eventType == XmlPullParser.START_TAG){ 
            	 String tag=parser.getName();
            	 if(tag.equalsIgnoreCase("urlDetail")){
            		 list.add(parser.nextText());
            	 }
             }
			 eventType = parser.next();  
		}
		return list;
	}
	
	public static BookDetail getbookdetail(String urldetail,Context context) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/bookDtl?urlDetail="+
				URLEncoder.encode(urldetail,"UTF-8");
		BookDetail bookdetail=new BookDetail();
		String s=get(urlTemp,context);
		if(G.Network){
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			if (eventType == XmlPullParser.START_DOCUMENT){ 
	              
            }else if (eventType == XmlPullParser.START_TAG){ 
           	 String tag=parser.getName();
           	 if(tag.equalsIgnoreCase("marc_no")){
           		 bookdetail.marc_no=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("titleAuth")){
           		 bookdetail.titleAuth=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("pubItem")){
           		 bookdetail.pubItem=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("isbnPrice")){
           		 bookdetail.isbnPrice=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("pageSize")){
           		 bookdetail.pageSize=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("author")){
           		 bookdetail.author=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("subject")){
           		 bookdetail.subject=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("clc")){
           		 bookdetail.clc=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("bookAbstract")){
           		 bookdetail.bookAbstract=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("bookImg")){
           		 bookdetail.bookImg=parser.nextText();
           	 }
            }else if (eventType == XmlPullParser.END_TAG){ 
                
            }
			eventType = parser.next();   
		}
		}
		return bookdetail;
	}
	
	public static List<BookInformation> getbookinfor(String urldetail,Context context) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/bookDtl?urlDetail="+
				URLEncoder.encode(urldetail,"UTF-8");
		List<BookInformation> list=new ArrayList<BookInformation>();
		BookInformation bookinfor=null;
		int i=0;
		String s=get(urlTemp,context);
		if(G.Network){
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			if (eventType == XmlPullParser.START_DOCUMENT){ 
	              
            }else if (eventType == XmlPullParser.START_TAG){ 
           	 String tag=parser.getName();
           	 if(tag.equalsIgnoreCase("eachBookInfo")){
           		 bookinfor=new BookInformation();
           	 }else if(tag.equalsIgnoreCase("indexNo")){
           		 bookinfor.indexNo=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("yvi")){
           		 bookinfor.yvi=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("storePlace")){
           		 bookinfor.storePlace=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("boolState")){
           		 bookinfor.boolState=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("barCode")){
           		 bookinfor.barCode=parser.nextText();
           	 }
            }else if(eventType == XmlPullParser.END_TAG){
           	 if(parser.getName().equalsIgnoreCase("eachBookInfo")&&bookinfor!=null){
           		 if(i>0){
           			 list.add(bookinfor);
           			 bookinfor=null;
           		 }
           		 i++;
             }
       }
			eventType = parser.next();   
		}
		}
		return list;
	}
	public static BookDetail getdetailByISBN(String ISBN,Context context) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/searchbookbyISBN?isbn="+
				URLEncoder.encode(ISBN,"UTF-8");
		BookDetail bookdetail=new BookDetail();
		String s=get(urlTemp,context);
		if(G.Network){
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			if (eventType == XmlPullParser.START_DOCUMENT){ 
	              
            }else if (eventType == XmlPullParser.START_TAG){ 
           	 String tag=parser.getName();
           	 if(tag.equalsIgnoreCase("titleAuth")){
           		 bookdetail.titleAuth=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("pubItem")){
           		 bookdetail.pubItem=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("isbnPrice")){
           		 bookdetail.isbnPrice=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("pageSize")){
           		 bookdetail.pageSize=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("author")){
           		 bookdetail.author=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("subject")){
           		 bookdetail.subject=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("clc")){
           		 bookdetail.clc=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("bookAbstract")){
           		 bookdetail.bookAbstract=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("bookImg")){
           		 bookdetail.bookImg=parser.nextText();
           	 }
            }else if (eventType == XmlPullParser.END_TAG){ 
                
            }
			eventType = parser.next();   
		}
		}
		return bookdetail;
	}
	public static List<BookInformation> getbookinforByISBN(String ISBN,Context context) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/searchbookbyISBN?isbn="+
				URLEncoder.encode(ISBN,"UTF-8");
		List<BookInformation> list=new ArrayList<BookInformation>();
		BookInformation bookinfor=null;
		int i=0;
		String s=get(urlTemp,context);
		if(G.Network){
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			if (eventType == XmlPullParser.START_DOCUMENT){ 
	              
            }else if (eventType == XmlPullParser.START_TAG){ 
           	 String tag=parser.getName();
           	 if(tag.equalsIgnoreCase("eachBookInfo")){
           		 bookinfor=new BookInformation();
           	 }else if(tag.equalsIgnoreCase("indexNo")){
           		 bookinfor.indexNo=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("yvi")){
           		 bookinfor.yvi=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("storePlace")){
           		 bookinfor.storePlace=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("boolState")){
           		 bookinfor.boolState=parser.nextText();
           	 }else if(tag.equalsIgnoreCase("barCode")){
           		 bookinfor.barCode=parser.nextText();
           	 }
            }else if(eventType == XmlPullParser.END_TAG){
           	 if(parser.getName().equalsIgnoreCase("eachBookInfo")&&bookinfor!=null){
           		 if(i>0){
           			 list.add(bookinfor);
           			 bookinfor=null;
           		 }
           		 i++;
             }
       }
			eventType = parser.next();   
		}
		}
		return list;
	}
	public static String searchTalkInfo(Context context) throws XmlPullParserException, IOException{
		String urlTemp = "http://elib.njutcm.edu.cn:8080/mainService.asmx/jzap?";
		String item=null;
		String s=getURL(urlTemp,context);
		if(G.Network){
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(s)); 
		int eventType=parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT){
			if (eventType == XmlPullParser.START_TAG){ 
				String tag=parser.getName();
				if(tag.equalsIgnoreCase("string")){
					item=parser.nextText();
				}
			}else if(eventType == XmlPullParser.END_TAG){
				
			}
			eventType = parser.next();   
		}
		return item;}
		else return null;
	}
}

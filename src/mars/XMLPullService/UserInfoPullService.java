package mars.XMLPullService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import mars.ClassUtil.UserInfo;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class UserInfoPullService {
    public static UserInfo getUserInfo(InputStream inputStream)throws Exception{
    	UserInfo userInfo=new UserInfo();
    	XmlPullParser parser=Xml.newPullParser();
    	parser.setInput(inputStream, "UTF-8");
    	int event=parser.getEventType();
    	while(event!=XmlPullParser.END_DOCUMENT){
    		switch(event){
    		case XmlPullParser.START_TAG:
				if ("userCode".equals(parser.getName()))
					userInfo.userCode = parser.nextText();
				else if ("passWord".equals(parser.getName())) 
					userInfo.passWord = parser.nextText();
			    else if ("userName".equals(parser.getName())) 
					userInfo.userName = parser.nextText();
				else if ("barCode".equals(parser.getName())) 
					userInfo.barCode = parser.nextText();
				else if ("expiryDate".equals(parser.getName())) 
					userInfo.expiryDate = parser.nextText();
				else if ("regDate".equals(parser.getName())) 
					userInfo.regDate = parser.nextText();
				else if ("effectiveDate".equals(parser.getName())) 
					userInfo.effectiveDate = parser.nextText();
				else if ("maxNum".equals(parser.getName())) 
					userInfo.maxNum = parser.nextText();
				else if ("maxAppointmentNum".equals(parser.getName())) 
					userInfo.maxAppointmentNum = parser.nextText();
				else if ("maxDelegeteNum".equals(parser.getName())) 
					userInfo.maxDelegeteNum = parser.nextText();
			    else if ("userType".equals(parser.getName())) 
					userInfo.userType = parser.nextText();
				else if ("userLevel".equals(parser.getName())) 
					userInfo.userLevel = parser.nextText();
				else if ("acctotalNum".equals(parser.getName())) 
					userInfo.acctotalNum = parser.nextText();
				else if ("violationNum".equals(parser.getName())) 
					userInfo.violationNum = parser.nextText();
				else if ("amountInArear".equals(parser.getName())) 
					userInfo.amountInArear = parser.nextText();
				else if ("department".equals(parser.getName())) 
					userInfo.department = parser.nextText();
				else if ("email".equals(parser.getName())) 
					userInfo.email = parser.nextText().split(" ")[0];
				else if ("userID".equals(parser.getName())) 
					userInfo.userID = parser.nextText();
				else if ("career".equals(parser.getName())) 
					userInfo.career = parser.nextText();
				else if ("position".equals(parser.getName())) 
					userInfo.position = parser.nextText();
				else if ("sex".equals(parser.getName())) 
					userInfo.sex = parser.nextText();
				else if ("zipCode".equals(parser.getName()))
					userInfo.zipCode = parser.nextText();
				else if ("tel".equals(parser.getName())) 
					userInfo.tel = parser.nextText();
				else if ("cellPhone".equals(parser.getName())) 
					userInfo.cellPhone = parser.nextText();
				else if ("education".equals(parser.getName())) 
					userInfo.education = parser.nextText();
				else if ("cashPledge".equals(parser.getName())) 
					userInfo.cashPledge = parser.nextText();
				else if ("poundage".equals(parser.getName())) 
					userInfo.poundage = parser.nextText();
				else if ("workUnit".equals(parser.getName()))
					userInfo.workUnit = parser.nextText();
				else if ("address".equals(parser.getName())) 
					userInfo.address = parser.nextText();
				else if ("birthday".equals(parser.getName())) 
					userInfo.birthday = parser.nextText();
				else if ("image".equals(parser.getName())) 
					userInfo.image = parser.nextText();
				else if ("pastDue5DayNum".equals(parser.getName())) 
					userInfo.pastDue5DayNum = parser.nextText();
				else if ("pastDueNum".equals(parser.getName())) 
					userInfo.pastDueNum = parser.nextText();
				else if ("appointmentNum".equals(parser.getName())) 
					userInfo.appointmentNum = parser.nextText();
				else if ("delegeteNum".equals(parser.getName())) 
					userInfo.delegeteNum = parser.nextText();
    			break;
    			default:break;
    		}
    		event = parser.next();
    	}
    	return userInfo;
    }
    public static ArrayList<HashMap<String,String>> getBorrowBookDetail(InputStream inputStream)throws Exception{
    	ArrayList<HashMap<String,String>>list=null;
    	HashMap<String,String>map=null;
    	XmlPullParser parser=Xml.newPullParser();
    	parser.setInput(inputStream, "UTF-8");
    	int event=parser.getEventType();
    	while(event!=XmlPullParser.END_DOCUMENT){
    		switch(event){
    		case XmlPullParser.START_DOCUMENT:
    			list=new ArrayList<HashMap<String,String>>();
    			break;
    		case XmlPullParser.START_TAG:
    			if ("bookCurrent".equals(parser.getName()))
    				map=new HashMap<String, String>();
    			else if ("barCode".equals(parser.getName()))
					map.put("barCode",parser.nextText());
				else if ("title".equals(parser.getName())) 
					map.put("title",parser.nextText());
			    else if ("marc_no".equals(parser.getName())) 
			    	map.put("marc_no",parser.nextText());
				else if ("author".equals(parser.getName())) 
					map.put("author",parser.nextText());
				else if ("borrowDate".equals(parser.getName())) 
					map.put("borrowDate",parser.nextText());
				else if ("returnDate".equals(parser.getName())) 
					map.put("returnDate",parser.nextText());
				else if ("extendCount".equals(parser.getName())) 
					map.put("extendCount",parser.nextText());
				else if ("attachment".equals(parser.getName())) 
					map.put("attachment",parser.nextText());
				else if ("storePlace".equals(parser.getName())) 
					map.put("storePlace",parser.nextText());
				else if ("extendLink".equals(parser.getName())) 
					map.put("extendLink",parser.nextText());
    			break;
    		case XmlPullParser.END_TAG:
    			if ("bookCurrent".equals(parser.getName()))
    				list.add(map);
    			break;
    			default:break;
    		}
    		event = parser.next();
    	}
    	return list;
    }
    public static ArrayList<HashMap<String,String>> getBorrowHistoryBookDetail(InputStream inputStream)throws Exception{
    	ArrayList<HashMap<String,String>>list=null;
    	HashMap<String,String>map=null;
    	XmlPullParser parser=Xml.newPullParser();
    	parser.setInput(inputStream, "UTF-8");
    	int event=parser.getEventType();
    	while(event!=XmlPullParser.END_DOCUMENT){
    		switch(event){
    		case XmlPullParser.START_DOCUMENT:
    			list=new ArrayList<HashMap<String,String>>();
    			break;
    		case XmlPullParser.START_TAG:
    			if ("bookHistory".equals(parser.getName()))
    				map=new HashMap<String, String>();
    			else if ("barCode".equals(parser.getName()))
					map.put("barCode",parser.nextText());
				else if ("title".equals(parser.getName())) 
					map.put("title",parser.nextText());
			    else if ("marc_no".equals(parser.getName())) 
			    	map.put("marc_no",parser.nextText());
				else if ("author".equals(parser.getName())) 
					map.put("author",parser.nextText());
				else if ("borrowDate".equals(parser.getName())) 
					map.put("borrowDate",parser.nextText());
				else if ("returnDate".equals(parser.getName())) 
					map.put("returnDate",parser.nextText());
				else if ("storePlace".equals(parser.getName())) 
					map.put("storePlace",parser.nextText());
    			break;
    		case XmlPullParser.END_TAG:
    			if ("bookHistory".equals(parser.getName()))
    				list.add(map);
    			break;
    			default:break;
    		}
    		event = parser.next();
    	}
    	return list;
    }
    
    
    public static ArrayList<HashMap<String,String>> getBreakRulesInfo(InputStream inputStream)throws Exception{
    	ArrayList<HashMap<String,String>>list=null;
    	HashMap<String,String>map=null;
    	XmlPullParser parser=Xml.newPullParser();
    	parser.setInput(inputStream, "UTF-8");
    	int event=parser.getEventType();
    	while(event!=XmlPullParser.END_DOCUMENT){
    		switch(event){
    		case XmlPullParser.START_DOCUMENT:
    			list=new ArrayList<HashMap<String,String>>();
    			break;
    		case XmlPullParser.START_TAG:
    			if ("bookFine".equals(parser.getName()))
    				map=new HashMap<String, String>();
    			else if ("title".equals(parser.getName()))
					map.put("title",parser.nextText());
				else if ("returnDate".equals(parser.getName())) 
					map.put("returnDate",parser.nextText());
			    else if ("payMoney".equals(parser.getName())) 
			    	map.put("payMoney",parser.nextText());
				else if ("status".equals(parser.getName())) 
					map.put("status",parser.nextText());
    			break;
    		case XmlPullParser.END_TAG:
    			if ("bookFine".equals(parser.getName()))
    				list.add(map);
    			break;
    			default:break;
    		}
    		event = parser.next();
    	}
    	return list;
    }
    
    public static ArrayList<HashMap<String,String>> getReaderSuggestionInfo(InputStream inputStream)throws Exception{
    	ArrayList<HashMap<String,String>>list=null;
    	HashMap<String,String>map=null;
    	XmlPullParser parser=Xml.newPullParser();
    	parser.setInput(inputStream, "UTF-8");
    	int event=parser.getEventType();
    	while(event!=XmlPullParser.END_DOCUMENT){
    		switch(event){
    		case XmlPullParser.START_DOCUMENT:
    			list=new ArrayList<HashMap<String,String>>();
    			break;
    		case XmlPullParser.START_TAG:
    			if ("purchaseHistory".equals(parser.getName()))
    				map=new HashMap<String, String>();
    			else if ("title".equals(parser.getName()))
					map.put("title",parser.nextText());
				else if ("author".equals(parser.getName())) 
					map.put("author",parser.nextText());
			    else if ("publicationInfo".equals(parser.getName())) 
			    	map.put("publicationInfo",parser.nextText());
				else if ("purchaseDate".equals(parser.getName())) 
					map.put("purchaseDate",parser.nextText());
				else if ("purchaseStatus".equals(parser.getName())) 
					map.put("purchaseStatus",parser.nextText());
    			break;
    		case XmlPullParser.END_TAG:
    			if ("purchaseHistory".equals(parser.getName()))
    				list.add(map);
    			break;
    			default:break;
    		}
    		event = parser.next();
    	}
    	return list;
    }
    public static ArrayList<HashMap<String,String>> getReservationHistoryInfo(InputStream inputStream)throws Exception{
    	ArrayList<HashMap<String,String>>list=null;
    	HashMap<String,String>map=null;
    	XmlPullParser parser=Xml.newPullParser();
    	parser.setInput(inputStream, "UTF-8");
    	int event=parser.getEventType();
    	while(event!=XmlPullParser.END_DOCUMENT){
    		switch(event){
    		case XmlPullParser.START_DOCUMENT:
    			list=new ArrayList<HashMap<String,String>>();
    			break;
    		case XmlPullParser.START_TAG:
    			if ("bookReservation".equals(parser.getName()))
    				map=new HashMap<String, String>();
    			else if ("marc_no".equals(parser.getName()))
					map.put("marc_no",parser.nextText());
    			else if ("bookNumber".equals(parser.getName()))
					map.put("bookNumber",parser.nextText());
    			else if ("title".equals(parser.getName()))
					map.put("title",parser.nextText());
				else if ("author".equals(parser.getName())) 
					map.put("author",parser.nextText());
			    else if ("storePlace".equals(parser.getName())) 
			    	map.put("storePlace",parser.nextText());
				else if ("reservationDate".equals(parser.getName())) 
					map.put("reservationDate",parser.nextText());
				else if ("returnDate".equals(parser.getName())) 
					map.put("returnDate",parser.nextText());
				else if ("status".equals(parser.getName())) 
					map.put("status",parser.nextText());
    			break;
    		case XmlPullParser.END_TAG:
    			if ("bookReservation".equals(parser.getName()))
    				list.add(map);
    			break;
    			default:break;
    		}
    		event = parser.next();
    	}
    	return list;
    }
}

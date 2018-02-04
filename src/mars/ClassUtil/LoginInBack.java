package mars.ClassUtil;

import cn.iMobileLib.NjutcmMobileLibrary.MenuActivity;
import mars.getdata.GetDataFromWeb;


public class LoginInBack {
	public static String islogining(String userCode,String passWord){
		String url="http://elib.njutcm.edu.cn:8080/userInfo.asmx/userinfo?passWord="+passWord+"&userCode="+userCode;
		String result=null;
		try{
			result=GetDataFromWeb.getXML(url);
			System.out.println(url);
		}
		catch(Exception e){
			return "ÍøÂç´íÎó";
		}
		if(result.split(">").length>10){
			MenuActivity.userName=result.split("<userName>")[1].split("</userName>")[0];
			return "µÇÂ¼³É¹¦";
		}
		else
		    return "µÇÂ¼Ê§°Ü";
	}
}
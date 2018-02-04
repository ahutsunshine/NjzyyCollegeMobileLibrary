package cn.iMobileLib.NjutcmMobileLibrary;

import mars.ClassUtil.UserInfo;
import mars.XMLPullService.UserInfoPullService;
import mars.getdata.GetDataFromWeb;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserInfoActivity extends Activity{
	private static String url=null;
	private UserInfo userInfo=null;
	private userInfoAsyncTask task=null;
	private LinearLayout userinfoLinearLayout=null;
	private LinearLayout userInfoNoneLinearLayout=null;
	private Button userInfoBackButton=null;
	private LinearLayout userInfoLinearLayout=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);
		userinfoLinearLayout=(LinearLayout)findViewById(R.id.userinfoLinearLayout);
		userInfoNoneLinearLayout=(LinearLayout)findViewById(R.id.userInfoNoneLinearLayout);
		userInfoLinearLayout=(LinearLayout)findViewById(R.id.userInfoLinearLayout);
		userInfoBackButton=(Button)findViewById(R.id.userInfoBackButton);
		userInfoBackButton.setOnClickListener(new UserInfoBackButtonListener());
		url="http://elib.njutcm.edu.cn:8080/userInfo.asmx/userinfo?passWord="+MenuActivity.passWord+"&userCode="+MenuActivity.userCode;
		task=new userInfoAsyncTask();
		task.execute(url);
	}

	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				userInfoLinearLayout.setVisibility(View.GONE);
				if(msg.arg1==1){
					InitData();
					userinfoLinearLayout.setVisibility(View.VISIBLE);
				}
				else if(msg.arg1==2){
					userInfoNoneLinearLayout.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	
	
    class userInfoAsyncTask extends AsyncTask<Object,Object,Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			
			try{
				System.out.println("----->>UserInfoAcitivity (String)arg0[0]="+(String)arg0[0]);
				userInfo=UserInfoPullService.getUserInfo(GetDataFromWeb.getInputStream((String)arg0[0]));
			}
			catch(Exception e){
				e.printStackTrace();
				return "0";
			}
			return "1";
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Message msg=handler.obtainMessage(1);
			if(result.equals("1")){
				msg.arg1=1;
			}
			else{
				msg.arg1=2;
			//	Toast.makeText(UserInfoActivity.this, "º”‘ÿ ß∞‹", Toast.LENGTH_SHORT).show();
			}
			msg.sendToTarget();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
    }
    
    private void InitData(){
    	setTextView(R.id.userCode, userInfo.userCode);
		setTextView(R.id.userName, userInfo.userName);
		setTextView(R.id.barCode, userInfo.barCode);
		setTextView(R.id.expiryDate, userInfo.expiryDate);
		setTextView(R.id.regDate, userInfo.regDate);
		setTextView(R.id.effectiveDate, userInfo.effectiveDate);
		setTextView(R.id.maxNum, userInfo.maxNum);
		setTextView(R.id.maxAppointmentNum, userInfo.maxAppointmentNum);
		setTextView(R.id.maxDelegeteNum, userInfo.maxDelegeteNum);
		setTextView(R.id.userType, userInfo.userType);
		setTextView(R.id.userLevel, userInfo.userLevel);
		setTextView(R.id.acctotalNum, userInfo.acctotalNum);
		setTextView(R.id.violationNum, userInfo.violationNum);
		setTextView(R.id.amountInArear, userInfo.amountInArear);
		setTextView(R.id.department, userInfo.department);
		setTextView(R.id.email, userInfo.email);
		setTextView(R.id.userID, userInfo.userID);
		setTextView(R.id.career, userInfo.career);
		setTextView(R.id.position, userInfo.position);
		setTextView(R.id.sex, userInfo.sex);
		setTextView(R.id.zipCode, userInfo.zipCode);
		setTextView(R.id.tel, userInfo.tel);
		setTextView(R.id.cellPhone, userInfo.cellPhone);
		setTextView(R.id.education, userInfo.education);
		setTextView(R.id.cashPledge, userInfo.cashPledge);
		setTextView(R.id.poundage, userInfo.poundage);
		setTextView(R.id.workUnit, userInfo.workUnit);
		setTextView(R.id.address, userInfo.address);
		setTextView(R.id.birthday, userInfo.birthday);
		setTextView(R.id.image, userInfo.image);
		setTextView(R.id.pastDue5DayNum, userInfo.pastDue5DayNum);
		setTextView(R.id.pastDueNum, userInfo.pastDueNum);
		setTextView(R.id.appointmentNum, userInfo.appointmentNum);
		setTextView(R.id.delegeteNum, userInfo.delegeteNum);
    }
    
    class UserInfoBackButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
    	
    }
    
    private void setTextView(int id,String content){
    	TextView tx=(TextView)findViewById(id);
    	tx.setText(content);
    }
}

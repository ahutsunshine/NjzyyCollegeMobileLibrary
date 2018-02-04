package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.HashMap;

import mars.XMLPullService.UserInfoPullService;
import mars.getdata.GetDataFromWeb;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

public class BreakRulesInfoActivity extends ListActivity{
	private Button breakRulesInfoBackButton=null;
	private LinearLayout breakRulesInfoLayout=null;
	private LinearLayout breakRulesInfoNoneLinearLayout=null;
	private LinearLayout breakRulesInfoWaitLayout=null;
	private SimpleAdapter adapter=null;
	private ArrayList<HashMap<String,String>>list=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.breakrulesinfo);
		breakRulesInfoLayout=(LinearLayout)findViewById(R.id.breakRulesInfoLayout);
		breakRulesInfoNoneLinearLayout=(LinearLayout)findViewById(R.id.breakRulesInfoNoneLinearLayout);
		breakRulesInfoWaitLayout=(LinearLayout)findViewById(R.id.breakRulesInfoWaitLayout);
		breakRulesInfoBackButton=(Button)findViewById(R.id.breakRulesInfoBackButton);
		breakRulesInfoBackButton.setOnClickListener(new ButtonListener());
		runThread();
	}
	
	class ButtonListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	}
	
	public void linkClick(View view){
		setSelection(0);
		setListAdapter(adapter);
	}
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				breakRulesInfoWaitLayout.setVisibility(View.GONE);
				if(msg.arg1==1){
					adapter=new SimpleAdapter(BreakRulesInfoActivity.this,list,R.drawable.breakbules,new String[]{"title","returnDate","payMoney","status"},new int[]{R.id.breakRulesBookTitle,R.id.breakRulesBookReturnDate,R.id.breakRulesPayMoney,R.id.breakRulesStatus});
					setListAdapter(adapter);
					if(adapter.getCount()!=0)
						breakRulesInfoLayout.setVisibility(View.VISIBLE);
					else
						breakRulesInfoNoneLinearLayout.setVisibility(View.VISIBLE);
				}
				else{
					breakRulesInfoNoneLinearLayout.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	private void runThread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg=handler.obtainMessage(1);
				msg.arg1=1;
				try {
					getData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.arg1=0;
				}finally{
					msg.sendToTarget();
				}
			}
		}).start();
	}
	private void getData() throws Exception{
		list=UserInfoPullService.getBreakRulesInfo(GetDataFromWeb.getInputStream("http://elib.njutcm.edu.cn:8080/mainService.asmx/getFineInfo?password="+MenuActivity.passWord+"&userCode="+MenuActivity.userCode));
	}
}

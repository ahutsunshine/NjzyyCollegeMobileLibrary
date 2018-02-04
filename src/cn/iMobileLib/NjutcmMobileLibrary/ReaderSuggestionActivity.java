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
import example.mobilelibrary.Adapter.ReaderSuggestionAdapter;

public class ReaderSuggestionActivity extends ListActivity{
	private Button readerSuggestionInfoBackButton=null;
	private LinearLayout readerSuggestionInfoLayout=null;
	private LinearLayout readerSuggestionInfoNoneLinearLayout=null;
	private LinearLayout readerSuggestionInfoWaitLayout=null;
	private ReaderSuggestionAdapter adapter=null;
	private ArrayList<HashMap<String,String>>list=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readersuggestioninfo);
		readerSuggestionInfoLayout=(LinearLayout)findViewById(R.id.readerSuggestionInfoLayout);
		readerSuggestionInfoNoneLinearLayout=(LinearLayout)findViewById(R.id.readerSuggestionInfoNoneLinearLayout);
		readerSuggestionInfoWaitLayout=(LinearLayout)findViewById(R.id.readerSuggestionInfoWaitLayout);
		readerSuggestionInfoBackButton=(Button)findViewById(R.id.readerSuggestionInfoBackButton);
		readerSuggestionInfoBackButton.setOnClickListener(new ButtonListener());
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
				readerSuggestionInfoWaitLayout.setVisibility(View.GONE);
				if(msg.arg1==1){
					adapter=new ReaderSuggestionAdapter(ReaderSuggestionActivity.this,list);
					setListAdapter(adapter);
					if(adapter.getCount()!=0)
						readerSuggestionInfoLayout.setVisibility(View.VISIBLE);
					else
						readerSuggestionInfoNoneLinearLayout.setVisibility(View.VISIBLE);
				}
				else{
					readerSuggestionInfoNoneLinearLayout.setVisibility(View.VISIBLE);
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
		list=UserInfoPullService.getReaderSuggestionInfo(GetDataFromWeb.getInputStream("http://211.70.149.139/mainService.asmx/getPurchaseHistory?password="+MenuActivity.passWord+"&userCode="+MenuActivity.userCode));
	}
}

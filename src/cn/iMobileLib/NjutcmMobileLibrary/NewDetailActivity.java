package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewDetailActivity extends Activity{
	private TextView titleText=null;
	private TextView detailText=null;
	private ImageButton returnSubject=null;
	private LinearLayout layoutLoading;
	private LinearLayout layoutLoaded;
	private String  Str_1;
	private String  Str_2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		layoutLoading=(LinearLayout)findViewById(R.id.tbar_3);
		layoutLoaded=(LinearLayout)findViewById(R.id.newsdetail_tbar);
		layoutLoaded.setVisibility(View.GONE);
		layoutLoading.setVisibility(View.VISIBLE);
		 new Thread(new Runnable() {
			 public void run() {
				 Intent intent=getIntent();
					 Str_1=intent.getStringExtra("detail");
					 Str_2=intent.getStringExtra("title");
					 mHandler.post(runnableUi);
					 mHandler.sendEmptyMessage(1);
			 }
		 }).start();
		
		returnSubject=(ImageButton)findViewById(R.id.return_Subject);
		titleText=(TextView)findViewById(R.id.title_2);
		detailText=(TextView)findViewById(R.id.detail_2);		
		
		returnSubject.setOnClickListener(new ReturnButtonListener());
	}
	private  Handler mHandler=new Handler(){
		 @Override
		 public void handleMessage(Message msg) {
			 if (msg.what==1) {
				layoutLoaded.setVisibility(View.VISIBLE);
				layoutLoading.setVisibility(View.GONE);
			 }
		 }
	};
	
	Runnable runnableUi=new Runnable(){

		@Override
		public void run() {
			titleText.setText(Str_2);
			detailText.setText(Str_1.trim());
		}
	};
	
	class ReturnButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			intent.setClass(NewDetailActivity.this, MenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			NewDetailActivity.this.startActivity(intent);
			
		}
		
	}

}

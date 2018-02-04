package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity{
	private Intent intent=new Intent();
	private Button returnButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		returnButton=(Button)findViewById(R.id.SetBackButton);
		returnButton.setOnClickListener(new ReturnButtonListener());
	}
	public void relClick(View v) {
		int tag = Integer.parseInt(v.getTag().toString());
		if(tag==1){
			intent.setClass(SettingActivity.this, GPRSControlActivity.class);
			SettingActivity.this.startActivity(intent);
		}
//		if(tag==2){
//			
//			if(!MenuActivity.isLoad){
//				intent.putExtra("Activity", "SettingActivity");
//				intent.setClass(SettingActivity.this, LoginActivity.class);
//			}
//			else{
//				intent.setClass(SettingActivity.this, FeedbackActivity.class);
//			}
//			SettingActivity.this.startActivity(intent);
//		}
	}
	class ReturnButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			finish();		
		}
		
	}

}

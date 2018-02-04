package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;



public class SubjectActivity extends Activity{
	private ImageButton newBookButton=null;
	private ImageButton cameraButton=null;
	private ImageButton department=null;
	private ImageButton settingButton=null;
	private ImageButton talkButton=null;
	private ImageButton libraryButton=null;
	private ImageButton floorButton=null;
	private ImageButton helpButton=null;
	private ImageButton feedbackButton=null;
	private int count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		newBookButton=(ImageButton)findViewById(R.id.newbooklist);
		cameraButton=(ImageButton)findViewById(R.id.camerasearch);
		department=(ImageButton)findViewById(R.id.department);
		settingButton=(ImageButton)findViewById(R.id.setting);
		talkButton=(ImageButton)findViewById(R.id.talkButton);
		libraryButton=(ImageButton)findViewById(R.id.myLibrary);
		floorButton=(ImageButton)findViewById(R.id.floorInfo);
		libraryButton.setOnClickListener(new LibraryButtonListener());
		newBookButton.setOnClickListener(new NewBookButtonListener());
		cameraButton.setOnClickListener(new cameraButtonListener());
		department.setOnClickListener(new departmentListener());
		settingButton.setOnClickListener(new SettingButtonListener());
		talkButton.setOnClickListener(new TalkButtonListener());
		floorButton.setOnClickListener(new FloorButtonListener() );
		helpButton=(ImageButton)findViewById(R.id.help);
		helpButton.setOnClickListener(new HelpButtonListener());
		feedbackButton=(ImageButton)findViewById(R.id.feedback);
		feedbackButton.setOnClickListener(new FeedbackButtonListener());
	}
	class LibraryButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根			
			Intent intent=new Intent();
			if(!MenuActivity.isLoad){
				intent.putExtra("Activity", "MyLibraryActivity");
				intent.setClass(SubjectActivity.this, LoginActivity.class);
			}
			else{
				intent.setClass(SubjectActivity.this, MyLibraryActivity.class);
			}
			SubjectActivity.this.startActivity(intent);
		}
	}
	class NewBookButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根			
			Intent intent=new Intent();
			intent.setClass(SubjectActivity.this,NewBookActivity.class);
			SubjectActivity.this.startActivity(intent);
			}						
	}
	class cameraButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			intent.setClass(SubjectActivity.this, CaptureActivity.class);
			SubjectActivity.this.startActivity(intent);
		}
		
	}
	class departmentListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			intent.setClass(SubjectActivity.this, DepartmentInforActivity.class);
			SubjectActivity.this.startActivity(intent);
		}
		
	}
	class SettingButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			intent.setClass(SubjectActivity.this, SettingActivity.class);
			SubjectActivity.this.startActivity(intent);
		}
		
	}
	class TalkButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			intent.setClass(SubjectActivity.this, TalkInforActivity.class);
			SubjectActivity.this.startActivity(intent);
		}	
	}
	class FloorButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(SubjectActivity.this, FloorDetailActivity.class);
			SubjectActivity.this.startActivity(intent);
		}
		
	}
	class HelpButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			intent.putExtra("flag", "1");
			intent.setClass(getApplicationContext(), HelpActivity.class);
			SubjectActivity.this.startActivity(intent);
		}
		
	}
	class FeedbackButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			if(!MenuActivity.isLoad){
				intent.putExtra("Activity", "FeedbackButton");
				intent.setClass(SubjectActivity.this, LoginActivity.class);
			}
			else{
				intent.setClass(SubjectActivity.this, FeedbackActivity.class);
			}
			SubjectActivity.this.startActivity(intent);
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		count=0;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(count==0){
			Toast.makeText(getApplicationContext(), "再按一下返回键退出程序!",
					Toast.LENGTH_SHORT).show();	
			count++;
			}
			else finish();
			return true;
		}	
		else
		return super.onKeyDown(keyCode, event);		
	}
}

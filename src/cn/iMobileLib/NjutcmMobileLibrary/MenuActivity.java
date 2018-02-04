package cn.iMobileLib.NjutcmMobileLibrary;

import mars.SqliteHelp.DatabaseHelper;
import mars.getdata.GetDataFromWeb;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.huewu.pla.sample.SchoolImageActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;


public class MenuActivity extends TabActivity implements
		android.widget.CompoundButton.OnCheckedChangeListener {
	public static TextView tvNews;
	
	private TabHost tabHost;
	private RelativeLayout layout1;
	private RelativeLayout layout2;
	private RelativeLayout layout3;
	private RelativeLayout layout4;
	private RelativeLayout layout5;
	private RadioButton radio1 = null;
	private RadioButton radio2 = null;
	private RadioButton radio3 = null;
	private RadioButton radio4 = null;
	private RadioButton radio5 = null;
	private String flagId = "radio1";
	public static boolean isLoad = false;
	public static String userCode = "";
	public static String passWord = "";
	public static String userName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_main);
		
			new Thread(){public void run() {
				String token;
				try {
					token = GetDataFromWeb.getXML("http://www.apabi.com/tiyan/mobile.mvc?api=signin&uid=zhangsh&pwd=MTIzNDU2");
					System.out.println("token resonse="+token);
					token=token.split(">")[2].split("<")[0];
					System.out.println("token="+token);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};}.start();
			
		
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		layout3 = (RelativeLayout) findViewById(R.id.layout3);
		layout4 = (RelativeLayout) findViewById(R.id.layout4);
		layout5 = (RelativeLayout) findViewById(R.id.layout5);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		tvNews = (TextView) findViewById(R.id.tvNews);
		radio3 = (RadioButton) findViewById(R.id.radio3);
		radio4 = (RadioButton) findViewById(R.id.radio4);
		radio5 = (RadioButton) findViewById(R.id.radio5);
		radio1.setOnCheckedChangeListener(this);
		radio2.setOnCheckedChangeListener(this);
		radio3.setOnCheckedChangeListener(this);
		radio4.setOnCheckedChangeListener(this);
		radio5.setOnCheckedChangeListener(this);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("tab1")
				.setContent(new Intent(this, SubjectActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("tab2")
				.setContent(new Intent(this, NewsActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("tab3")
				.setContent(new Intent(this, KSearchActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("tab4")
				.setContent(new Intent(this, SearchlibraryActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator("tab5")
				.setContent(new Intent(this, SchoolImageActivity.class)));

		radio1.setChecked(true);

    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	if(prefs.getBoolean("hasNews", true)) {
    		tvNews.setVisibility(View.VISIBLE);
    	}
    	
		CreateSQLite();

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		UmengUpdateAgent.update(this);
		MobclickAgent.setDebugMode(true);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.radio1:
			if (isChecked) {
				tabHost.setCurrentTabByTag("tab1");
				radio2.setChecked(false);
				radio3.setChecked(false);
				radio4.setChecked(false);
				radio5.setChecked(false);
				flagId = "radio1";
				layout1.setBackgroundResource(R.drawable.new_selector_footer_checked);
			} else {
				layout1.setBackgroundResource(0);
			}
			break;
		case R.id.radio2:
			if (isChecked) {
				tabHost.setCurrentTabByTag("tab2");
				radio1.setChecked(false);
				radio3.setChecked(false);
				radio4.setChecked(false);
				radio5.setChecked(false);
				flagId = "radio2";
				tvNews.setVisibility(View.GONE);
	        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	        	SharedPreferences.Editor editor = prefs.edit();
	        	editor.putBoolean("hasNews", false);
	        	editor.commit();
				layout2.setBackgroundResource(R.drawable.new_selector_footer_checked);
			} else {
				layout2.setBackgroundResource(0);
			}
			break;
		case R.id.radio3:
			if(!isLoad&&isChecked){
				isLoad();
			}
			else{
				if (isChecked) {
					tabHost.setCurrentTabByTag("tab3");
					radio2.setChecked(false);
					radio1.setChecked(false);
					radio4.setChecked(false);
					radio5.setChecked(false);
					flagId = "radio3";
					layout3.setBackgroundResource(R.drawable.new_selector_footer_checked);
				} else {
					layout3.setBackgroundResource(0);
				}
			}
			break;
		case R.id.radio4:
			if (isChecked) {
				tabHost.setCurrentTabByTag("tab4");
				radio2.setChecked(false);
				radio3.setChecked(false);
				radio1.setChecked(false);
				radio5.setChecked(false);
				flagId = "radio4";
				layout4.setBackgroundResource(R.drawable.new_selector_footer_checked);
			} else {
				layout4.setBackgroundResource(0);
			}
			break;
		case R.id.radio5:
			if (isChecked) {
				tabHost.setCurrentTabByTag("tab5");
				radio2.setChecked(false);
				radio3.setChecked(false);
				radio4.setChecked(false);
				radio1.setChecked(false);
				flagId = "radio5";
				layout5.setBackgroundResource(R.drawable.new_selector_footer_checked);
			} else {
				layout5.setBackgroundResource(0);
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			tabHost.setCurrentTabByTag("tab3");
			setChecked("radio3");
		} else {
			setChecked(flagId);
		}
	}

	private void setChecked(String id) {
		InitRadioButton();
		if (id.equals("radio1"))
			radio1.performClick();
		else if (id.equals("radio2"))
			radio2.performClick();
		else if (id.equals("radio3"))
				radio3.performClick();
		else if (id.equals("radio4"))
			radio4.performClick();
		else if (id.equals("radio5"))
			radio5.performClick();
	}
    private void InitRadioButton(){
    	radio2.setChecked(false);
		radio3.setChecked(false);
		radio4.setChecked(false);
		radio5.setChecked(false);
		radio1.setChecked(false);
    }
	private void CreateSQLite() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		DatabaseHelper dbHelper = new DatabaseHelper(MenuActivity.this,
				"user_password_table");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (isFirstRun) {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("isFirstRun", false);
			editor.commit();
		} else {
			Cursor cursor = db.query("login", new String[] { "usercode",
					"password", "username" }, null, null, null, null, null);
			while (cursor.moveToNext()) {
				if (!cursor.getString(cursor.getColumnIndex("usercode")).trim()
						.equals("")
						&& !cursor.getString(cursor.getColumnIndex("password"))
								.trim().equals("")) {
					userCode = cursor.getString(cursor
							.getColumnIndex("usercode"));
					passWord = cursor.getString(cursor
							.getColumnIndex("password"));
					userName = cursor.getString(cursor
							.getColumnIndex("username"));
					isLoad = true;
				}
			}
			cursor.close();
		}
		db.close();
	}

	private void isLoad(){
		Intent intent=new Intent();System.out.println("sunpeng");
		intent.putExtra("Activity", "SearchActivity");
		intent.setClass(MenuActivity.this, LoginActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isLoad = false;
		userCode = "";
		passWord = "";
		userName = "";
	}
}

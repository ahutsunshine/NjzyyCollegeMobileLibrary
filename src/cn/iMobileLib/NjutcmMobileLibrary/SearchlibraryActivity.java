package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

public class SearchlibraryActivity extends Activity implements RecognizerDialogListener{
	private EditText edittext=null;
	private Button searchButton=null;
	private RecognizerDialog soundDialog;
	private int count=0;
	private ImageView camearSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.searchlibrary); 
		 edittext=(EditText)findViewById(R.id.etKeyword);
		 searchButton=(Button)findViewById(R.id.searchBooklist);
		 camearSearch=(ImageView)findViewById(R.id.imgcamerasearch);
		 camearSearch.setOnClickListener(new CamearSearchListener());
		 searchButton.setOnClickListener(new searchButtonListener());
		 initSoundFun();
	}
	class CamearSearchListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), CaptureActivity.class);
			SearchlibraryActivity.this.startActivity(intent);
		}
		
	}
	class searchButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(edittext.getText().toString().length()<1){
				new AlertDialog.Builder(SearchlibraryActivity.this).setTitle("提示").setMessage("请输入关键字")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						dialog.dismiss();
					}
				}).create().show();
			}
			else{
			Intent intent=new Intent();
			intent.putExtra("keyword", edittext.getText().toString());
			intent.setClass(SearchlibraryActivity.this, SearchBookActivity.class);
			SearchlibraryActivity.this.startActivity(intent);
			}
		}
		
	}
	private void initSoundFun() {
		// 语音功能
		soundDialog = new RecognizerDialog(this, this.getResources().getString(
				R.string.sound_appid));
		// ===注册监听器
		soundDialog.setListener(this);
		// ===类型：sms普通文本
		soundDialog.setEngine("sms", null, null);
		// ===采样频率--考虑android支持情况8k、16k；所以一般采用16k
		soundDialog.setSampleRate(RATE.rate16k);
	}
	public void imgClick(View v) {
		soundDialog.show();
	}

	@Override
	public void onEnd(SpeechError arg0) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void onResults(ArrayList<RecognizerResult> arg0, boolean arg1) {
		// TODO 自动生成的方法存根
		String result = "";
		for (int i = 0; i < arg0.size(); i++) {
			result += arg0.get(i).text;
		}
		edittext.append(result.replace("。", ""));
		edittext.setSelection(edittext.getText().length());
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
			Toast.makeText(getApplicationContext(), "再按一下返回键退出程序!",Toast.LENGTH_SHORT).show();	
			count++;
			}
			else finish();
			return true;
		}	
		else
		return super.onKeyDown(keyCode, event);		
	}
}

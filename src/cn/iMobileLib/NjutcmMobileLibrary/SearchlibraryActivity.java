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
		// TODO �Զ����ɵķ������
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
			// TODO �Զ����ɵķ������
			Intent intent=new Intent();
			intent.setClass(getApplicationContext(), CaptureActivity.class);
			SearchlibraryActivity.this.startActivity(intent);
		}
		
	}
	class searchButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO �Զ����ɵķ������
			if(edittext.getText().toString().length()<1){
				new AlertDialog.Builder(SearchlibraryActivity.this).setTitle("��ʾ").setMessage("������ؼ���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �Զ����ɵķ������
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
		// ��������
		soundDialog = new RecognizerDialog(this, this.getResources().getString(
				R.string.sound_appid));
		// ===ע�������
		soundDialog.setListener(this);
		// ===���ͣ�sms��ͨ�ı�
		soundDialog.setEngine("sms", null, null);
		// ===����Ƶ��--����android֧�����8k��16k������һ�����16k
		soundDialog.setSampleRate(RATE.rate16k);
	}
	public void imgClick(View v) {
		soundDialog.show();
	}

	@Override
	public void onEnd(SpeechError arg0) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void onResults(ArrayList<RecognizerResult> arg0, boolean arg1) {
		// TODO �Զ����ɵķ������
		String result = "";
		for (int i = 0; i < arg0.size(); i++) {
			result += arg0.get(i).text;
		}
		edittext.append(result.replace("��", ""));
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
		// TODO �Զ����ɵķ������
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(count==0){
			Toast.makeText(getApplicationContext(), "�ٰ�һ�·��ؼ��˳�����!",Toast.LENGTH_SHORT).show();	
			count++;
			}
			else finish();
			return true;
		}	
		else
		return super.onKeyDown(keyCode, event);		
	}
}

package cn.iMobileLib.NjutcmMobileLibrary;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import example.mobilelibrary.http.XmlWebService;

public class FeedbackActivity extends Activity{
	private Button mBack;
	private ImageView mPublish;
	private EditText mContent;
	private TextView mCount;
	private String result=null;
	private ProgressDialog dialog;
	//检测数字数目
	private CharSequence temp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advice_feedback);
		
		findViewById();
		mPublish.setOnClickListener(new OnClickListener() {

			private String url;
			public void onClick(View v) {
				if (mContent.getText().toString().trim().length() == 0) {
					System.out.println("检测有无输入内容");
					Toast.makeText(FeedbackActivity.this, "您尚未输入内容,请输入后重试", Toast.LENGTH_SHORT).show();
				}else if(temp.length()>60){
					Toast.makeText(FeedbackActivity.this, "您输入的内容超过限制范围，请删减", Toast.LENGTH_SHORT).show();
					return;
				} else {
					try {
						System.out.println("正在提交");
						 dialog=new ProgressDialog(FeedbackActivity.this);
						 dialog.setMessage("正在提交，请稍后...");
						 dialog.setCanceledOnTouchOutside(false);
						 dialog.show();
					   //  url="http://211.70.149.139/mainService.asmx/FeedBack?feedback="+URLEncoder.encode(mContent.getText().toString(),"UTF-8");
						url="http://elib.njutcm.edu.cn:8081/getInitInfo.asmx/recordSuggestionFeedback?suggestion="+URLEncoder.encode(mContent.getText().toString(),"UTF-8")+"&userCode="+MenuActivity.userCode;	
					    System.out.println("url="+url);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							 try {
								result=XmlWebService.get(url);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								result="<boolean>false</boolean>";
								mHandler.post(runnableUi);
							 	mHandler.sendEmptyMessage(1);	
								
							}						
							 	mHandler.post(runnableUi);
							 	mHandler.sendEmptyMessage(1);		
						}
					}).start();					
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();			
					}
					
				}
			}
		});
		mContent.addTextChangedListener(new TextWatcher() {
			
			private int selectionStart;
			private int selectionEnd;

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {
				int number = s.length();
				mCount.setText(String.valueOf(number));
				try {
					selectionStart = mContent.getSelectionStart();
					selectionEnd = mCount.getSelectionEnd();
					if (temp.length() > 60) {
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						mContent.setText(s);
						mContent.setSelection(tempSelection);
					}
				} catch (Exception e) {
					Toast.makeText(FeedbackActivity.this, "您输入的内容超过限制范围，请删减", Toast.LENGTH_SHORT).show();
				}
			
				
			}
		});	
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}
	
	private  Handler mHandler=new Handler(){
		 @Override
		 public void handleMessage(Message msg) {
			if(msg.what==1){
				dialog.dismiss();
				}
		 }
	};
	Runnable runnableUi=new Runnable(){

		@Override
		public void run() {
			if(!result.equalsIgnoreCase("")){
				String temp[]=result.split(">")[2].split("<");
				Toast.makeText(getApplicationContext(), temp[0],Toast.LENGTH_SHORT).show();
				FeedbackActivity.this.finish();
			}
			else
				Toast.makeText(FeedbackActivity.this, "很抱歉，提交失败", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
		}
	};	
	private void findViewById() {
		mBack = (Button) findViewById(R.id.back);
		mPublish = (ImageView) findViewById(R.id.fdBackButton);
		mContent = (EditText) findViewById(R.id.newsfeedpublish_content);
		mCount = (TextView) findViewById(R.id.newsfeedpublish_count);
	}
}


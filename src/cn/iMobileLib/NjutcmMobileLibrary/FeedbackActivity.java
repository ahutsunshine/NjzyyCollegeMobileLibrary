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
	//���������Ŀ
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
					System.out.println("���������������");
					Toast.makeText(FeedbackActivity.this, "����δ��������,�����������", Toast.LENGTH_SHORT).show();
				}else if(temp.length()>60){
					Toast.makeText(FeedbackActivity.this, "����������ݳ������Ʒ�Χ����ɾ��", Toast.LENGTH_SHORT).show();
					return;
				} else {
					try {
						System.out.println("�����ύ");
						 dialog=new ProgressDialog(FeedbackActivity.this);
						 dialog.setMessage("�����ύ�����Ժ�...");
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
						// TODO �Զ����ɵ� catch ��
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
					Toast.makeText(FeedbackActivity.this, "����������ݳ������Ʒ�Χ����ɾ��", Toast.LENGTH_SHORT).show();
				}
			
				
			}
		});	
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
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
				Toast.makeText(FeedbackActivity.this, "�ܱ�Ǹ���ύʧ��", Toast.LENGTH_SHORT).show();
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


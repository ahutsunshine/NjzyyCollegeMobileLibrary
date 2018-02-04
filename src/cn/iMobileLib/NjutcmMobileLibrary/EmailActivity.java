package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmailActivity extends Activity {
	private EditText et_subject, et_content;
	private Button btn_send;
	ProgressDialog pDialog = null;

	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.email);

		// et_to = (EditText) findViewById(R.id.et_to);
		et_subject = (EditText) findViewById(R.id.et_subject);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_send = (Button) findViewById(R.id.btn_send);

		// button�ļ�����
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(){
					public void run() {
						Boolean isConnected=WebServiceHelper.getNetWorkInfo(EmailActivity.this);
						if(isConnected)  {
							String toEmail = getIntent().getExtras().getString("email");
							String subject = et_subject.getText().toString();
							String content = et_content.getText().toString();
							// ����Intent
							Intent emailIntent = new Intent(
									android.content.Intent.ACTION_SEND);
							// ������������
							emailIntent.setType("plain/text");
							// ���ö�����Ϣ
							emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
									new String[] { toEmail });
							emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
									subject);
							emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
									content);
							// ����Activity
							startActivity(Intent.createChooser(emailIntent, "ѡ���ͷ�ʽ"));
							
							EmailActivity.this.finish();
						} else {
							runOnUiThread( new Runnable() {
								public void run() {
									Toast.makeText(EmailActivity.this, "���������Ƿ�����...",
											Toast.LENGTH_SHORT).show();
									return;
								}
							});
							
						}
					};
				}.start();
				
			}

		});

	}
}
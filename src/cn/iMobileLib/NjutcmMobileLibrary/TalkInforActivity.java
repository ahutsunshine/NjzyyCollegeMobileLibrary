package cn.iMobileLib.NjutcmMobileLibrary;



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.http.XmlWebService;

public class TalkInforActivity extends Activity{
	private int flag=0;
	private Button backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.talkinfor);
		backButton=(Button)findViewById(R.id.TalkBackButton);
		backButton.setOnClickListener(new BackButtonListener());
		new loadInfor().execute();
	}
	private class loadInfor extends AsyncTask<Integer,Integer,String>{
		ProgressDialog dialog;
		@Override
		protected String doInBackground(Integer... arg0) {
			// TODO 自动生成的方法存根			
			try {
				return XmlWebService.searchTalkInfo(TalkInforActivity.this);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块	
				if(e.getMessage()!=null)
					flag=1;				
				return null;
			}
		}
		@Override
		protected void onPreExecute() {
			// TODO 自动生成的方法存根
			super.onPreExecute();
			dialog=new ProgressDialog(TalkInforActivity.this);
			dialog.setMessage("请求中...");
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO 自动生成的方法存根
			dialog.dismiss();
			TextView talktext=(TextView)findViewById(R.id.talkinfor);
			if(result!=null)
			talktext.setText(result);
			else if(!G.Network)Toast.makeText(getApplicationContext(), "2G/3G网络已关闭",Toast.LENGTH_SHORT).show();
			else G.abnormal(flag, TalkInforActivity.this);
		}
	}
	class BackButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
}

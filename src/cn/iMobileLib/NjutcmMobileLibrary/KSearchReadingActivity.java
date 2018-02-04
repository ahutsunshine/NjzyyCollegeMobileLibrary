package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mobilelibrary.api.ApiKSearch;
import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.entity.G;

/**
 * 
 * 知识检索详细阅读文件
 * 
 * @author Administrator
 * 
 */
public class KSearchReadingActivity extends BaseLoadingActivity {

	private HeaderWidget headerWidget;
	//
	private String url;
	// 参数
	private G.HostType paramHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		View view = LayoutInflater.from(this).inflate(R.layout.ksearch_reading,
				null);
		addRootView(view);
		//
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		headerWidget.setMiddleText("文件阅读");
		headerWidget.setLeftText("返回");

		Bundle b = getIntent().getExtras();
		url = b.getString("downurl");
		paramHost = G.HostType.valueOf(b.getInt("host"));
		
		asyncRequest(0);
	}

	@Override
	public void onMsgStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMsgOk() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMsgError() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		// TODO Auto-generated method stub
		try {
			return ApiKSearch.readFile(paramHost, url);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onAsyncInUI(Object object) {
		// TODO Auto-generated method stub
		if(object==null){
			//ImageView imageView=new ImageView(KSearchReadingActivity.this);
			//imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder=new Builder(KSearchReadingActivity.this);
			builder.setMessage("阅读出错啦，请稍后重试...");
			builder.setTitle("提示");
			//builder.setView(imageView);
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			if(!isFinishing())
			{
				try {
					builder.create().show();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}builder.create().show();
			return;
		}
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText((String) object);
	}
}

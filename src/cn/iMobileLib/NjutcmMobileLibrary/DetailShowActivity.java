package cn.iMobileLib.NjutcmMobileLibrary;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.entity.G.NewsType;
import example.mobilelibrary.entity.NewsDetails;

public class DetailShowActivity  extends BaseLoadingActivity {
	private HeaderWidget headerWidget;
	private TextView textView_content = null;
	private TextView textView_title = null;
	private TextView headerTextView = null;
	public static String link = null;
	Intent intent = null;
	private int newsType;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 此句话在这个activity中不能使用，不知什么原因
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		View view = LayoutInflater.from(this).inflate(R.layout.news_details,
				null);
		addRootView(view);
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		headerWidget.setMiddleText("新闻内容");
		headerWidget.setLeftText("返回");
		textView_content = (TextView) findViewById(R.id.desc);
		textView_title = (TextView) findViewById(R.id.title);
		headerTextView = (TextView) findViewById(R.id.left_btn);
		//layout=(LinearLayout)findViewById(R.layout.base_loading.)
		intent = getIntent();
		link = intent.getStringExtra("link");
		System.out.println("------->>link=" + link);

		newsType = getIntent().getIntExtra("newstype", 0);
		asyncRequest(0);
		headerTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});
		
	}

	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isConnected();
	}

	@Override
	public void onMsgStart() {

	}

	@Override
	public void onMsgOk() {

	}

	@Override
	public void onMsgError() {

	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}

	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		try {
			// urlString="http://192.168.1.107:8900/mainService.asmx/getNewsDetail?link=%2F";
			System.out
					.println("---------->>NewsdetailActivity newstype=" + newsType);
			return WebServiceHelper.pullParseXmlDetails(NewsType.values()[newsType],
					link);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onAsyncInUI(Object object) {
		NewsDetails detail = (NewsDetails) object;
		if (detail == null) {
			textView_title.setText(null);
			textView_content.setText(null);
		} else {
		textView_title.setText(detail.title);
		textView_content.setText(detail.content);
		}
		
	}

}

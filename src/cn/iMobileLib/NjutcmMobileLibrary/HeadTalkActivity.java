package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.widget.Button;

public class HeadTalkActivity extends Activity{

	private WebView webView;
	private Button backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headertalk);
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.loadUrl("file:///android_asset/header_talk.html");
		webView.getSettings().setTextSize(TextSize.NORMAL);
		backButton=(Button)findViewById(R.id.HtalkBackButton);
		backButton.setOnClickListener(new BackButtonListener());
	//	webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	}
	
	class BackButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
}

package cn.iMobileLib.NjutcmMobileLibrary;

import mars.ClassUtil.WebScale;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.mobilelibrary.widget.HeaderWidget;

public class FloorDetailActivity extends Activity {
	private HeaderWidget headerWidget = null;
	private RadioGroup group = null;
	private WebView webView = null;
	@SuppressWarnings("unused")
	private WebScale webScale = null;

	public int Backcount = 0;
	public int fullScreenCount = 0;
	// 数据
		private int[] floorUrls = { R.string.floor_detail_1,
				R.string.floor_detail_2, R.string.floor_detail_3,
				R.string.floor_detail_4, R.string.floor_detail_5, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.new_floor_detail);
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		webView = (WebView) findViewById(R.id.webview);
		group = (RadioGroup) findViewById(R.id.group);
		headerWidget.setMiddleText("1F");
		headerWidget.hiddenRightBtn(true);
		headerWidget.setRightText("全屏");
		headerWidget.bindCickToRightBtn(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				fullScreenCount++;
				FloorDetailActivity.this.getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				// hideLayout.setVisibility(View.GONE);
			}
		});
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO 自动生成的方法存根
				RadioButton radioButton = (RadioButton) group
						.findViewById(checkedId);
				if (radioButton.isChecked()) {
					String tag = (String) radioButton.getTag();
					// 加载数据
					webView.loadUrl(getResources().getString(floorUrls[Integer.parseInt(tag) - 1]));
					headerWidget.setMiddleText(tag + "F");
				}
			}
		});
		// 打开使用缓存
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 设置支持缩放
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		// 获取触摸焦点
		webView.requestFocus();
		// 无限缩放
		webView.getSettings().setUseWideViewPort(true);
		// 设置加载进来的页面自适应手机屏幕
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webScale = new WebScale(webView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//
		Log.d("ydy", "DepartDetailActivity>>>onPause");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("ydy", "DepartDetailActivity>>>onStart");
		//初始化时加载webview数据
		webView.loadUrl(getResources().getString(floorUrls[0]));
	}

	@Override
	protected void onResume() {
		super.onResume();
		//
		Log.d("ydy", "DepartDetailActivity>>>onResume");
		//
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("ydy", "DepartDetailActivity>>>onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("ydy", "DepartDetailActivity>>>onDestroy");
	}

/*	public void imgClick(View v) {
		switch (v.getId()) {
		case R.id.scaleMax:
			webScale.zoomIn();
			break;

		case R.id.scaleMin:
			webScale.zoomOut();
			break;
		default:
			break;
		}
	}*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Backcount++;
			if (fullScreenCount == Backcount) // 判断返回条件
			{
				//hideLayout.setVisibility(View.VISIBLE);
				FloorDetailActivity.this.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

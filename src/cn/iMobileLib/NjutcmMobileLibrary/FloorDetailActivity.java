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
	// ����
		private int[] floorUrls = { R.string.floor_detail_1,
				R.string.floor_detail_2, R.string.floor_detail_3,
				R.string.floor_detail_4, R.string.floor_detail_5, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.new_floor_detail);
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		webView = (WebView) findViewById(R.id.webview);
		group = (RadioGroup) findViewById(R.id.group);
		headerWidget.setMiddleText("1F");
		headerWidget.hiddenRightBtn(true);
		headerWidget.setRightText("ȫ��");
		headerWidget.bindCickToRightBtn(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
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
				// TODO �Զ����ɵķ������
				RadioButton radioButton = (RadioButton) group
						.findViewById(checkedId);
				if (radioButton.isChecked()) {
					String tag = (String) radioButton.getTag();
					// ��������
					webView.loadUrl(getResources().getString(floorUrls[Integer.parseInt(tag) - 1]));
					headerWidget.setMiddleText(tag + "F");
				}
			}
		});
		// ��ʹ�û���
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// ����֧������
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		// ��ȡ��������
		webView.requestFocus();
		// ��������
		webView.getSettings().setUseWideViewPort(true);
		// ���ü��ؽ�����ҳ������Ӧ�ֻ���Ļ
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
		//��ʼ��ʱ����webview����
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
		// TODO �Զ����ɵķ������

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Backcount++;
			if (fullScreenCount == Backcount) // �жϷ�������
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

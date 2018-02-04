package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.entity.D;
import example.mobilelibrary.entity.G;

/**
 * 
 * ����
 * 
 * @author Administrator
 * 
 */
public abstract class BaseLoadingActivity extends Activity {

	// ��Ϣ����
	protected final static int MSG_NULL = -1;
	protected final static int MSG_OK = 0;
	protected final static int MSG_ERROR = 1;
	protected final static int MSG_START = 2;
	// widget
	private LinearLayout layoutTip;
	private LinearLayout layoutRoot;
	private LinearLayout layoutLoading;
	private LinearLayout layoutError;
	private HeaderWidget headerWidget;
	// ��һ�������Ƿ�ʧ��
	private boolean firstRequestFalse = false;
	private static String FILE_NAME="example.mobilelibrary";
	// ��ͼ����
	private enum ViewType {
		NORMAL, LOADING, ERROR;
	}
	
	//�ж��Ƿ������������
	// �������
	private Object resultObject;
	private int requestCount = 0;// �������
	private boolean threadLock = false;// ����
	// ��Ϣ���
	private Handler handler;
	public static Boolean gprs=null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.base_loading);
		Log.d("ydy", this.getClass() + ">>>onCreate");
		// ����ʵ����
		layoutTip = (LinearLayout) findViewById(R.id.layout3);
		layoutRoot = (LinearLayout) findViewById(R.id.root);
		layoutLoading = (LinearLayout) findViewById(R.id.layout1);
		layoutError = (LinearLayout) findViewById(R.id.layout2);
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case MSG_START:
					// ҳ���һ�μ��ز��л�ҳ��--���һ�μ���ʧ��ʱ����
					if (requestCount == 1 || firstRequestFalse)
						switchView(ViewType.LOADING);
					threadLock = true;
					onMsgStart();
					break;
				case MSG_OK:
					if (requestCount == 1 || firstRequestFalse)
						switchView(ViewType.NORMAL);
					// reset
					firstRequestFalse = false;
					// ����ص�
					onAsyncInUI(resultObject);
					threadLock = false;// ����
					onMsgOk();
					break;
				case MSG_ERROR:
					//Toast.makeText(BaseLoadingActivity.this, "����ʧ�ܣ����Ժ�����",Toast.LENGTH_SHORT).show();
					// BaseLoadingActivity.this.finish();
					// reset
					// firstRequestFalse = false;
					// threadLock = false;
					// onMsgError();
					return;
				case MSG_NULL:
					//Toast.makeText(BaseLoadingActivity.this, "��������",Toast.LENGTH_SHORT).show();
					System.out.println("-------->>MSG_NULL");
					layoutRoot.getChildAt(1).setVisibility(View.GONE);
					layoutLoading.setVisibility(View.GONE);
					layoutError.setVisibility(View.GONE);
					layoutTip.setVisibility(View.VISIBLE);
					//hiddenLoding(true);
					// BaseLoadingActivity.this.finish();
					return;
				}
			}
		};
	}
	public void hiddenLoding(boolean hidden) {
		if (hidden) {
			layoutLoading.setVisibility(View.INVISIBLE);
		} else {
			layoutLoading.setVisibility(View.VISIBLE);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		//
		Log.d("ydy", this.getClass() + ">>>onPause");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("ydy", this.getClass() + ">>>onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		//
		Log.d("ydy", this.getClass() + ">>>onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("ydy", this.getClass() + ">>>onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("ydy", this.getClass() + ">>>onDestroy");
	}

	/**
	 * �л�ҳ����ͼ
	 * 
	 * @param viewType
	 */
	private void switchView(ViewType viewType) {
		switch (viewType) {
		case NORMAL:
			// getChildAt����Ĳ������ǲ��������ε�����
			layoutRoot.getChildAt(1).setVisibility(View.VISIBLE);
			layoutLoading.setVisibility(View.GONE);
			layoutError.setVisibility(View.GONE);
			break;
		case LOADING:
			layoutRoot.getChildAt(1).setVisibility(View.GONE);
			layoutLoading.setVisibility(View.VISIBLE);
			layoutError.setVisibility(View.GONE);
			break;
		case ERROR:
			layoutRoot.getChildAt(1).setVisibility(View.GONE);
			layoutLoading.setVisibility(View.GONE);
			layoutError.setVisibility(View.VISIBLE);
			break;
		}
	}

	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isConnected();
	}

	/**
	 * �첽��������
	 * 
	 * @param what
	 *            ˵�������ˢ�°�ť--Ĭ�ϲ���Ϊ0
	 */
	public void asyncRequest(final int what) {
		SharedPreferences sharePre=BaseLoadingActivity.this.getSharedPreferences(FILE_NAME, 0);
		gprs=!sharePre.getBoolean(R.id.gprscontrol+"",true);
		if (threadLock) {
			return;
		}
		requestCount++;
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				System.out.println("---->>BaseLoading isWifiConnected="+isWifiConnected());
				
				if ((!isWifiConnected()&&!G.getNetworkType(BaseLoadingActivity.this).equalsIgnoreCase("2G/3G"))||(gprs&&G.getNetworkType(BaseLoadingActivity.this).equalsIgnoreCase("2G/3G"))) {	
					if(!isWifiConnected()&&!G.getNetworkType(BaseLoadingActivity.this).equalsIgnoreCase("2G/3G")){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								//Toast.makeText(BaseLoadingActivity.this, "���������Ƿ�����...", Toast.LENGTH_SHORT).show();
								
							}
						});
						
					}else if(gprs&&G.getNetworkType(BaseLoadingActivity.this).equalsIgnoreCase("2G/3G")){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(BaseLoadingActivity.this, "����������2G/3G�Ƿ�ر�...", Toast.LENGTH_SHORT).show();
							}
						});
						
					}
					handler.sendEmptyMessage(MSG_ERROR);
					resultObject=null;
					handler.sendEmptyMessage(MSG_OK);
				} else {
					handler.sendEmptyMessage(MSG_START);
					try {
						resultObject = onAsyncInBackground(what);
						D.out("BaseLoadingActivity.asyncRequest.resultObject="
								+ resultObject);
						if (resultObject == null) {
							//handler.sendEmptyMessage(MSG_NULL);
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
								//	Toast.makeText(BaseLoadingActivity.this, "���糬ʱ�����Ժ�����...", Toast.LENGTH_SHORT).show();
								}
							});
							handler.sendEmptyMessage(MSG_OK);
						} else {
							handler.sendEmptyMessage(MSG_OK);
						}
					} catch (Exception e) {
						e.printStackTrace();
						String eMsg = e.getMessage();
						D.out("BaseLoadingActivity.asyncRequest.requestError>.>>"
								+ eMsg);
						if (eMsg == null) {
							handler.sendEmptyMessage(MSG_NULL);
						} else {
							handler.sendEmptyMessage(MSG_ERROR);
						}
					}
				}
			}
		}).start();
	}
	/**
	 * ���root����
	 * 
	 * @param view
	 */
	public void addRootView(View view) {
		layoutRoot.addView(view, 1);
	}

	/**
	 * ��ʾû�н����ʾ
	 * 
	 */
	public void showNoneTip() {
		layoutRoot.getChildAt(1).setVisibility(View.GONE);
		layoutLoading.setVisibility(View.GONE);
		layoutError.setVisibility(View.GONE);
		layoutTip.setVisibility(View.VISIBLE);
	}

	/**
	 * ���ر�ͷ�ؼ�
	 * 
	 * @return
	 */
	public HeaderWidget getHeaderView() {
		return headerWidget;
	}

	public void btnClick(View v) {
		firstRequestFalse = true;
		asyncRequest(0);
	}

	/**
	 * ��Ϣ==��ʼ
	 */
	public abstract void onMsgStart();

	/**
	 * ��Ϣ==�ɹ�
	 * 
	 */
	public abstract void onMsgOk();

	/**
	 * ��Ϣ==ʧ��
	 * 
	 */
	public abstract void onMsgError();

	/**
	 * ��̨�߳� asyncRequest(final int what) �����������¼�
	 * 
	 * @param what
	 * @return
	 */
	public abstract Object onAsyncInBackground(int what) throws Exception;

	/**
	 * ��ui�߳��е���---���Ը���UI�����������Ҫ��
	 * 
	 * @param object
	 *            onAsyncInBackground���صĲ���
	 * 
	 */
	public abstract void onAsyncInUI(Object object);
}

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
 * 请求
 * 
 * @author Administrator
 * 
 */
public abstract class BaseLoadingActivity extends Activity {

	// 消息队列
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
	// 第一次请求是否失败
	private boolean firstRequestFalse = false;
	private static String FILE_NAME="example.mobilelibrary";
	// 视图类型
	private enum ViewType {
		NORMAL, LOADING, ERROR;
	}
	
	//判断是否可以连接外网
	// 结果对象
	private Object resultObject;
	private int requestCount = 0;// 请求次数
	private boolean threadLock = false;// 单例
	// 消息句柄
	private Handler handler;
	public static Boolean gprs=null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.base_loading);
		Log.d("ydy", this.getClass() + ">>>onCreate");
		// 变量实例化
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
					// 页面第一次加载才切换页面--或第一次加载失败时调用
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
					// 结果回调
					onAsyncInUI(resultObject);
					threadLock = false;// 解锁
					onMsgOk();
					break;
				case MSG_ERROR:
					//Toast.makeText(BaseLoadingActivity.this, "请求失败，请稍后重试",Toast.LENGTH_SHORT).show();
					// BaseLoadingActivity.this.finish();
					// reset
					// firstRequestFalse = false;
					// threadLock = false;
					// onMsgError();
					return;
				case MSG_NULL:
					//Toast.makeText(BaseLoadingActivity.this, "暂无详情",Toast.LENGTH_SHORT).show();
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
	 * 切换页面视图
	 * 
	 * @param viewType
	 */
	private void switchView(ViewType viewType) {
		switch (viewType) {
		case NORMAL:
			// getChildAt里面的参数就是布局里面层次的索引
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
	 * 异步请求数据
	 * 
	 * @param what
	 *            说明：点击刷新按钮--默认参数为0
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
								//Toast.makeText(BaseLoadingActivity.this, "请检查网络是否连接...", Toast.LENGTH_SHORT).show();
								
							}
						});
						
					}else if(gprs&&G.getNetworkType(BaseLoadingActivity.this).equalsIgnoreCase("2G/3G")){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(BaseLoadingActivity.this, "请检查设置中2G/3G是否关闭...", Toast.LENGTH_SHORT).show();
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
								//	Toast.makeText(BaseLoadingActivity.this, "网络超时，请稍后再试...", Toast.LENGTH_SHORT).show();
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
	 * 添加root容器
	 * 
	 * @param view
	 */
	public void addRootView(View view) {
		layoutRoot.addView(view, 1);
	}

	/**
	 * 显示没有结果提示
	 * 
	 */
	public void showNoneTip() {
		layoutRoot.getChildAt(1).setVisibility(View.GONE);
		layoutLoading.setVisibility(View.GONE);
		layoutError.setVisibility(View.GONE);
		layoutTip.setVisibility(View.VISIBLE);
	}

	/**
	 * 返回表头控件
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
	 * 消息==开始
	 */
	public abstract void onMsgStart();

	/**
	 * 消息==成功
	 * 
	 */
	public abstract void onMsgOk();

	/**
	 * 消息==失败
	 * 
	 */
	public abstract void onMsgError();

	/**
	 * 后台线程 asyncRequest(final int what) 方法触发此事件
	 * 
	 * @param what
	 * @return
	 */
	public abstract Object onAsyncInBackground(int what) throws Exception;

	/**
	 * 在ui线程中调用---可以更新UI操作（如果需要）
	 * 
	 * @param object
	 *            onAsyncInBackground返回的参数
	 * 
	 */
	public abstract void onAsyncInUI(Object object);
}

package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.NewsType;
import example.mobilelibrary.entity.NewsBean;
import example.mobilelibrary.entity.NewsDetails;

public class SocialNewsDetailActivity extends BaseLoadingActivity  implements OnTouchListener, OnGestureListener {
	private HeaderWidget headerWidget;
	private TextView textView_content = null;
	private TextView textView_title = null;
	private TextView headerTextView = null;
	public static String link = null;
	SQLiteDatabase sqLiteDatabase = null;
	Intent intent = null;
	private static String FILE_NAME="example.mobilelibrary";
	
	
	// sun
	private ArrayList<String> linkList = null;
	private int position;
	private int page;
	private GestureDetector mGestureDetector = null;
	private boolean isReady = false;
	private boolean isLoadLink = false;
	private LinearLayout newsDetailLayout = null;
	private List<NewsBean> items = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 此句话在这个activity中不能使用，不知什么原因
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		sqLiteDatabase = openOrCreateDatabase("libraryNewsContent",
				Context.MODE_PRIVATE, null);
		View view = LayoutInflater.from(this).inflate(R.layout.news_details,
				null);
		addRootView(view);
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		headerWidget.setMiddleText("新闻内容");
		textView_content = (TextView) findViewById(R.id.desc);
		textView_title = (TextView) findViewById(R.id.title);
		headerTextView = (TextView) findViewById(R.id.left_btn);


		// sun
		newsDetailLayout = (LinearLayout) view
				.findViewById(R.id.newsDetailLayout);
		mGestureDetector = new GestureDetector((OnGestureListener) this);
		newsDetailLayout.setOnTouchListener(this);
		newsDetailLayout.setLongClickable(true);
		intent = getIntent();
		link = intent.getStringExtra("link");
		linkList = intent.getStringArrayListExtra("linkList");
		page = intent.getIntExtra("page", 1);
		position = intent.getIntExtra("position", 0);
		System.out.println("------->>link=" + link);

		headerTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		asyncRequest(0);
		loadData();
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
		if (sqLiteDatabase != null)
			sqLiteDatabase.close();
	}
	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isConnected();
	}
	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		SharedPreferences sharePre=SocialNewsDetailActivity.this.getSharedPreferences(FILE_NAME, 0);
		if (isWifiConnected()||(sharePre.getBoolean(R.id.gprscontrol+"",true)&&G.getNetworkType(SocialNewsDetailActivity.this).equalsIgnoreCase("2G/3G"))) {
			try {
				// urlString="http://192.168.1.107:8900/mainService.asmx/getNewsDetail?link=%2F";
				long post=System.currentTimeMillis();
				System.out.println("---------->>NewsdetailActivity newstype="
						+ NewsType.values()[getIntent().getIntExtra("newstype",
								0)]);
				NewsDetails details=WebServiceHelper.pullParseXmlDetails(
						NewsType.values()[getIntent()
								.getIntExtra("newstype", 0)], link);
				long now=System.currentTimeMillis();
				System.out.println("SocialNewsDetail Times="+(now-post));
				return details;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}

	}

	@Override
	public void onAsyncInUI(Object object) {
		if (object==null) {
			//ImageView imageView = new ImageView(SocialNewsDetailActivity.this);
			//imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder = new Builder(
					SocialNewsDetailActivity.this);
			builder.setMessage("加载出错啦,请检查网络是否连接...");
			builder.setTitle("提示");
			//builder.setView(imageView);
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			if (!isFinishing()) {
				try {
					builder.create().show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			return;
		}
		try {
			NewsDetails detail = (NewsDetails) object;
			textView_title.setText(detail.title);
			textView_content.setText(detail.content);
		} catch (Exception e) {
			e.printStackTrace();
			//ImageView imageView = new ImageView(SocialNewsDetailActivity.this);
			//imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder = new Builder(
					SocialNewsDetailActivity.this);
			builder.setMessage("加载出错啦...");
			builder.setTitle("提示");
			//builder.setView(imageView);
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			if (!isFinishing()) {
				try {
					builder.create().show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return;
		}

	}

	
	// sun
		private void loadData() {
			if (position == linkList.size() - 1) {
				page++;
				loadLink();
			} else
				isLoadLink = true;
		}

		private void loadLink() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						items = WebServiceHelper.searchNews(
								NewsType.values()[getIntent().getIntExtra(
										"newstype", 0)], page);
						for (int i = 0; i < items.size(); i++)
							linkList.add(items.get(i).getLink());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					isLoadLink = true;
				}
			}).start();
		}

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 0
					&& Math.abs(e1.getY() - e2.getY()) < 50) {
				if (position <= linkList.size() - 2 && isLoadLink) {
					position++;
					link = linkList.get(position);
					isReady = true;
				}
			} else if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 0
					&& Math.abs(e1.getY() - e2.getY()) < 50) {
				if (position > 0 && isLoadLink) {
					position--;
					link = linkList.get(position);
					isReady = true;
				}
			}
			if (isReady)
				skipToOtherActivity();
			return false;
		}

		private void skipToOtherActivity() {
			Intent intent = new Intent();
			intent.putExtras(getIntent());
			intent.putExtra("link", link);
			intent.putExtra("linkList", linkList);
			intent.putExtra("position", position);
			intent.putExtra("page", page);
			intent.setClass(this, SocialNewsDetailCopyActivity.class);
			startActivity(intent);
			finish();
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			// TODO Auto-generated method stub
			mGestureDetector.onTouchEvent(ev);
			return super.dispatchTouchEvent(ev);
		}
}

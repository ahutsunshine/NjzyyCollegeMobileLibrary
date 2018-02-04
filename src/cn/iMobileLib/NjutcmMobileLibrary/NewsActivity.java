package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.ToggleButton;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.NewsType;

public class NewsActivity extends TabActivity {
	private TabHost tabHost = null;
	private RadioGroup group = null;
	private LinearLayout layout_left;

	// 折叠--展开控制变量
	private final static int SPEED = 30;// 每次自动展开/收缩的速度
	private final static int sleep_time = 5;// 休眠时间
	private int MAX_WIDTH = 0;// 每次自动展开/收缩的范围
	private boolean hasMeasured = false;// flag值测量一次
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.news_main);
		// 变量实例化
		layout_left = (LinearLayout) findViewById(R.id.left);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		group = (RadioGroup) findViewById(R.id.group);
		// 折叠初始化
		initMove();
		initTab();
	}

	public void initTab() {
		Intent intent;

		// 图书馆新闻
		intent = new Intent(NewsActivity.this, NewsListActivity.class);
		intent.putExtra("newstype", G.NewsType.LIBRARY.ordinal());
		System.out.println("------->>NewsActivty newstype="+ NewsType.values()[G.NewsType.LIBRARY.ordinal()]);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("tab1").setContent(intent));
		// 学校新闻
		intent = new Intent(this, SchoolNewsListActivity.class);
		intent.putExtra("newstype", G.NewsType.SCHOOL.ordinal());
		System.out.println("------->>NewsActivty newstype="
				+ NewsType.values()[G.NewsType.SCHOOL.ordinal()]);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("tab2")
				.setContent(intent));
		// 社会新闻
		intent = new Intent(this, SocialNewsListActivity.class);
		intent.putExtra("newstype", G.NewsType.SOCIAL.ordinal());
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("tab3")
				.setContent(intent));
		// 绑定事件
		group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio1:
					tabHost.setCurrentTabByTag("tab1");
					break;
				case R.id.radio2:
					tabHost.setCurrentTabByTag("tab2");
					break;
				case R.id.radio3:
					tabHost.setCurrentTabByTag("tab3");
					break;

				default:
					break;
				}

			}
		});
	}

	// ======================================折叠模块============================================================
	private void initMove() {
		// 获取最大高度
		getMAX_WIDTH();
		// 折叠切换按钮
		ToggleButton tButton = (ToggleButton) findViewById(R.id.toggle1);
		tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					new AsynMove().execute(SPEED);
				} else {
					new AsynMove().execute(-SPEED);
				}
			}
		});
	}

	// 获取左边栏的宽度
	private void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_left.getViewTreeObserver();
		// 获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					MAX_WIDTH = layout_left.getWidth();
					// LinearLayout.LayoutParams lpRight =
					// (LinearLayout.LayoutParams) layout_right
					// .getLayoutParams();
					// lpRight.leftMargin = MAX_WIDTH;
					// layout_right.setLayoutParams(lpRight);
					// 标记--只运算一次
					hasMeasured = true;
				}
				return true;
			}
		});
	}

	// 异步折叠--展开
	private class AsynMove extends AsyncTask<Integer, Integer, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout_left
					.getLayoutParams();
			// RelativeLayout.LayoutParams lpRight =
			// (RelativeLayout.LayoutParams) layout_right
			// .getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], MAX_WIDTH);
				// lpRight.leftMargin = Math.min(lpRight.leftMargin + values[0],
				// MAX_WIDTH);
			} else {
				// 左移动
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], -MAX_WIDTH);
				// lpRight.leftMargin = Math
				// .max(lpRight.leftMargin + values[0], 0);
			}
			layout_left.setLayoutParams(layoutParams);
			// layout_right.setLayoutParams(lpRight);
		}
	}
	
}

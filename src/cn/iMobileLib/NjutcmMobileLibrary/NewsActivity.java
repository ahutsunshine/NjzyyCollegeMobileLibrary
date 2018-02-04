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

	// �۵�--չ�����Ʊ���
	private final static int SPEED = 30;// ÿ���Զ�չ��/�������ٶ�
	private final static int sleep_time = 5;// ����ʱ��
	private int MAX_WIDTH = 0;// ÿ���Զ�չ��/�����ķ�Χ
	private boolean hasMeasured = false;// flagֵ����һ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.news_main);
		// ����ʵ����
		layout_left = (LinearLayout) findViewById(R.id.left);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		group = (RadioGroup) findViewById(R.id.group);
		// �۵���ʼ��
		initMove();
		initTab();
	}

	public void initTab() {
		Intent intent;

		// ͼ�������
		intent = new Intent(NewsActivity.this, NewsListActivity.class);
		intent.putExtra("newstype", G.NewsType.LIBRARY.ordinal());
		System.out.println("------->>NewsActivty newstype="+ NewsType.values()[G.NewsType.LIBRARY.ordinal()]);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("tab1").setContent(intent));
		// ѧУ����
		intent = new Intent(this, SchoolNewsListActivity.class);
		intent.putExtra("newstype", G.NewsType.SCHOOL.ordinal());
		System.out.println("------->>NewsActivty newstype="
				+ NewsType.values()[G.NewsType.SCHOOL.ordinal()]);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("tab2")
				.setContent(intent));
		// �������
		intent = new Intent(this, SocialNewsListActivity.class);
		intent.putExtra("newstype", G.NewsType.SOCIAL.ordinal());
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("tab3")
				.setContent(intent));
		// ���¼�
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

	// ======================================�۵�ģ��============================================================
	private void initMove() {
		// ��ȡ���߶�
		getMAX_WIDTH();
		// �۵��л���ť
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

	// ��ȡ������Ŀ��
	private void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_left.getViewTreeObserver();
		// ��ȡ�ؼ����
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
					// ���--ֻ����һ��
					hasMeasured = true;
				}
				return true;
			}
		});
	}

	// �첽�۵�--չ��
	private class AsynMove extends AsyncTask<Integer, Integer, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// ����
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// ������

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
			// ���ƶ�
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], MAX_WIDTH);
				// lpRight.leftMargin = Math.min(lpRight.leftMargin + values[0],
				// MAX_WIDTH);
			} else {
				// ���ƶ�
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

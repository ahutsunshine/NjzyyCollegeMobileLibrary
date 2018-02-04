package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.Adapter.LibraryNewsAdapter;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.NewsType;
import example.mobilelibrary.entity.NewsBean;

public class SocialNewsListActivity extends BaseListActivity {
	SQLiteDatabase newsDatabase = null;
	private ListView listView;
	private HeaderWidget headerWidget;
	private LibraryNewsAdapter adapter;
	private ArrayList<NewsBean> datas = null;
	private int page = 1;
	public static Boolean isConnected = null;
	private ArrayList<String> linkList = new ArrayList<String>();
	private static String FILE_NAME="example.mobilelibrary";
	private int clicknum=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

		// 前面已经设有标题栏了，所以要隐藏它
		headerWidget = getHeaderView();
		headerWidget.setVisibility(View.GONE);

		listView = getListView();
		datas = new ArrayList<NewsBean>();
		adapter = new LibraryNewsAdapter(this, datas);
		listView.setAdapter(adapter);
		asyncRequest(0);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 可能因为添加了 headerview的缘故
				linkList.clear();
				for (int i = 0; i < adapter.getCount(); i++) {
					linkList.add(adapter.getItem(i).getLink());
				}
				String link = linkList.get(position - 1);
				Intent intent = new Intent();
				intent.putExtras(getIntent());
				intent.putExtra("link", link);
				intent.putExtra("position", position - 1);
				intent.putExtra("page", page);
				intent.putExtra("linkList", linkList);
				intent.setClass(SocialNewsListActivity.this,
						SocialNewsDetailActivity.class);
				System.out.println("------->>NewsListActivity link=" + link);
				startActivity(intent);
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
	public void onRefresh() {
		asyncRequest(0);
		page = 1;

	}

	@Override
	public void onLoadMore() {
		page++;
		asyncRequest(1);

	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		// newsDatabase.close();
	}

	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		System.out.println("----->>isConnected=" + isConnected);
		SharedPreferences sharePre=SocialNewsListActivity.this.getSharedPreferences(FILE_NAME, 0);
		if (isWifiConnected()||(sharePre.getBoolean(R.id.gprscontrol+"",true)&&G.getNetworkType(SocialNewsListActivity.this).equalsIgnoreCase("2G/3G"))) {
			try {
				long post=System.currentTimeMillis();
				System.out
						.println("---------->>SocialNewsListActivity newstype="
								+ NewsType.values()[getIntent().getIntExtra(
										"newstype", 0)]);
				List<NewsBean> socialList=WebServiceHelper.searchNews(
						NewsType.values()[getIntent()
											.getIntExtra("newstype", 0)], page);
				long now=System.currentTimeMillis();
				System.out.println("SocialNewsList Times="+(now-post));
				return socialList;

			} catch (Exception e) {
				return null;
			}

		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void onAsyncInUI(Object object) {
		if (object == null) {
			//ImageView imageView = new ImageView(SocialNewsListActivity.this);
			//imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder = new Builder(
					SocialNewsListActivity.this);
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
			@SuppressWarnings("unchecked")
			ArrayList<NewsBean> datasTemp = (ArrayList<NewsBean>) object;
			if (page == 1) {
				datas.clear();
			}
			if (datasTemp == null) {
				return;
			}
			datas.addAll(datasTemp);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
			//ImageView imageView = new ImageView(SocialNewsListActivity.this);
			//imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder = new Builder(
					SocialNewsListActivity.this);
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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		clicknum=0;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(clicknum==0){
			Toast.makeText(getApplicationContext(), "再按一下返回键退出程序!",
					Toast.LENGTH_SHORT).show();	
			clicknum++;
			}
			else finish();
			return true;
		}	
		else
		return super.onKeyDown(keyCode, event);		
	}
}

package cn.iMobileLib.NjutcmMobileLibrary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import example.defineView.XListView;
import example.defineView.XListView.IXListViewListener;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.NewBookList;
import example.mobilelibrary.http.XmlWebService;

public class NewBookActivity extends Activity implements IXListViewListener {
	private XListView bookListView;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter mAdapter;
	private LinearLayout layoutLoading;
	private List<NewBookList> books = null;
	private ArrayList<String> link = new ArrayList<String>();
	private int start = 1;
	private int flag = 0;
	private Button newBookbutton = null;
	private Button backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_book_list);
		bookListView = (XListView) findViewById(R.id.newbookList_1);
		bookListView.setVisibility(View.GONE);
		layoutLoading = (LinearLayout) findViewById(R.id.tbar_4);
		layoutLoading.setVisibility(View.VISIBLE);
		newBookbutton = (Button) findViewById(R.id.newBookbutton);
		backButton = (Button) findViewById(R.id.NbBackButton);
		newBookbutton.setOnClickListener(new ButtonListener());
		backButton.setOnClickListener(new BackButtonListener());
		bookListView.setPullLoadEnable(true);
		new Thread(new Runnable() {
			public void run() {
				try {
					geneItems();
					mHandler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				mHandler.post(runnableUi);
			}
		}).start();
		bookListView.setOnItemClickListener(new ClickItem());
		bookListView.setXListViewListener(this);
		// System.out.println("dddddd");

	}

	class BackButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}

	}

	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			bookListView.setSelection(0);
			bookListView.setAdapter(mAdapter);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				layoutLoading.setVisibility(View.GONE);
				bookListView.setVisibility(View.VISIBLE);
			}
		}
	};
	Runnable runnableUi = new Runnable() {

		@Override
		public void run() {
			if (G.Network && list != null) {
				mAdapter = new SimpleAdapter(getApplicationContext(), list,R.layout.new_book_list_1, new String[] { "title","information" }, new int[] {
								R.id.newbook_title, R.id.newbook_infor });
				bookListView.setAdapter(mAdapter);
			} else if (!G.Network)
				Toast.makeText(getApplicationContext(), "2G/3G网络已关闭",
						Toast.LENGTH_SHORT).show();
			else if (list == null && G.Network)
				G.abnormal(flag, getApplicationContext());
		}
	};

	class ClickItem implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			Intent intent = new Intent();
			intent.putExtra("link", link.get(arg2 - 1));
			intent.putStringArrayListExtra("linkList", link);
			intent.putExtra("position", arg2 - 1);
			intent.putExtra("page", start);
			intent.putExtra("flag", "NewBookActivity");
			intent.setClass(NewBookActivity.this, NewBookDetailActivity.class);
			NewBookActivity.this.startActivity(intent);
		}

	}

	private void geneItems() {
		try {
			//如果网络不通则会没有返回值出现异常
			books = XmlWebService.newbooklist(start, getApplicationContext());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			if (e.getMessage() != null)
				flag = 1;
			System.out.println("perhaps there is something wrong in the network!");
		}
		if (start == 1)
			list.clear();
		if (books != null)
			for (int i = 0; i < books.size(); i++) {
				NewBookList book = books.get(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("title", book.title);
				// System.out.println(book.title);
				map.put("information", book.information);
				// System.out.println(book.information);
				link.add("item.php?marc_no=" + book.marc_no);
				list.add(map);
			}
		else
			list = null;
	}

	// private class LoadData extends
	// AsyncTask<Integer,Integer,List<NewBookList>>{
	//
	// @Override
	// protected List<NewBookList> doInBackground(Integer... params) {
	// // TODO 自动生成的方法存根
	//
	// return null;
	// }
	// }
	private void onLoad() {
		bookListView.stopRefresh();
		bookListView.stopLoadMore();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		bookListView.setRefreshTime(sdf.format(new Date()));
		// bookListView.setRefreshTime();
	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		new Thread(new Runnable() {
			@Override
			public void run() {
				start = 1;
				books.clear();
				link.clear();
				try {
					geneItems();
					mAdapter.notifyDataSetChanged();
					onLoad();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					if (e.getMessage() != null)
						flag = 1;
					else
						flag = 0;
				}

				/*runOnUiThread(new Runnable() {
					public void run() {
						mAdapter.notifyDataSetChanged();
						onLoad();
					}
				});*/

			}
		}).start();

	}

	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		new Thread() {
			@Override
			public void run() {
				try {
					start++;
					geneItems();
					mAdapter.notifyDataSetChanged();
					onLoad();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				// mHandler.post(runnableUi);
				/*runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						mAdapter.notifyDataSetChanged();
						onLoad();
					}
				});*/

			};
		}.start();

		// mHandler.post(new Runnable() {
		// @Override
		// public void run() {
		// try {
		// start++;
		// geneItems();
		// } catch (Exception e) {
		// // TODO 自动生成的 catch 块
		// e.printStackTrace();
		// }
		// mAdapter.notifyDataSetChanged();
		// onLoad();
		// }
		// });
	}

}

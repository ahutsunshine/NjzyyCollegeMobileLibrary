package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import example.defineView.XListView;
import example.defineView.XListView.IXListViewListener;
import example.mobilelibrary.entity.News;
import example.mobilelibrary.entity.NewsDetail;
import example.mobilelibrary.http.XmlWebService;

public class NewsTextActivity extends Activity implements IXListViewListener {
	private XListView mListView;
	ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
	private SimpleAdapter mAdapter;
	private List<News> items = null;
	private LinearLayout layoutLoading;
	List<String> url=new ArrayList<String>();
	
	private int start = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_text);
		mListView = (XListView) findViewById(R.id.xListView);
		 mListView.setPullLoadEnable(true);
		 mListView.setVisibility(View.GONE);
		 layoutLoading=(LinearLayout)findViewById(R.id.tbar_2);
		 layoutLoading.setVisibility(View.VISIBLE);
	 new Thread(new Runnable() {
		 public void run() {
		try {
			geneItems();
			mHandler.sendEmptyMessage(1);
		} catch (XmlPullParserException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	
		mHandler.post(runnableUi);
		 }
		 }).start();
		
	 	
		mListView.setOnItemClickListener(new ItemClickEvent());
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		
		
	}
	private Handler mHandler=new Handler(){
		 @Override
		 public void handleMessage(Message msg) {
		   // TODO Auto-generated method stub
		   if (msg.what==1) {
		    layoutLoading.setVisibility(View.GONE); 
		    mListView.setVisibility(View.VISIBLE);
		   }
	}
	};
	Runnable runnableUi=new Runnable(){

		@Override
		public void run() {
			mAdapter = new SimpleAdapter(getApplicationContext(),list, R.layout.new_text1,new String[]{"title","date"},
					new int[]{R.id.title,R.id.date});
			mListView.setAdapter(mAdapter);
		}
		};
	class ItemClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			String urlDetail=url.get(arg2);
			try {
				NewsDetail Detail=XmlWebService.Newsdetail(urlDetail);
				Intent intent=new Intent();
				intent.putExtra("detail", Detail.detail);
				intent.putExtra("title", Detail.title);
				
				intent.setClass(NewsTextActivity.this,NewDetailActivity.class);
				NewsTextActivity.this.startActivity(intent);
			} catch (XmlPullParserException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		} 
		
	}
	private void geneItems() throws XmlPullParserException, IOException {
		items=XmlWebService.parse(start);
		for(int i=0;i<items.size();i++){
			News news=items.get(i);
			HashMap<String,String> map=new HashMap<String,String>();
			map.put("title",news.title);
			map.put("date", news.date);
			url.add(news.link);
			list.add(map);			
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	//	mListView.setRefreshTime("鍒氬垰");
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = 1;
				items.clear();
				list.clear();
				try {
					geneItems();
				} catch (XmlPullParserException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				// mAdapter.notifyDataSetChanged();
				mAdapter = new SimpleAdapter(getApplicationContext(),list, R.layout.new_text1,new String[]{"title","date"},
						new int[]{R.id.title,R.id.date});
				mListView.setAdapter(mAdapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					start++;
					geneItems();
				} catch (XmlPullParserException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	 @Override  
	    protected void onPause() {  
	        super.onPause();  
	        // Another activity is taking focus (this activity is about to be "paused").  
	        start=1;
	        
	    }  
	

}

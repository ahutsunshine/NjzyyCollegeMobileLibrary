package cn.iMobileLib.NjutcmMobileLibrary;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import example.mobilelibrary.Adapter.ButtonListAdapter;
import example.mobilelibrary.entity.BookList;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.http.XmlWebService;


public class SearchBookActivity extends Activity{
	public static int totalNum=0;	
	public static int thisPage = 1;
	public static int thisPageNum = 0;
	public static final int numPerPage = 20;
	ListView lvList;
	ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
	private ButtonListAdapter mAdapter;
//	private List<BookList>  books=null;
	private String Str;
	private int page=1;
	View footerView;
	ArrayList<String> url=new ArrayList<String>();
	LinearLayout footerNextPage;
	TextView footerText;
	//sun
	private Button newBookbutton=null;
	private Button backButton;
	private int flag=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bsearch_result_list);
		lvList = (ListView) findViewById(R.id.lvList);
		footerView = getLayoutInflater().inflate(R.layout.list_footer, null);		
		footerNextPage = (LinearLayout) footerView.findViewById(R.id.layoutNextPage);
		footerText = (TextView) footerView.findViewById(R.id.tvFooter);
		lvList.addFooterView(footerView, null, false);
		backButton=(Button)findViewById(R.id.back);
		backButton.setOnClickListener(new BackButtonListener());
		newBookbutton=(Button)findViewById(R.id.newBookbutton);
		newBookbutton.setOnClickListener(new ButtonListener());
	//	mAdapter = new SimpleAdapter(getApplicationContext(),list, R.layout.bsearch_result,new String[]{"listName","language","storeNum","outNum"},
		//		new int[]{R.id.book_title,R.id.book_language,R.id.book_storeNum,R.id.book_outNum});
		mAdapter=new ButtonListAdapter(getApplicationContext(),list);
		lvList.setAdapter(mAdapter);
		Intent intent=getIntent();
		Str=intent.getStringExtra("keyword");
		lvList.setOnItemClickListener(new ItemClick());
		new LoadData().execute();
	}
	class BackButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	class ItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO 自动生成的方法存根
			String urldetail=url.get(arg2);
			Intent intent=new Intent();
			intent.putExtra("link",urldetail);
			intent.putExtra("linkList",url);
			intent.putExtra("position", arg2);
			intent.putExtra("page", page);
			intent.putExtra("keyWord", Str);
			intent.setClass(SearchBookActivity.this,BookDetailActivity.class);
			SearchBookActivity.this.startActivity(intent);
		}
		
	}
	public void showData() {	
	
		footerText.setText("共" + totalNum + "条记录," + "当前第" + thisPage + "页");
		if((thisPage - 1) * numPerPage + thisPageNum < totalNum) {
			footerNextPage.setVisibility(View.VISIBLE);
			footerNextPage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					new LoadMoreData().execute();
				}
			});
		} else {
			footerNextPage.setVisibility(View.GONE);
			footerNextPage.setOnClickListener(null);
		}
		mAdapter.notifyDataSetChanged();
					
	}
	private class LoadData extends AsyncTask<Integer,Integer,List<BookList>>{

		ProgressDialog dialog;
		@Override
		protected List<BookList> doInBackground(Integer... arg0) {
			// TODO 自动生成的方法存根
			
			try {
				List<BookList> bookLists=XmlWebService.searchBook(Str, page, getApplicationContext());

				return bookLists;
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				if(e.getMessage()!=null) 
					flag=1;	
				else flag=0;
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO 自动生成的方法存根
			super.onPreExecute();
			//dialog=ProgressDialog.show(SearchBookActivity.this, "请稍等", "载入中");
			dialog=new ProgressDialog(SearchBookActivity.this);
			dialog.setMessage("请求中...");
		    dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(List<BookList> result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			dialog.dismiss();				
			if(result!=null)
			{		
				list.clear();	
				for(int i=0;i<result.size();i++){
					BookList book=result.get(i);
					HashMap<String,String> map=new HashMap<String,String>();	
					map.put("listName", book.listName);
					map.put("language", book.language);
					map.put("storeNum", book.storeNum);
					map.put("outNum",book.outNum);
					url.add(book.urlDetail);
					list.add(map);
				}	
				showData();
			}
			
			else if(G.Network&&result==null) G.abnormal(flag, getApplicationContext());
			else Toast.makeText(getApplicationContext(), "2G/3G网络已关闭",
				     Toast.LENGTH_SHORT).show();
		}
		}
		private class LoadMoreData extends AsyncTask<Integer, Integer, List<BookList>> {
			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				dialog=new ProgressDialog(SearchBookActivity.this);
				dialog.setMessage("请求中...");
			    dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
			@Override
			protected List<BookList> doInBackground(Integer... params) {
				try {
					page++;
					List<BookList> bookLists=XmlWebService.searchBook(Str, page, getApplicationContext());


					return bookLists;
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					return null;
				} 
			}
			@Override
			protected void onPostExecute(List<BookList> result) {
				// TODO 自动生成的方法存根
				super.onPostExecute(result);
				dialog.dismiss();
				if(result!=null)
				{
					for(int i=0;i<result.size();i++){
						BookList book=result.get(i);
						HashMap<String,String> map=new HashMap<String,String>();	
						map.put("listName", book.listName);
						map.put("language", book.language);
						map.put("storeNum", book.storeNum);
						map.put("outNum",book.outNum);
						url.add(book.urlDetail);
						list.add(map);
					}
					showData();
				}
			}
		}	
		
		
		//sun
		class ButtonListener implements OnClickListener{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lvList.setSelection(0);
				lvList.setAdapter(mAdapter);
			}
			
		}
/*		public void orderClick(View view){
			new AlertDialog.Builder(SearchBookActivity.this).setTitle("提示").setMessage("借阅规则不允许，不得预约")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					dialog.dismiss();
				}
			}).create().show();
		}*/
}

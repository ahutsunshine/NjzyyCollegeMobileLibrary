package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.entity.G.NewsType;
import example.mobilelibrary.entity.NewsBean;
import example.mobilelibrary.entity.NewsDetails;

public class SchoolNewsDetailCopyActivity extends BaseLoadingActivity  implements OnTouchListener, OnGestureListener{
	private HeaderWidget headerWidget;
	private TextView textView_content = null;
	private TextView textView_title = null;
	private TextView headerTextView = null;
	public static String link = null;
	SQLiteDatabase sqLiteDatabase = null;
	Intent intent = null;
	public static Boolean isConnected =true;

	
	//sun
	private ArrayList<String>linkList=null;
	private int position;
	private int page;
	private GestureDetector mGestureDetector=null; 
	private boolean isReady=false;
	private boolean isLoadLink=false;
	private LinearLayout newsDetailLayout=null;
	private List<NewsBean> items =null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//此句话在这个activity中不能使用，不知什么原因
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		sqLiteDatabase = openOrCreateDatabase("schoolNewsContent.db",
				Context.MODE_PRIVATE, null);
		View view = LayoutInflater.from(this).inflate(R.layout.news_details,
				null);
		addRootView(view);
		
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		headerWidget.setMiddleText("新闻内容");
		headerWidget.setLeftText("返回");
		textView_content = (TextView) findViewById(R.id.desc);
		textView_title = (TextView) findViewById(R.id.title);
		headerTextView = (TextView) findViewById(R.id.left_btn);
		
		
		//sun
		newsDetailLayout=(LinearLayout)view.findViewById(R.id.newsDetailLayout);
		mGestureDetector = new GestureDetector((OnGestureListener) this);
		newsDetailLayout.setOnTouchListener(this);
		newsDetailLayout.setLongClickable(true);
		intent = getIntent();
		link = intent.getStringExtra("link");
		linkList=intent.getStringArrayListExtra("linkList");
		page=intent.getIntExtra("page", 1);
		position=intent.getIntExtra("position", 0);
		System.out.println("------->>link=" + link);
		asyncRequest(0);
		sqLiteDatabase
				.execSQL("create table if not exists schNewsContent(ID integer primary key autoincrement,Title varchar,Content varchar,NewsID varchar)");

		headerTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});
		new Thread() {
			@Override
			public void run() {
				isConnected = WebServiceHelper
						.getNetWorkInfo(SchoolNewsDetailCopyActivity.this);
				asyncRequest(0);
			}
		}.start();
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
		//sqLiteDatabase.close();
	}


	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		try {
			// urlString="http://192.168.1.107:8900/mainService.asmx/getNewsDetail?link=%2F";
			System.out.println("---------->>NewsdetailActivity newstype="+NewsType.values()[getIntent().getIntExtra("newstype", 0)]);
			return WebServiceHelper.pullParseXmlDetails(
					NewsType.values()[getIntent().getIntExtra("newstype", 0)],
					link);
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public void onAsyncInUI(Object object) {
		ContentValues values;
		if (!isConnected) { // 没有连接网络
			// 首先判断表中内容不为空，否则仅有cursor.getCount()没有意义会报错
			// 先加载缓存
			try {
				Cursor csCursor = sqLiteDatabase.rawQuery(
						"select * from schNewsContent where NewsID=?",
						new String[] { link });
				if (csCursor != null && csCursor.getCount() != 0) {
					// 使用cursor时，注意初始位置，是从下标为-1的地方开始的,调用Cursor的一个方法moveToFirst(),将Index移动到第一位上才可以
					// csCursor.moveToFirst();或者使用while(csCursor.moveToNext())
					while (csCursor.moveToNext()) {
						textView_title.setText(csCursor.getString(csCursor
								.getColumnIndex("Title")));
						textView_content.setText(csCursor.getString(csCursor
								.getColumnIndex("Content")));
						Log.d("ydy",
								"---->>Title="
										+ csCursor.getString(csCursor
												.getColumnIndex("Title")));
						Log.d("ydy", "----->>缓存加载成功，共" + csCursor.getCount()
								+ "条...");
					}
				} else {
					textView_title.setText(null);
					textView_content.setText(null);
					Toast.makeText(SchoolNewsDetailCopyActivity.this, "请检查网络是否连接...",
							Toast.LENGTH_SHORT).show();
				}
				csCursor.close();
			} catch (Exception e) {
				e.printStackTrace();
				//ImageView imageView = new ImageView(SchoolNewsDetailCopyActivity.this);
				//imageView.setImageResource(R.drawable.sorry);
				AlertDialog.Builder builder = new Builder(
						SchoolNewsDetailCopyActivity.this);
				builder.setMessage("加载出错啦...");
				builder.setTitle("提示");
				//builder.setView(imageView);
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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

		} else {
			try {
				NewsDetails detail = (NewsDetails) object;
				textView_title.setText(detail.title);
				textView_content.setText(detail.content);
				values = new ContentValues();

				// 首先判断表中内容不为空，否则仅有cursor.getCount()语句会报错
				// 进行缓存
				Cursor cursor = sqLiteDatabase.rawQuery(
						"select * from schNewsContent where NewsID=?",
						new String[] { link });

				// 定要加上判断为空条件，否则最初初始化时会报错
				if (cursor == null || cursor.getCount() == 0) {
					values.put("Title", detail.title);
					values.put("Content", detail.content);
					values.put("NewsID", link);
					sqLiteDatabase.insert("schNewsContent", null, values);
				}

				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
				//ImageView imageView = new ImageView(SchoolNewsDetailCopyActivity.this);
				//imageView.setImageResource(R.drawable.sorry);
				AlertDialog.Builder builder = new Builder(
						SchoolNewsDetailCopyActivity.this);
				builder.setMessage("加载出错啦...");
				builder.setTitle("提示");
				//builder.setView(imageView);
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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

	}
	
	
	
	//sun
	private void loadData(){
	if(position==linkList.size()-1){
		page++;
		loadLink();
		}
	else
		isLoadLink=true;
  }

	private void loadLink(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					items=WebServiceHelper.searchNews(NewsType.values()[getIntent().getIntExtra("newstype", 0)], page);
					for(int i=0;i<items.size();i++)
						linkList.add(items.get(i).getLink());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isLoadLink=true;
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
		if(e1.getX()-e2.getX()>50&&Math.abs(velocityX)>0&&Math.abs(e1.getY()-e2.getY())<50){
			if(position<=linkList.size()-2&&isLoadLink){
				position++;
				link=linkList.get(position);
				isReady=true;
			}
		}
		else if(e2.getX()-e1.getX()>50&&Math.abs(velocityX)>0&&Math.abs(e1.getY()-e2.getY())<50){
			if(position>0&&isLoadLink){
				position--;
				link=linkList.get(position);
				isReady=true;
			}
		}
		if(isReady)
			skipToOtherActivity();
		return false;
	}
	
	private void skipToOtherActivity(){
		Intent intent=new Intent();
		intent.putExtras(getIntent());
		intent.putExtra("link", link);
		intent.putExtra("linkList", linkList);
		intent.putExtra("position", position);
		intent.putExtra("page", page);
		intent.setClass(this, SchoolNewsDetailCopyActivity.class);
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

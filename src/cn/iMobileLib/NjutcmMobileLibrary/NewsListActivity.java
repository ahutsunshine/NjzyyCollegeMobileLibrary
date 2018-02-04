package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.Adapter.LibraryNewsAdapter;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.NewsType;
import example.mobilelibrary.entity.NewsBean;

public class NewsListActivity extends BaseListActivity {


	SQLiteDatabase newsDatabase = null;
	private ListView listView;
	private HeaderWidget headerWidget;
	private LibraryNewsAdapter adapter;
	private ArrayList<NewsBean> datas = null;
	private int page = 1;
	private ArrayList<String> linkList = new ArrayList<String>();
	private static String FILE_NAME="example.mobilelibrary";
	public int clicknum=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������

		// �����������ݿ�
		newsDatabase = openOrCreateDatabase("libaryNews.db",Context.MODE_PRIVATE, null);
		newsDatabase.execSQL("create table if not exists libNews (ID integer primary key autoincrement,Title varchar,Pubdate char(15),Abstract varchar,NewsID varchar)");

		// ǰ���Ѿ����б������ˣ�����Ҫ������
		
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
				// ������Ϊ����� headerview��Ե��
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
				intent.setClass(NewsListActivity.this, NewsDetailActivity.class);
				System.out.println("------->>NewsListActivity link=" + link);
				startActivity(intent);
			}
		});

	}
	
	@Override
	public void onRefresh() {
		asyncRequest(0);
		page = 1;
	}

	@Override
	public void onLoadMore() {
		SharedPreferences sharePre=NewsListActivity.this.getSharedPreferences(FILE_NAME, 0);
		if (isWifiConnected()||(sharePre.getBoolean(R.id.gprscontrol+"",true)&&G.getNetworkType(NewsListActivity.this).equalsIgnoreCase("2G/3G"))) {
			page++;
			asyncRequest(1);
		}else {
			//System.out.println("���������Ƿ�����...");
			asyncRequest(0);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO �Զ����ɵķ������
		super.onDestroy();
		newsDatabase.close();
	}

	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		SharedPreferences sharePre=NewsListActivity.this.getSharedPreferences(FILE_NAME, 0);
		if (isWifiConnected()||(sharePre.getBoolean(R.id.gprscontrol+"",true)&&G.getNetworkType(NewsListActivity.this).equalsIgnoreCase("2G/3G"))) {
			System.out.println("---->>onAsyncInBackground  start");		
				List<NewsBean> list= WebServiceHelper.searchNews(NewsType.values()[getIntent().getIntExtra("newstype", 0)], page);
				//���ڵ���
				if(list!=null)
				System.out.println("��һ������:"+list.get(0));
				return list;

		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void onAsyncInUI(Object object) {
		try {
			if(object==null){
				System.out.println("����Ϊ��");
				return;
			}
			ContentValues values;
			Cursor csCursor = newsDatabase.rawQuery("select * from libNews",null);
			List<NewsBean> items = new ArrayList<NewsBean>();

			// û����������ػ���
			SharedPreferences sharePre=NewsListActivity.this.getSharedPreferences(FILE_NAME, 0);
			if ((!isWifiConnected()&&!G.getNetworkType(NewsListActivity.this).equalsIgnoreCase("2G/3G"))||(!sharePre.getBoolean(R.id.gprscontrol+"",true)&&G.getNetworkType(NewsListActivity.this).equalsIgnoreCase("2G/3G"))) {
				
				// �����жϱ������ݲ�Ϊ�գ��������cursor.getCount()�ᱨ��
				// �ȼ��ػ���
				if (page == 1) {
					datas.clear();
				}
				if (csCursor != null && csCursor.getCount() != 0) {

					while (csCursor.moveToNext()) {
						NewsBean item = new NewsBean();
						item.setTitle(csCursor.getString(csCursor.getColumnIndex("Title")));
						item.setDate(csCursor.getString(csCursor.getColumnIndex("Pubdate")));
						item.setContent(csCursor.getString(csCursor.getColumnIndex("Abstract")));
						item.setLink(csCursor.getString(csCursor.getColumnIndex("NewsID")));
						Log.d("ydy","---->>Title="+ csCursor.getString(csCursor.getColumnIndex("Title")));
						items.add(item);
					}
					csCursor.close();
					Log.d("ydy", "----->>׼�����ػ��棬��" + csCursor.getCount()+ "��...");
				} else {
					csCursor.close();
				}
				if(items==null){
					Log.d("items", "itemsΪ��");
				}
				else {
					datas.addAll(items);
				}

			} else {
				if (page == 1) {
					datas.clear();
				}
				// �����жϱ������ݲ�Ϊ�գ��������cursor.getCount()���ᱨ��
				// �������л���
				@SuppressWarnings("unchecked")
				ArrayList<NewsBean> datasTemp = (ArrayList<NewsBean>) object;
				for (NewsBean libNewsBean : datasTemp) {
					String NewsId = libNewsBean.getLink();
					Cursor cursor = newsDatabase.rawQuery("select * from libNews where NewsID=?",new String[] { NewsId });

					// �жϱ����Ƿ��Ѿ�����������棬��������򲻲���
					if (csCursor == null || cursor.getCount() == 0) {
						values = new ContentValues();
						values.put("Title", libNewsBean.getTitle());
						values.put("Pubdate", libNewsBean.getDate());
						values.put("Abstract", libNewsBean.getContent());
						values.put("NewsID", libNewsBean.getLink());
						Log.d("ydy", "---->>Tilte=" + libNewsBean.getTitle()+ "  NewsID=" + libNewsBean.getLink());
						newsDatabase.insert("libNews", null, values);
					}
					cursor.close();
				}

				datas.addAll(datasTemp);
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ImageView imageView = new ImageView(NewsListActivity.this);
		//	imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder = new Builder(NewsListActivity.this);
			builder.setMessage("���س�����...");
			builder.setTitle("��ʾ");
			builder.setView(imageView);
			builder.setPositiveButton("ȷ��",
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
		// TODO �Զ����ɵķ������
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(clicknum==0){
			Toast.makeText(getApplicationContext(), "�ٰ�һ�·��ؼ��˳�����!",
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

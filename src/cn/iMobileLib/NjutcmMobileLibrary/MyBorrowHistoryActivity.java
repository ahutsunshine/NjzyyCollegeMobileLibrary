package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.HashMap;

import mars.XMLPullService.UserInfoPullService;
import mars.getdata.GetDataFromWeb;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyBorrowHistoryActivity extends ListActivity{
	private ArrayList<HashMap<String,String>>list=null;
	private ArrayList<String>linkList=new ArrayList<String>();
	private LinearLayout myBorrowHistoryLayout=null;
	private LinearLayout myBorrowHistoryNoneLinearLayout=null;
	private LinearLayout myBorrowHistoryWaitLayout=null;
	private Button myBorrowHistoryBackButton=null;
	private SimpleAdapter adapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myborrowhistory);
		myBorrowHistoryLayout=(LinearLayout)findViewById(R.id.myBorrowHistoryLayout);
		myBorrowHistoryNoneLinearLayout=(LinearLayout)findViewById(R.id.myBorrowHistoryNoneLinearLayout);
		myBorrowHistoryWaitLayout=(LinearLayout)findViewById(R.id.myBorrowHistoryWaitLayout);
		myBorrowHistoryBackButton=(Button)findViewById(R.id.myBorrowHistoryBackButton);
		myBorrowHistoryBackButton.setOnClickListener(new ButtonListener());
		runThread();
	}
	
	class ButtonListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
	}
	
	public void linkClick(View view){
		setSelection(0);
		setListAdapter(adapter);
	}
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				myBorrowHistoryWaitLayout.setVisibility(View.GONE);
				if(msg.arg1==1){
					adapter=new SimpleAdapter(MyBorrowHistoryActivity.this,list,R.drawable.borrowbook,new String[]{"title","borrowDate","returnDate",""},new int[]{R.id.borrowBookTitle,R.id.bookBorrowDate,R.id.borrowReturnDate});
					setListAdapter(adapter);
					if(adapter.getCount()!=0)
						myBorrowHistoryLayout.setVisibility(View.VISIBLE);
					else
						myBorrowHistoryNoneLinearLayout.setVisibility(View.VISIBLE);
				}
				else{
					myBorrowHistoryNoneLinearLayout.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	private void runThread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg=handler.obtainMessage(1);
				msg.arg1=1;
				try {
					getData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.arg1=0;
				}finally{
					msg.sendToTarget();
				}
			}
		}).start();
	}
	private void getData() throws Exception{
		list=UserInfoPullService.getBorrowHistoryBookDetail(GetDataFromWeb.getInputStream("http://elib.njutcm.edu.cn:8080/mainService.asmx/getBorrowHistory?password="+MenuActivity.passWord+"&userCode="+MenuActivity.userCode));
        if(list!=null)
			for(int i=0;i<list.size();i++)
				linkList.add("item.php?marc_no="+list.get(i).get("marc_no"));
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
			// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.putExtra("link", linkList.get(position));
		intent.putExtra("linkList", linkList);
		intent.putExtra("position",position);
		intent.putExtra("flag", "MyBorrowHistoryActivity");
		intent.setClass(MyBorrowHistoryActivity.this, NewBookDetailActivity.class);
		MyBorrowHistoryActivity.this.startActivity(intent);
	}
}

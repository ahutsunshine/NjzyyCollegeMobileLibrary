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

public class MyCurrentBorrowActivity extends ListActivity{
	private ArrayList<HashMap<String,String>>list=null;
	private ArrayList<String>linkList=new ArrayList<String>();
	private LinearLayout myCurrentBorrowLayout=null;
	private LinearLayout myCurrentBorrowNoneLinearLayout=null;
	private LinearLayout myCurrentBorrowWaitLayout=null;
	private Button myCurrentBorrowBackButton=null;
	private SimpleAdapter adapter=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycurrentborrow);
		myCurrentBorrowLayout=(LinearLayout)findViewById(R.id.myCurrentBorrowLayout);
		myCurrentBorrowNoneLinearLayout=(LinearLayout)findViewById(R.id.myCurrentBorrowNoneLinearLayout);
		myCurrentBorrowWaitLayout=(LinearLayout)findViewById(R.id.myCurrentBorrowWaitLayout);
		myCurrentBorrowBackButton=(Button)findViewById(R.id.myCurrentBorrowBackButton);
		myCurrentBorrowBackButton.setOnClickListener(new ButtonListener());
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
				myCurrentBorrowWaitLayout.setVisibility(View.GONE);
				if(msg.arg1==1){
					adapter=new SimpleAdapter(MyCurrentBorrowActivity.this,list,R.drawable.borrowbook,new String[]{"title","borrowDate","returnDate",""},new int[]{R.id.borrowBookTitle,R.id.bookBorrowDate,R.id.borrowReturnDate});
					setListAdapter(adapter);
					if(adapter.getCount()!=0)
						myCurrentBorrowLayout.setVisibility(View.VISIBLE);
					else
						myCurrentBorrowNoneLinearLayout.setVisibility(View.VISIBLE);
				}
				else{
					myCurrentBorrowNoneLinearLayout.setVisibility(View.VISIBLE);
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
		list=UserInfoPullService.getBorrowBookDetail(GetDataFromWeb.getInputStream("http://elib.njutcm.edu.cn:8080/mainService.asmx/getCurrentBorrow?password="+MenuActivity.passWord+"&userCode="+MenuActivity.userCode));
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
		intent.putExtra("flag", "MyCurrentBorrowActivity");
		intent.setClass(MyCurrentBorrowActivity.this, NewBookDetailActivity.class);
		MyCurrentBorrowActivity.this.startActivity(intent);
	}
}

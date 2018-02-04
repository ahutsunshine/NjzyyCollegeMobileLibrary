package cn.iMobileLib.NjutcmMobileLibrary;

import mars.SqliteHelp.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyLibraryActivity extends Activity{
	private Button myLibraryBackButton=null;
	private Button myLibraryCancelButton=null;
	private TextView myLibraryTextview=null;
	private ImageButton certificateinformationImageButton=null;
	private ImageButton currentBorrowImageButton=null;
	private ImageButton borrowHistoryImageButton=null;
	private ImageButton breakRulesInfomation=null;
	private ImageButton readerSuggestion=null;
	private ImageButton ReservationInfomation=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylibrary);
		myLibraryBackButton=(Button)findViewById(R.id.myLibraryBackButton);
		myLibraryCancelButton=(Button)findViewById(R.id.myLibraryCancelButton);
		myLibraryTextview=(TextView)findViewById(R.id.myLibraryTextview);
		certificateinformationImageButton=(ImageButton)findViewById(R.id.certificateinformation);
		currentBorrowImageButton=(ImageButton)findViewById(R.id.currentBorrow);
		borrowHistoryImageButton=(ImageButton)findViewById(R.id.borrowHistory);
		certificateinformationImageButton.setOnClickListener(new ImageButtonListener());
		breakRulesInfomation=(ImageButton)findViewById(R.id.breakRulesInfomation);
		readerSuggestion=(ImageButton)findViewById(R.id.readerSuggestion);
		ReservationInfomation=(ImageButton)findViewById(R.id.reservationInfomation);
		borrowHistoryImageButton.setOnClickListener(new ImageButtonListener());
		currentBorrowImageButton.setOnClickListener(new ImageButtonListener());
		breakRulesInfomation.setOnClickListener(new ImageButtonListener());
		readerSuggestion.setOnClickListener(new ImageButtonListener());
		myLibraryBackButton.setOnClickListener(new ButtonListener());
		myLibraryCancelButton.setOnClickListener(new ButtonListener());
		ReservationInfomation.setOnClickListener(new ImageButtonListener());
		myLibraryTextview.setText(MenuActivity.userName+"的图书馆");
	}
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()){
			case R.id.myLibraryBackButton:finish();break;
			case R.id.myLibraryCancelButton:
				DatabaseHelper dbHelper=new DatabaseHelper(MyLibraryActivity.this,"user_password_table");
    			SQLiteDatabase db=dbHelper.getWritableDatabase();
    			db.delete("login", null, null);
    			db.close();
    			MenuActivity.isLoad=false;
    			finish();
				break;
			default:break;
			}
		}
	}
	
	class ImageButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("ccc");
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.certificateinformation:// 证件信息
				intent.setClass(MyLibraryActivity.this, UserInfoActivity.class);
				break;
			case R.id.currentBorrow:// 当前借阅
				intent.setClass(MyLibraryActivity.this, MyCurrentBorrowActivity.class);
				break;
			case R.id.borrowHistory:// 借阅历史
				intent.setClass(MyLibraryActivity.this, MyBorrowHistoryActivity.class);
				break;
			case R.id.breakRulesInfomation: //违章信息
				intent.setClass(MyLibraryActivity.this,BreakRulesInfoActivity.class);
				break;
			case R.id.readerSuggestion:	//读者荐购
				intent.setClass(MyLibraryActivity.this,ReaderSuggestionActivity.class);
				break;
			case R.id.reservationInfomation://预约信息
				intent.setClass(MyLibraryActivity.this,ReservationHistoryActivity.class);
				break;
			default:
				break;
			}
			MyLibraryActivity.this.startActivity(intent);
		}
	}
}
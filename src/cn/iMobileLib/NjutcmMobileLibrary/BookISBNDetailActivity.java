package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import example.mobilelibrary.entity.BookDetail;
import example.mobilelibrary.entity.BookInformation;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.http.XmlWebService;

public class BookISBNDetailActivity extends Activity {
	private TextView booktitleText = null;
	private TextView bookautherText = null;
	private TextView bookpubItemText = null;
	private TextView bookisbnPriceText = null;
	private TextView bookpageSizeText = null;
	private TextView bookclcText = null;
	private TextView booksubjectText = null;
	private TextView bookdetailinfor = null;
	private TextView bookinfor = null;
	private ImageView imageview = null;
	private Button returnButton;
	private LinearLayout layoutLoading;
	private LinearLayout layoutLoaded;
	private LinearLayout bookDetailNoneLinearLayout;
	private Bitmap bitmap;
	private String ISBN = null;
	private String storeBook = "";
	private BookDetail bookdetail;
	private List<BookInformation> bookinfors;
	private LinearLayout pLayout = null;
	private int flag = 0;
	private boolean service=true;
	private Button reservationButton=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail);
		reservationButton=(Button)findViewById(R.id.right_btn);
		booktitleText = (TextView) findViewById(R.id.book_detail_title1);
		bookautherText = (TextView) findViewById(R.id.book_detail_author1);
		bookpubItemText = (TextView) findViewById(R.id.book_detail_pubItem1);
		bookisbnPriceText = (TextView) findViewById(R.id.book_detail_isbnPrice1);
		bookpageSizeText = (TextView) findViewById(R.id.book_detail_pageSize1);
		bookclcText = (TextView) findViewById(R.id.book_detail_clc1);
		booksubjectText = (TextView) findViewById(R.id.book_detail_subject1);
		bookdetailinfor = (TextView) findViewById(R.id.book_detail_infor);
		bookinfor = (TextView) findViewById(R.id.book_local_infor1);
		imageview = (ImageView) findViewById(R.id.book_picture);
		returnButton = (Button) findViewById(R.id.bdBackButton);
		returnButton.setOnClickListener(new ReturnButtonListener());
		layoutLoading = (LinearLayout) findViewById(R.id.tbar_bookdetail);
		layoutLoaded = (LinearLayout) findViewById(R.id.newsdetail_tbar);
		bookDetailNoneLinearLayout = (LinearLayout) findViewById(R.id.bookDetailNoneLinearLayout);
		pLayout = (LinearLayout) findViewById(R.id.picture);
		layoutLoaded.setVisibility(View.GONE);
		layoutLoading.setVisibility(View.VISIBLE);
		reservationButton.setVisibility(View.GONE);
		new Thread(new Runnable() {
			public void run() {

				try {
					Intent intent = getIntent();
					ISBN = intent.getStringExtra("ISBN");
					bookdetail = XmlWebService.getdetailByISBN(ISBN,getApplicationContext());
					System.out.println("bookdetailInfo title="+ bookdetail.titleAuth);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					if (e.getMessage() != null)
						flag = 1;
					else
						flag = 0;
					service=false;
				}
				try {
					bookinfors = XmlWebService.getbookinforByISBN(ISBN,getApplicationContext());
					System.out.println("bookImg address="+bookdetail.bookImg);
					bitmap = getHttpBitmap(bookdetail.bookImg);
				} catch (Exception e) {
					flag = 3;
					e.printStackTrace();
				}
				if(!service) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AlertDialog.Builder builder = new Builder(BookISBNDetailActivity.this);
							builder.setMessage("咦，您的网络出问题啦，请检查网络是否连接");
							builder.setTitle("提示");
							builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {dialog.dismiss();BookISBNDetailActivity.this.finish();}
									});
							if (!isFinishing()) {try {builder.create().show();} catch (Exception ex) {ex.printStackTrace();}}
							return;
						}
					});
				}else {
					mHandler.post(runnableUi);
				}

			}
		}).start();
	}
	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isConnected();
	}
	class ReturnButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			finish();
		}
	}

	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap resize = null;
		try {
			myFileURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);

			InputStream is = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			// resize=Bitmap.createScaledBitmap(bitmap, 300, 200, true);
			resize = bitmap;
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resize;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				layoutLoading.setVisibility(View.GONE);
				if (msg.arg1 == 0) {
					layoutLoaded.setVisibility(View.GONE);
					pLayout.setVisibility(View.GONE);
					AlertDialog.Builder builder = new Builder(BookISBNDetailActivity.this);
					builder.setMessage("很遗憾，没有此馆藏...");
					builder.setTitle("提示");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									BookISBNDetailActivity.this.finish();
								}
							});
					if (!isFinishing()) {
						try {
							builder.create().show();
						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}

				} else if (msg.arg1 == 1)
					layoutLoaded.setVisibility(View.VISIBLE);
				else {
					bookDetailNoneLinearLayout.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	Runnable runnableUi = new Runnable() {

		@Override
		public void run() {

			imageview.setImageBitmap(bitmap);
			Message msg = mHandler.obtainMessage(1);
			if (bookdetail.titleAuth == "") {
				msg.arg1 = 0;
			} else if (bookdetail != null) {
				Log.d("BookIBSNActivty ", "bookdetail not null");
				booktitleText.setText(bookdetail.titleAuth);
				bookautherText.setText(bookdetail.author);
				bookpubItemText.setText(bookdetail.pubItem);
				bookisbnPriceText.setText(bookdetail.isbnPrice);
				bookpageSizeText.setText(bookdetail.pageSize);
				bookclcText.setText(bookdetail.clc);
				booksubjectText.setText(bookdetail.subject);
				bookdetailinfor.setText(bookdetail.bookAbstract);

				for (int i = 0; i < bookinfors.size(); i++) {
					BookInformation bookinfor = bookinfors.get(i);
					storeBook += "索书号：" + bookinfor.indexNo + "\n" + "条码号："
							+ bookinfor.barCode + "\n" + "年卷期：" + bookinfor.yvi
							+ "\n" + "校区-藏馆地：" + bookinfor.storePlace + "\n"
							+ "书刊状态：" + bookinfor.boolState + "\n" + "\n";
				}
				System.out.println("storeBook="+storeBook);
				msg.arg1 = 1;
				bookinfor.setText(storeBook);
			} else if (G.Network) {
				msg.arg1 = 2;
				G.abnormal(flag, getApplicationContext());
				layoutLoading.setVisibility(View.GONE);
			} else {
				Toast.makeText(getApplicationContext(), "2G/3G网络已关闭",
						Toast.LENGTH_SHORT).show();
				msg.arg1 = 2;
			}
			msg.sendToTarget();
		}
	};
}

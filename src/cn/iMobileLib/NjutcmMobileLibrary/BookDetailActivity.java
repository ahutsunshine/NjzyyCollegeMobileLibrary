package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import example.mobilelibrary.entity.BookDetail;
import example.mobilelibrary.entity.BookInformation;
import example.mobilelibrary.entity.BookReservationApplication;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.http.XmlWebService;

public class BookDetailActivity extends Activity implements OnTouchListener,
		OnGestureListener {
	private TextView booktitleText = null;
	private TextView bookautherText = null;
	private TextView bookpubItemText = null;
	private TextView bookisbnPriceText = null;
	private TextView bookpageSizeText = null;
	private TextView bookclcText = null;
	private TextView booksubjectText = null;
	private TextView bookdetailinfor = null;
	private TextView Bookinfor = null;
	private ImageView imageview = null;
	private Button returnButton;
	private LinearLayout layoutLoading;
	private LinearLayout layoutLoaded;
	private Bitmap bitmap;
	private String link = null;
	private String storeBook = "";
	private BookDetail bookdetail;
	private BookReservationApplication book = null;
	private List<BookInformation> bookinfors;

	// sun
	private int position;
	private ArrayList<String> linkList = null;
	private int page;
	private GestureDetector mGestureDetector = null;
	private String keyWord;
	private boolean isReady = false;
	private boolean isLoadLink = false;
	private LinearLayout bookDetailNoneLinearLayout = null;
	private TextView reservationTextView = null;
	private String marc_no = null;
	private String indexNo = null;
	public static int count = 1;
	public static String result = null;
	ProgressDialog pDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail);
		reservationTextView = (TextView) findViewById(R.id.right_btn);
		booktitleText = (TextView) findViewById(R.id.book_detail_title1);
		bookautherText = (TextView) findViewById(R.id.book_detail_author1);
		bookpubItemText = (TextView) findViewById(R.id.book_detail_pubItem1);
		bookisbnPriceText = (TextView) findViewById(R.id.book_detail_isbnPrice1);
		bookpageSizeText = (TextView) findViewById(R.id.book_detail_pageSize1);
		bookclcText = (TextView) findViewById(R.id.book_detail_clc1);
		booksubjectText = (TextView) findViewById(R.id.book_detail_subject1);
		bookdetailinfor = (TextView) findViewById(R.id.book_detail_infor);
		Bookinfor = (TextView) findViewById(R.id.book_local_infor1);
		imageview = (ImageView) findViewById(R.id.book_picture);
		imageview.setOnClickListener(new ImageviewListener());
		returnButton = (Button) findViewById(R.id.bdBackButton);
		returnButton.setOnClickListener(new ReturnButtonListener());
		layoutLoading = (LinearLayout) findViewById(R.id.tbar_bookdetail);
		layoutLoaded = (LinearLayout) findViewById(R.id.newsdetail_tbar);
		layoutLoaded.setVisibility(View.GONE);
		layoutLoading.setVisibility(View.VISIBLE);
		reservationTextView.setOnClickListener(new ReservationListener());
		bookDetailNoneLinearLayout = (LinearLayout) findViewById(R.id.bookDetailNoneLinearLayout);
		mGestureDetector = new GestureDetector((OnGestureListener) this);
		layoutLoaded.setOnTouchListener(this);
		layoutLoaded.setLongClickable(true);
		Intent intent = getIntent();
		link = intent.getStringExtra("link");
		position = intent.getIntExtra("position", 0);
		page = intent.getIntExtra("page", 1);
		linkList = intent.getStringArrayListExtra("linkList");
		keyWord = intent.getStringExtra("keyWord");

		new Thread(new Runnable() {
			public void run() {
				try {

					bookdetail = XmlWebService.getbookdetail(link,BookDetailActivity.this);
					bookinfors = XmlWebService.getbookinfor(link,getApplicationContext());
					bitmap = getHttpBitmap(bookdetail.bookImg);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
				}
				mHandler.post(runnableUi);
				mHandler.sendEmptyMessage(1);
			}
		}).start();

		loadData();
	}

	/*
	 * public void onClick(View v) {
	 * 
	 * }
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	};

	class ReservationListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			System.out.println("------->>BookDetailActivity start!");
			if (marc_no != null) {
				pDialog = new ProgressDialog(BookDetailActivity.this);
				pDialog.setMessage("请求预约中...");
				pDialog.show();
				new Thread() {
					public void run() {
						try {
							book = new BookReservationApplication();
							book = WebServiceHelper.getReservationApplicationInfo(marc_no,MenuActivity.passWord,MenuActivity.userCode);
							if (book != null&& Integer.parseInt(book.canBorrowNum) > 0&& Integer.parseInt(book.stayLibrary) == 0&& Integer.parseInt(book.waitNum) == 0) {
								result = WebServiceHelper.getReservationResult(marc_no, indexNo,MenuActivity.passWord,MenuActivity.userCode);
								if (result != null) {
									runOnUiThread(new Runnable() {
										public void run() {
											handler.sendEmptyMessage(0);
											AlertDialog.Builder builder = new Builder(BookDetailActivity.this);
											builder.setMessage(result);
											builder.setTitle("提示");
											builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog,int which) {
															dialog.dismiss();
														}
													});
											builder.create().show();
										}
									});
								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											pDialog.dismiss();
											AlertDialog.Builder builder = new Builder(BookDetailActivity.this);
											builder.setMessage("预约失败了,请检查网络后重试...");
											builder.setTitle("提示");
											builder.setPositiveButton(
													"确认",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface dialog,int which) {
															dialog.dismiss();
														}
													});
											builder.create().show();
										}
									});
								}
							} else {
								runOnUiThread(new Runnable() {
									public void run() {
										pDialog.dismiss();
										AlertDialog.Builder builder = new Builder(BookDetailActivity.this);
										if (book != null) {
											builder.setMessage(book.isReservation);
										} else {
											builder.setMessage("预约失败了,请检查网络后重试...");
										}
										builder.setTitle("提示");
										builder.setPositiveButton(
												"确认",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(DialogInterface dialog,int which) {
														dialog.dismiss();
													}
												});
										builder.create().show();

									}
								});
							}
						} catch (Exception e) {
							e.printStackTrace();
							runOnUiThread(new Runnable() {
								public void run() {
									pDialog.dismiss();
									Toast.makeText(BookDetailActivity.this,"很抱歉,预约失败了...", Toast.LENGTH_SHORT).show();
								}
							});
						}
					};
				}.start();
			}

		}

	}

	class ImageviewListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			Intent intent = new Intent();
			intent.putExtra("link", bookdetail.bookImg);
			intent.setClass(getApplicationContext(), ImageShower.class);
			BookDetailActivity.this.startActivity(intent);
		}

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
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);

			InputStream is = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			// resize=Bitmap.createScaledBitmap(bitmap, 200, 200, true);
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
			}
		}
	};
	Runnable runnableUi = new Runnable() {

		@Override
		public void run() {

			imageview.setImageBitmap(bitmap);
			if (bookdetail == null) {
				reservationTextView.setVisibility(View.GONE);
			} else if (bookdetail != null) {
				marc_no = bookdetail.marc_no;
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
					indexNo = bookinfor.indexNo;
					storeBook += "索书号：" + bookinfor.indexNo + "\n" + "条码号："
							+ bookinfor.barCode + "\n" + "年卷期：" + bookinfor.yvi
							+ "\n" + "校区-藏馆地：" + bookinfor.storePlace + "\n"
							+ "书刊状态：" + bookinfor.boolState + "\n" + "\n";
					// if(i%2!=0){
					// Bookinfor.setBackgroundColor(R.color.editfocuse);
					// }
				}

				Bookinfor.setText(storeBook);
				layoutLoaded.setVisibility(View.VISIBLE);
			} else if (G.Network && bookdetail == null) {
				bookDetailNoneLinearLayout.setVisibility(View.VISIBLE);
			} else
				Toast.makeText(getApplicationContext(), "2G/3G网络已关闭",
						Toast.LENGTH_SHORT).show();
		}
	};

	// sun

	private void loadData() {
		if (position == linkList.size() - 1) {
			page++;
			loadLink();
		} else
			isLoadLink = true;
	}

	private void loadLink() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					linkList.addAll(XmlWebService.getLink(keyWord, page));
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isLoadLink = true;
			}
		}).start();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
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
		if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 0
				&& Math.abs(e1.getY() - e2.getY()) < 50) {
			if (position <= linkList.size() - 2 && isLoadLink) {
				position++;
				link = linkList.get(position);
				isReady = true;
			}
		} else if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 0
				&& Math.abs(e1.getY() - e2.getY()) < 50) {
			if (position > 0 && isLoadLink) {
				position--;
				link = linkList.get(position);
				isReady = true;
			}
		}
		if (isReady)
			skipToOtherActivity();
		return false;
	}

	private void skipToOtherActivity() {
		Intent intent = new Intent();
		intent.putExtra("link", link);
		intent.putExtra("linkList", linkList);
		intent.putExtra("position", position);
		intent.putExtra("page", page);
		intent.putExtra("keyWord", keyWord);
		intent.setClass(this, BookDetailCopyActivity.class);
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
	public boolean onTouch(View arg0, MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}

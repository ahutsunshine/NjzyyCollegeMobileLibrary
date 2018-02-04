package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	// 定义固定延迟时间，final相当于C++的const
	private final int DATE_TIME = 2000;
	private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_image/";
	private LinearLayout layout = null;
	public static String filePath = null;
	Handler handler = null;

	private static Bitmap bitmap = null;
	public static String filename = null;
	public String imageInfo = null;
	final String INITIALIZED = "initialized";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.welcome);
		final SharedPreferences myPrefs = getPreferences(MODE_PRIVATE);
		final boolean hasPreferences = myPrefs.getBoolean(INITIALIZED, false);
		final SharedPreferences mPerferences = PreferenceManager.getDefaultSharedPreferences(this);
		String path = mPerferences.getString("path", null);
		System.out.println("image_path="+path);
		if (path != null) {
			File file = new File(path);
			if (file.exists()) { // 如果存在这张图片，则设置这张图片为启动图片
				System.out.println("start image is exist");
				layout = (LinearLayout) findViewById(R.id.imageLayout);
				layout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeFile(path)));
			} 
		}
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
						System.out.println("检测图片中...");
						imageInfo = WebServiceHelper.searchImageInfo();
						filename = imageInfo.split("/")[1];
						System.out.println("---------->>imageInfo="+ imageInfo);
						if (imageInfo != null) {
							SharedPreferences.Editor mEditor = mPerferences.edit();
							mEditor.putString("path", ALBUM_PATH+ imageInfo.split("/")[1]);
							mEditor.commit();
							filePath = ALBUM_PATH+ imageInfo.split("/")[1];
							File file = new File(filePath);
							if (imageInfo.split("/")[0] != "1") {
								if (!file.exists()) { // 判断当前路径下是否存在更新的图片，不存在则下载
									System.out.println(imageInfo.split("/")[1]+"正在下载");
									String url = "http://elib.njutcm.edu.cn:8081/data/"+ imageInfo.split("/")[1];
									try {
										bitmap = WebServiceHelper.getImagePicture(url);
									} catch (IOException e) {
										e.printStackTrace();
									}
									saveFile(bitmap, filename);
								}
							}
						}
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainActivity.this,"网络请求出错，请重试！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}.start();
		

		handler = new Handler();
		// 设置初始界面的图片背景
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(hasPreferences) {
					Intent intent=new Intent();
					intent.setClass(MainActivity.this, MenuActivity.class);
					MainActivity.this.startActivity(intent);
					finish();
					}
					else{
						Intent intent=new Intent();
						intent.putExtra("flag", "0");
						intent.setClass(MainActivity.this, HelpActivity.class);
						MainActivity.this.startActivity(intent);
						finish();
					}
					 Editor editor = myPrefs.edit();
				     editor.putBoolean(INITIALIZED, true);
				     editor.commit();
				
			}
		}, DATE_TIME);
	}

	public boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isConnected();
	}

	public void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

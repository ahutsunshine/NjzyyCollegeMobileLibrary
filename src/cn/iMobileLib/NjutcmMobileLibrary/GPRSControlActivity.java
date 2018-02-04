package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CacheManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import example.mobilelibrary.entity.G;

public class GPRSControlActivity extends Activity{
	 SharedPreferences sharePre;
	 private final String FILE_NAME="example.mobilelibrary";
	 private ToggleButton gprsButton;
	 ProgressDialog pDialog=null;
	 private Button backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gprs_setting);
		sharePre=getSharedPreferences(FILE_NAME, 0);
		gprsButton=(ToggleButton)findViewById(R.id.gprscontrol);
		backButton=(Button)findViewById(R.id.back);
		backButton.setOnClickListener(new BackButtonListener());
		gprsButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 自动生成的方法存根
				sharePre.edit().putBoolean(R.id.gprscontrol+"", isChecked).commit();
				if(!isChecked&&G.getNetworkType(GPRSControlActivity.this).equalsIgnoreCase("2G/3G"))
					G.Network=false;
				else
					G.Network=true;
			}
		});
		gprsButton.setChecked(sharePre.getBoolean(R.id.gprscontrol+"",true));
	}
	class BackButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	public void relClick(View v){
		int tag = Integer.parseInt(v.getTag().toString());
		RelativeLayout layout = (RelativeLayout) v;
		switch (tag) {
		case 1:// 2G/3G网络
			ToggleButton tb1 = (ToggleButton) layout.getChildAt(1);
			tb1.setChecked(!tb1.isChecked());
			break;
		case 2:// 清除缓存
			clearCache();
			break;
		default:
			break;
		}
	}	
	// 遍历清理文件
		private void clearFile(File f) {
			File file = f;
			if (file != null && file.exists() && file.isDirectory()) {
				for (File item : file.listFiles()) {
					if (item.isDirectory()) {
						clearFile(item);
					} else {
						item.delete();
					}
				}
				// file.delete();
			}
			//
			this.deleteDatabase("webview.db");
			this.deleteDatabase("webviewCache.db");
			// 以下不知是否可以清理
			this.deleteDatabase("webview.db-shm");
			this.deleteDatabase("webview.db-wal");
			this.deleteDatabase("webviewCache.db-shm");
			this.deleteDatabase("webviewCache.db-wal");
		}
		
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.dismiss();
					
					Toast.makeText(GPRSControlActivity.this, "清理成功",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		// 清除缓存
			private void clearCache() {
				pDialog=new ProgressDialog(this);
				pDialog.setMessage("清理缓存中...");
				pDialog.show();
				Thread thread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						clearFile(CacheManager.getCacheFileBaseDir());
						SQLiteDatabase sqLiteDatabase=openOrCreateDatabase("libaryNews.db", Context.MODE_PRIVATE,null);
						sqLiteDatabase.execSQL("DROP TABLE IF EXISTS libNews");
						sqLiteDatabase.close();
						SQLiteDatabase liteDatabase=openOrCreateDatabase("libraryNewsContent",Context.MODE_PRIVATE, null);
						liteDatabase.execSQL("DROP TABLE IF EXISTS libNewsContent");
						liteDatabase.close();
						SQLiteDatabase database=openOrCreateDatabase("schoolNews.db",Context.MODE_PRIVATE, null);
						database.execSQL("DROP TABLE IF EXISTS schNews");
						database.close();
						SQLiteDatabase schDatabase=openOrCreateDatabase("schoolNewsContent.db",Context.MODE_PRIVATE, null);
						schDatabase.execSQL("DROP TABLE IF EXISTS schNewsContent");
						schDatabase.close();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendEmptyMessage(0);
					}
				});
				thread.start();
				thread = null;
			}

}

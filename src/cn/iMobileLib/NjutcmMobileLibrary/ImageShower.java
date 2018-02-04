package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;


public class ImageShower extends Activity {

	private ImageView bigpictureview;
	private String link;
	private Bitmap bitmap;
	private ImageLoadingDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshower);
		bigpictureview=(ImageView)findViewById(R.id.bigpicture);
		  dialog = new ImageLoadingDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Intent intent=getIntent();
		link=intent.getStringExtra("link");
		final Handler mHandler=new Handler();
		// 两秒后关闭后dialog
		new Thread(new Runnable() {
			 public void run() {
				 bitmap=getHttpBitmap(link);
				 System.out.println(link);
				 mHandler.post(runnableUi);
			 }
		}).start();
//		new Handler().post(new Runnable() {
//			@Override
//			public void run() {
//				dialog.dismiss();
//			}
//		});
	}

	public static Bitmap getHttpBitmap(String url){
		URL myFileURL;
		Bitmap resize=null;
		try{
			myFileURL=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);
			 
			InputStream is=conn.getInputStream();
			Bitmap bitmap=BitmapFactory.decodeStream(is);
			resize=Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*2, bitmap.getHeight()*2, true);
	//		resize=bitmap;
			is.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return resize;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}
	Runnable runnableUi=new Runnable(){

		@Override
		public void run() {
			// TODO 自动生成的方法存根
			bigpictureview.setImageBitmap(bitmap);
			dialog.dismiss();
		}
		
	};
}

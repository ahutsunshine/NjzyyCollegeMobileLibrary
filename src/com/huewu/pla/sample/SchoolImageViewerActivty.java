package com.huewu.pla.sample;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import cn.iMobileLib.NjutcmMobileLibrary.R;

public class SchoolImageViewerActivty extends Activity {
	
	private String fileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fileName = getIntent().getStringExtra("fileName");
		if (fileName == null) {
			this.finish();
			return;
		}
		setContentView(R.layout.school_image_viewer_activity);
		ZoomImageView imageView = (ZoomImageView) findViewById(R.id.image);
        InputStream path;
		try {
			path = getAssets().open(SchoolImageActivity.image_path + "/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			this.finish();
			return;
		}
        Bitmap bm = BitmapFactory.decodeStream(path);
		imageView.setImageBitmap(bm);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}

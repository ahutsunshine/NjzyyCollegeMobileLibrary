package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DepartmentList extends Activity{
	
	private Button backButton;
	private int[] layouts ={R.layout.department,R.layout.depart_tsgbgs
			,R.layout.depart_tscaijib,R.layout.office,
			R.layout.depart_qkyls,R.layout.depart_xxjszx,
			R.layout.depart_xxjsb,R.layout.depart_wxjs,
			R.layout.depart_gjb};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		int n = getIntent().getExtras().getInt("number");
		setContentView(layouts[n - 1]);
		backButton=(Button)findViewById(R.id.back);
		backButton.setOnClickListener(new BackButtonListener());
	}
	class BackButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	public void linClick(View v) {
		String tag=v.getTag().toString().replace("-","");
		Intent i=new Intent("android.intent.action.CALL",Uri.parse("tel:"+tag));
		startActivity(i);
	}
	public void onClick(View v) {
		String tag=v.getTag().toString();
		Intent intent=new Intent();
		intent.putExtra("email", tag);
		intent.setClass(DepartmentList.this, EmailActivity.class);
		startActivity(intent);
	}
	
	
}

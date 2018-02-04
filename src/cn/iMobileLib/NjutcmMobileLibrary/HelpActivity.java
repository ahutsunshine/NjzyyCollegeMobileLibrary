package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import example.defineView.CustomViewPager;
import example.mobilelibrary.Adapter.HelpAdapter;



public class HelpActivity extends Activity{

	private List<View> lstDots = new ArrayList<View>();
	LocalActivityManager manager = null;
	@SuppressWarnings("unused")
	private LinearLayout linearlayout;	
	private int oldPosition = 0;
	private String flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		List<View> lst = new ArrayList<View>();
		 manager = new LocalActivityManager(this , true);
	      manager.dispatchCreate(savedInstanceState);
	      linearlayout=(LinearLayout)findViewById(R.id.losedot);
	      Intent inten=getIntent();
	      flag=inten.getStringExtra("flag");
		final LayoutInflater inflater = LayoutInflater.from(this);
		lst.add(inflater.inflate(R.layout.help1, null));
		lst.add(inflater.inflate(R.layout.help2, null));
		lst.add(inflater.inflate(R.layout.help3, null));
		lst.add(inflater.inflate(R.layout.help4, null));
//		lst.get(3).setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO 自动生成的方法存根
//				Intent intent=new Intent();
//				intent.setClass(HelpActivity.this,MenuActivity.class);
//				HelpActivity.this.startActivity(intent);
//				finish();
//			}
//			
//		});
		Intent intent = new Intent(HelpActivity.this, MenuActivity.class);
		lst.add(manager.startActivity("1", intent).getDecorView());	
		lstDots.add(findViewById(R.id.dot01));
		lstDots.add(findViewById(R.id.dot02));
		lstDots.add(findViewById(R.id.dot03));
		lstDots.add(findViewById(R.id.dot04));
		final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new HelpAdapter(lst));
	
		viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO 自动生成的方法存根		
				if(arg0<4){
					lstDots.get(arg0).setBackgroundResource(R.drawable.help_dot_focused);
					lstDots.get(oldPosition).setBackgroundResource(R.drawable.help_dot_normal);	
					
					oldPosition = arg0;	}
	/*				if(arg0==3){
						Intent intent=new Intent();
						intent.setClass(HelpActivity.this,MenuActivity.class);
						HelpActivity.this.startActivity(intent);
					}*/
				else{
					if(flag.equalsIgnoreCase("0")){
					Intent intent=new Intent();
					intent.setClass(HelpActivity.this,MenuActivity.class);
					HelpActivity.this.startActivity(intent);
					HelpActivity.this.finish();
					System.out.println("flag-->0");
					}
					else {
						HelpActivity.this.finish();	
						System.out.println("flag-->1");
					}
				}		
					
			}
			
		});
	}

}

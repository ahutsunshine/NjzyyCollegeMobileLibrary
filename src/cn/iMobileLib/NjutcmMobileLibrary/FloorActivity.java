package cn.iMobileLib.NjutcmMobileLibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobilelibrary.widget.HeaderWidget;

public class FloorActivity extends Activity {

	private ListView listView=null;
	private HeaderWidget headerWidget=null;
	int array;

	public int Backcount = 0;
	public int fullScreenCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.new_floor);
		listView = (ListView) findViewById(R.id.listview);
		listView.setFadingEdgeLength(0);
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		array = R.array.floor_info_array;
		listView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.new_list_item, getResources().getStringArray(array)));
		headerWidget.hiddenRightBtn(false);
		headerWidget.setRightText("全屏");
		headerWidget.setLeftText("首页");
		headerWidget.bindCickToRightBtn(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				fullScreenCount++;
				FloorActivity.this.getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				//hideLayout.setVisibility(View.GONE);
			}
		});
		headerWidget.setMiddleText("楼层介绍");
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent=new Intent();
				if(position==0)
				{
					//Toast.makeText(FloorActivity.this, "你选中了"+Integer.toString(position+1)+"项",Toast.LENGTH_SHORT).show();
					intent.setClass(FloorActivity.this, FloorDetailActivity.class);
				}
				else if(position==1){
					//Toast.makeText(FloorActivity.this, "你选中了"+Integer.toString(position+1)+"项",Toast.LENGTH_SHORT).show();
					intent.setClass(FloorActivity.this, FloorDetailActivity.class);
				}
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Backcount++;
			if (fullScreenCount == Backcount) // 判断返回条件
			{
				//hideLayout.setVisibility(View.VISIBLE);
				FloorActivity.this.getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

package cn.iMobileLib.NjutcmMobileLibrary;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
public class DepartmentInforActivity extends Activity{
	private ListView listview;
	private Button backButton;
	@SuppressWarnings("rawtypes")
	private Class class1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.department_list);
		listview=(ListView)findViewById(R.id.listview);
		backButton=(Button)findViewById(R.id.back);
		listview.setAdapter(new ArrayAdapter<String>(this,
				R.layout.department_list_item, getResources().getStringArray(R.array.depart_info_array)));
		backButton.setOnClickListener(new BackButtonListener());
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				if(arg2==0)
					class1=HeadTalkActivity.class;
				else class1=DepartmentList.class;
				Intent intent=new Intent();
				String text=listview.getAdapter().getItem(arg2).toString();
				intent.putExtra("title", text);
				intent.putExtra("number", arg2);
				intent.setClass(DepartmentInforActivity.this, class1);
				startActivity(intent);
			}
			
		});
	}
	class BackButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}

}

package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import example.defineView.DownListDialog;
import example.mobilelibrary.Adapter.DownListAdapter;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.HostType;

public class KSearchActivity extends BaseSearchActivity {
	private DownListDialog downListDialogCH = null;
	private DownListDialog downListDialogEN = null;

	private Spinner spLanguage = null;
	private ImageView imageView = null;
	// 搜索参数
	private EditText etParam = null;
	// 服务器类型
	private G.HostType hostType = G.HostType.CNKI;
	private G.LanguageType languageType = G.LanguageType.CH;
	private int count = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.search);
		etParam = (EditText) findViewById(R.id.etParam);
		imageView = (ImageView) findViewById(R.id.ivDownList);
		spLanguage = (Spinner) findViewById(R.id.spLanguage);
		// bind the sound function--语音功能
		bindSoundView(etParam);
		downListDialogCH = DownListDialog.create(this);
		downListDialogEN = DownListDialog.create(this);
		initDownListCh();
		initDialogEN();
		spLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				languageType = G.LanguageType.values()[arg2];
				if (arg2 == 1) {
					imageView.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.sci));
				} else {
					imageView.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.cnki));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO 自动生成的方法存根

			}
		});
	}

	// 初始化搜索类型下拉菜单
	public void initDownListCh() {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = null;

		// 内容1
		item = new HashMap<String, String>();
		item.put("img", R.drawable.cnki + "");
		item.put("txt", "中国知网");
		item.put("value", G.HostType.CNKI.value() + "");
		data.add(item);

		// 内容2
		item = new HashMap<String, String>();
		item.put("img", R.drawable.wafang + "");
		item.put("txt", "万方");
		item.put("value", G.HostType.WANFANG.value() + "");
		data.add(item);

		// 内容 3
		item = new HashMap<String, String>();
		item.put("img", R.drawable.weipu + "");
		item.put("txt", "维普");
		item.put("value", G.HostType.WEIPU.value() + "");
		data.add(item);

		// 内容4
		item = new HashMap<String, String>();
		item.put("img", R.drawable.fangzheng + "");
		item.put("txt", "电子图书");
		item.put("value", G.HostType.FANGZHENG.value() + "");
		data.add(item);

		// 内容 5
		/*item = new HashMap<String, String>();
		item.put("img", R.drawable.chaoxing + "");
		item.put("txt", "超星");
		item.put("value", G.HostType.CHAOXING.value() + "");
		data.add(item);*/

		// 内容6
		/*item = new HashMap<String, String>();
		item.put("img", R.drawable.sinomed + "");
		item.put("txt", "中国生物医学");
		item.put("value", G.HostType.SINOMED.value() + "");
		data.add(item);*/

		// 传入两个参数
		// DownListAdapter和DownListDialog.OnDownItemClickListener，而onItemClick是DownListDialog.OnDownItemClickListener监听事件的内容
		downListDialogCH.setDownListAdaper(new DownListAdapter(this, data),
				new DownListDialog.OnDownItemClickListener() {
					@Override
					public void onItemClick(BaseAdapter adapter, int position) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> map = (HashMap<String, String>) adapter
								.getItem(position);
						if (G.HostType.valueOf(Integer.parseInt(map
								.get("value"))) == HostType.FANGZHENG) {
							try {
								//目的是检测是否安装了apabi reader,如果安装了就会则不会显示异常，否则显示异常，弹出下载窗口
								getPackageManager().getPackageInfo("com.founder.apabi.reader", 0);
							//openApp("com.founder.apabi.reader");
						} catch (Exception ex) {
					        needApabiReader();
						}
						imageView.setImageBitmap(BitmapFactory.decodeResource(
								getResources(),
								Integer.parseInt(map.get("img"))));
						hostType = G.HostType.valueOf(Integer.parseInt(map
								.get("value")));
						} else {
							imageView.setImageBitmap(BitmapFactory.decodeResource(
									getResources(),
									Integer.parseInt(map.get("img"))));
							hostType = G.HostType.valueOf(Integer.parseInt(map
									.get("value")));
						}
					}
				});
	}
	
	private void needApabiReader() {
		new AlertDialog.Builder(this).setTitle("提示").setMessage("需要安装方正阅读器(Apabi Reader),是否前往下载?").setPositiveButton("确定", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://apabi.cn/download/index.html");
                intent.setData(content_url);
                KSearchActivity.this.startActivity(intent); 
			}
		}).setNegativeButton("取消", null).show();
	}

	@SuppressWarnings("unused")
	private void openApp(String packageName0) throws NameNotFoundException {
		PackageInfo pi = getPackageManager().getPackageInfo(packageName0, 0);

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = getPackageManager().queryIntentActivities(resolveIntent, 0);

		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String packageName = ri.activityInfo.packageName;
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			startActivity(intent);
		}
	}

	// 初始化图片的下拉列表--英文
	private void initDialogEN() {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = null;
		// 19
		item = new HashMap<String, String>();
		item.put("img", R.drawable.sci + "");
		item.put("txt", "sci");
		item.put("value", G.HostType.SCI.value() + "");
		data.add(item);
		// 20
		item = new HashMap<String, String>();
		item.put("img", R.drawable.fmjs + "");
		item.put("txt", "西文生物医学期刊");
		item.put("value", G.HostType.FMJS.value() + "");
		data.add(item);
		// 21
		item = new HashMap<String, String>();
		item.put("img", R.drawable.thieme + "");
		item.put("txt", "Thieme英文期刊");
		item.put("value", G.HostType.THIEME.value() + "");
		data.add(item);
		// 22
		item = new HashMap<String, String>();
		item.put("img", R.drawable.cambridge + "");
		item.put("txt", "剑桥期刊");
		item.put("value", G.HostType.CAMBRIDGE.value() + "");
		data.add(item);
		// 23
		item = new HashMap<String, String>();
		item.put("img", R.drawable.bmj + "");
		item.put("txt", "BMJ医学全文数据");
		item.put("value", G.HostType.BMJ.value() + "");
		data.add(item);
		// 24
		item = new HashMap<String, String>();
		item.put("img", R.drawable.bmj_group + "");
		item.put("txt", "BMJ循证医学全文数据库");
		item.put("value", G.HostType.BMJGROUP.value() + "");
		data.add(item);
		// 25
		item = new HashMap<String, String>();
		item.put("img", R.drawable.springer + "");
		item.put("txt", "Springer SLCC期刊全文数据库");
		item.put("value", G.HostType.SPRINGER.value() + "");
		data.add(item);
		//
		downListDialogEN.setDownListAdaper(new DownListAdapter(this, data),
				new DownListDialog.OnDownItemClickListener() {
					@Override
					public void onItemClick(BaseAdapter adapter, int position) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> map = (HashMap<String, String>) adapter
								.getItem(position);
						imageView.setImageBitmap((BitmapFactory.decodeResource(
								KSearchActivity.this.getResources(),
								Integer.parseInt(map.get("img")))));

						hostType = G.HostType.valueOf(Integer.parseInt(map
								.get("value")));
					}
				});
	}

	// 点击图标换搜索类型的事件
	public void imgClick(View view) {
		if (G.LanguageType.CH == languageType)
			downListDialogCH.show();
		if (G.LanguageType.EN == languageType)
			downListDialogEN.show();

	}

	// 知识检索的检索按钮事件
	public void btnClick(View view) {
		Intent intent = new Intent();
		intent.setClass(KSearchActivity.this, KSearchListActivity.class);
		String param = etParam.getText().toString();
		if (param.equals("")) {
			Toast.makeText(KSearchActivity.this, "请输入关键字", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		intent.putExtra("keyword", param);
		// 使用ordinal()方法获取枚举变量的整数值会更好一些
		intent.putExtra("language", languageType.ordinal());
		intent.putExtra("host", hostType.value());
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		count = 0;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (count == 0) {
				Toast.makeText(getApplicationContext(), "再按一下返回键退出程序!",Toast.LENGTH_SHORT).show();
				count++;
			} else
				finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}
}

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
	// ��������
	private EditText etParam = null;
	// ����������
	private G.HostType hostType = G.HostType.CNKI;
	private G.LanguageType languageType = G.LanguageType.CH;
	private int count = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.search);
		etParam = (EditText) findViewById(R.id.etParam);
		imageView = (ImageView) findViewById(R.id.ivDownList);
		spLanguage = (Spinner) findViewById(R.id.spLanguage);
		// bind the sound function--��������
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
				// TODO �Զ����ɵķ������

			}
		});
	}

	// ��ʼ���������������˵�
	public void initDownListCh() {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = null;

		// ����1
		item = new HashMap<String, String>();
		item.put("img", R.drawable.cnki + "");
		item.put("txt", "�й�֪��");
		item.put("value", G.HostType.CNKI.value() + "");
		data.add(item);

		// ����2
		item = new HashMap<String, String>();
		item.put("img", R.drawable.wafang + "");
		item.put("txt", "��");
		item.put("value", G.HostType.WANFANG.value() + "");
		data.add(item);

		// ���� 3
		item = new HashMap<String, String>();
		item.put("img", R.drawable.weipu + "");
		item.put("txt", "ά��");
		item.put("value", G.HostType.WEIPU.value() + "");
		data.add(item);

		// ����4
		item = new HashMap<String, String>();
		item.put("img", R.drawable.fangzheng + "");
		item.put("txt", "����ͼ��");
		item.put("value", G.HostType.FANGZHENG.value() + "");
		data.add(item);

		// ���� 5
		/*item = new HashMap<String, String>();
		item.put("img", R.drawable.chaoxing + "");
		item.put("txt", "����");
		item.put("value", G.HostType.CHAOXING.value() + "");
		data.add(item);*/

		// ����6
		/*item = new HashMap<String, String>();
		item.put("img", R.drawable.sinomed + "");
		item.put("txt", "�й�����ҽѧ");
		item.put("value", G.HostType.SINOMED.value() + "");
		data.add(item);*/

		// ������������
		// DownListAdapter��DownListDialog.OnDownItemClickListener����onItemClick��DownListDialog.OnDownItemClickListener�����¼�������
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
								//Ŀ���Ǽ���Ƿ�װ��apabi reader,�����װ�˾ͻ��򲻻���ʾ�쳣��������ʾ�쳣���������ش���
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
		new AlertDialog.Builder(this).setTitle("��ʾ").setMessage("��Ҫ��װ�����Ķ���(Apabi Reader),�Ƿ�ǰ������?").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://apabi.cn/download/index.html");
                intent.setData(content_url);
                KSearchActivity.this.startActivity(intent); 
			}
		}).setNegativeButton("ȡ��", null).show();
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

	// ��ʼ��ͼƬ�������б�--Ӣ��
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
		item.put("txt", "��������ҽѧ�ڿ�");
		item.put("value", G.HostType.FMJS.value() + "");
		data.add(item);
		// 21
		item = new HashMap<String, String>();
		item.put("img", R.drawable.thieme + "");
		item.put("txt", "ThiemeӢ���ڿ�");
		item.put("value", G.HostType.THIEME.value() + "");
		data.add(item);
		// 22
		item = new HashMap<String, String>();
		item.put("img", R.drawable.cambridge + "");
		item.put("txt", "�����ڿ�");
		item.put("value", G.HostType.CAMBRIDGE.value() + "");
		data.add(item);
		// 23
		item = new HashMap<String, String>();
		item.put("img", R.drawable.bmj + "");
		item.put("txt", "BMJҽѧȫ������");
		item.put("value", G.HostType.BMJ.value() + "");
		data.add(item);
		// 24
		item = new HashMap<String, String>();
		item.put("img", R.drawable.bmj_group + "");
		item.put("txt", "BMJѭ֤ҽѧȫ�����ݿ�");
		item.put("value", G.HostType.BMJGROUP.value() + "");
		data.add(item);
		// 25
		item = new HashMap<String, String>();
		item.put("img", R.drawable.springer + "");
		item.put("txt", "Springer SLCC�ڿ�ȫ�����ݿ�");
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

	// ���ͼ�껻�������͵��¼�
	public void imgClick(View view) {
		if (G.LanguageType.CH == languageType)
			downListDialogCH.show();
		if (G.LanguageType.EN == languageType)
			downListDialogEN.show();

	}

	// ֪ʶ�����ļ�����ť�¼�
	public void btnClick(View view) {
		Intent intent = new Intent();
		intent.setClass(KSearchActivity.this, KSearchListActivity.class);
		String param = etParam.getText().toString();
		if (param.equals("")) {
			Toast.makeText(KSearchActivity.this, "������ؼ���", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		intent.putExtra("keyword", param);
		// ʹ��ordinal()������ȡö�ٱ���������ֵ�����һЩ
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
		// TODO �Զ����ɵķ������
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (count == 0) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ�·��ؼ��˳�����!",Toast.LENGTH_SHORT).show();
				count++;
			} else
				finish();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}
}

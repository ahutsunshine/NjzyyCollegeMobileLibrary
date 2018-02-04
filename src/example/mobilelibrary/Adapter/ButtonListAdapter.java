package example.mobilelibrary.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.iMobileLib.NjutcmMobileLibrary.R;

public class ButtonListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String,String>> data;
	public ButtonListAdapter (Context context,ArrayList<HashMap<String, String>> data){
		this.context=context;
		this.data=data;
	}
	@Override
	public int getCount() {
		// TODO �Զ����ɵķ������
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO �Զ����ɵķ������
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO �Զ����ɵķ������
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO �Զ����ɵķ������
		ItemHolder holder;
		if (view == null) {
			holder = new ItemHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view=inflater.inflate(R.layout.bsearch_result, null);
			view.setTag(holder);
		}else{
			holder = (ItemHolder) view.getTag();
		}
		holder.booktitle=(TextView)view.findViewById(R.id.book_title);
		holder.booklanguage=(TextView)view.findViewById(R.id.book_language);
		holder.bookstoreNum=(TextView)view.findViewById(R.id.book_storeNum);
		holder.bookoutNum=(TextView)view.findViewById(R.id.book_outNum);
		//holder.orderbook=(Button)view.findViewById(R.id.orderbook);
		HashMap<String, String> itemMap = data.get(arg0);
		holder.booktitle.setText(itemMap.get("listName"));
		holder.booklanguage.setText(itemMap.get("language"));
		holder.bookstoreNum.setText(itemMap.get("storeNum"));
		holder.bookoutNum.setText(itemMap.get("outNum"));
		/*holder.orderbook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				new AlertDialog.Builder(context).setTitle("��ʾ").setMessage("���Ĺ�����������ԤԼ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �Զ����ɵķ������
						dialog.dismiss();
					}
				}).create().show();
				Toast.makeText(context, "���Ĺ�����������ԤԼ", Toast.LENGTH_SHORT)
				.show();
			}
		});*/
		return view;
	}

	private class ItemHolder {
		TextView booktitle;
		TextView booklanguage;
		TextView bookoutNum;
		TextView bookstoreNum;
		//Button orderbook;
	}
}

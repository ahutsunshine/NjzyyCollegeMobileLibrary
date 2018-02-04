package example.mobilelibrary.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.iMobileLib.NjutcmMobileLibrary.R;

public class DownListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> data;
	public DownListAdapter(Context context,
			ArrayList<HashMap<String, String>> data) {
		this.context = context;
		this.data = data;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO 自动生成的方法存根
		ItemHolder holder;
		if (view == null) {
			holder = new ItemHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.ksearch_list_item, null);
			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		holder.imgView = (ImageView) view.findViewById(R.id.img);
		holder.txtView = (TextView) view.findViewById(R.id.txt);
		//
		HashMap<String, String> itemMap = data.get(arg0);
		holder.imgView.setImageBitmap((BitmapFactory.decodeResource(
				context.getResources(), Integer.parseInt(itemMap.get("img")))));
		holder.txtView.setText(itemMap.get("txt"));

		return view;
	}
	private class ItemHolder {
		ImageView imgView;
		TextView txtView;
	}
}

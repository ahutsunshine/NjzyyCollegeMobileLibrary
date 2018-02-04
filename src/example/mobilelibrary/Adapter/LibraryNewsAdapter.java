package example.mobilelibrary.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.iMobileLib.NjutcmMobileLibrary.R;
import example.mobilelibrary.entity.NewsBean;

public class LibraryNewsAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private ArrayList<NewsBean> data;
	
	public LibraryNewsAdapter(Context context, ArrayList<NewsBean> data) {
		this.context = context;
		this.data = data;
	}

	public int getCount() {
		return data.size();
	}

	public NewsBean getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		ItemHolder holder;
		if (view == null) {
			holder = new ItemHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.news_item, null);
			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		
		holder.titleView = (TextView) view.findViewById(R.id.title);
		holder.dateView= (TextView) view.findViewById(R.id.date);
		holder.contentView=(TextView)view.findViewById(R.id.content);
		
		NewsBean item = getItem(position);
		holder.titleView.setText(item.getTitle());
		System.out.println("LibNewsAdapter:标题"+item.getTitle());
		holder.dateView.setText(item.getDate());
		System.out.println("LibNewsAdapter:日期"+item.getDate());
		holder.contentView.setText(item.getContent());
		System.out.println("LibNewsAdapter:内容"+item.getContent());
		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView dateView;
		TextView contentView;
	}

}

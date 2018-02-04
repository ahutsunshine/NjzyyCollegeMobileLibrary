package example.mobilelibrary.Adapter;

import java.util.ArrayList;

import com.umeng.common.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.iMobileLib.NjutcmMobileLibrary.R;
import example.mobilelibrary.entity.KSearchBean;

/**
 * 
 * 下拉列表
 * 
 * @author Administrator
 * 
 */
public class KSearchListAdapter extends BaseAdapter {
	// 上下文
	private Context context;
	// 数据
	private ArrayList<KSearchBean> data;

	public KSearchListAdapter(Context context, ArrayList<KSearchBean> data) {
		this.context = context;
		this.data = data;
	}

	public int getCount() {
		return data.size();
	}

	public KSearchBean getItem(int position) {
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
			view = inflater.inflate(R.layout.newbook_list_item, null);
			holder.titleView = (TextView) view.findViewById(R.id.title);
			holder.authorView = (TextView) view.findViewById(R.id.author);

			view.setTag(holder);
		} else {
			holder = (ItemHolder) view.getTag();
		}
		//
		KSearchBean bean = getItem(position);
		holder.titleView.setText(bean.getTitle());
		String summary = "";
		if (bean.getAuthor() != null && !bean.getAuthor().contentEquals("")) {
			summary += "责任者: " + bean.getAuthor();
		}
		if (bean.getSource() != null && !bean.getSource().contentEquals("")) {
			if(!summary.contentEquals("")) summary += "\n";
			summary += "出处: " + bean.getSource();
		}
		if(bean.getPubdate()!=null){
			summary += "  出版社: " + bean.getPubdate()+"  ";
		}
		if(bean.getIsbn()!=null)
			summary += "  索书号: " + bean.getIsbn()+"  ";
		Log.d("chaoxing service", bean.getSummary());
		if (bean.getSummary() != null && !bean.getSummary().contentEquals("")) {
			if(!summary.contentEquals("")) summary += "\n";
			summary += bean.getSummary();
		}
		holder.authorView.setText(summary);
		return view;
	}

	private class ItemHolder {
		TextView titleView;
		TextView authorView;
		// TextView publishView;
		// TextView timeView;
	}
}

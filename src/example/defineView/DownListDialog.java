package example.defineView;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.iMobileLib.NjutcmMobileLibrary.R;



/**
 * 下拉对话框
 * 
 * @author 郭建亮
 * 
 */
public class DownListDialog extends Dialog {

	// list view
	private ListView lstView;

	private DownListDialog(Context context) {
		super(context);
	}

	private DownListDialog(Context context, int theme) {
		super(context, theme);
		View view = LayoutInflater.from(context).inflate(R.layout.ksearch_list,
				null);
		lstView = (ListView) view.findViewById(R.id.list1);
		setContentView(view);
	}

	/**
	 * 创建实例
	 * 
	 * @param context
	 * @return
	 */
	public static DownListDialog create(Context context) {
		return new DownListDialog(context, R.style.CustomeDialog);
	}

	/**
	 * 添加数据并设置监听器
	 * 
	 * @param adapter
	 * @param onDownItemClickListener
	 */
	public void setDownListAdaper(BaseAdapter adapter,
			OnDownItemClickListener onDownItemClickListener) {
		final OnDownItemClickListener onDownItemClickListener2 = onDownItemClickListener;
		lstView.setAdapter(adapter);
		lstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				onDownItemClickListener2.onItemClick((BaseAdapter)parent.getAdapter(),
						position);
				//
				dismiss();
			}
		});
	}

	public static interface OnDownItemClickListener {
		public void onItemClick(BaseAdapter adapter, int position);
	}
}

package cn.iMobileLib.NjutcmMobileLibrary;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mobilelibrary.widget.XListView;
import com.mobilelibrary.widget.XListView.IXListViewListener;

/**
 * 
 * 列表
 * 
 * @author Administrator
 * 
 */
public abstract class BaseListActivity extends BaseLoadingActivity implements
		IXListViewListener {

	// widget
	private XListView lstView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加root容器
		View view = LayoutInflater.from(this).inflate(R.layout.base_list, null);
		addRootView(view);
		// 变量实例化
		lstView = (XListView) findViewById(R.id.list1);
		//
		lstView.setPullLoadEnable(true);
		lstView.setXListViewListener(this);

	}

	/**
	 * 获取listview
	 * 
	 * @return
	 */
	protected XListView getListView() {
		return lstView;
	}

	@Override
	public void onMsgStart() {

	}

	@Override
	public void onMsgOk() {
		// 停止刷新
		lstView.stopLoadMore();
		lstView.stopRefresh();
		// 记录更新时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		lstView.setRefreshTime(sdf.format(new Date()));
	}

	@Override
	public void onMsgError() {
		lstView.stopLoadMore();
		lstView.stopRefresh();
	}

}

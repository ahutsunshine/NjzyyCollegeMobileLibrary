package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.mobilelibrary.api.ApiKSearch;
import com.mobilelibrary.widget.HeaderWidget;
import com.mobilelibrary.widget.XListView;

import example.mobilelibrary.Adapter.KSearchListAdapter;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.HostType;
import example.mobilelibrary.entity.KSearchBean;

public class KSearchListActivity extends BaseListActivity {
	private XListView listView;
	private HeaderWidget headerWidget;

	private KSearchListAdapter adapter;
	private ArrayList<KSearchBean> datas;
	private int page = 1;
	// 参数
	private G.HostType paramHost;
	private String paramKeyword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

		headerWidget = getHeaderView();
		headerWidget.setMiddleText("检索结果");
		listView = getListView();
		datas = new ArrayList<KSearchBean>();
		adapter = new KSearchListAdapter(this, datas);
		listView.setAdapter(adapter);
		listView.setDivider(null);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 可能因为添加了 headerview的缘故
				G.HostType type = G.HostType.valueOf(getIntent().getExtras().getInt("host"));
				Intent intent = new Intent();
				System.out.println("------>>type=" + type);
				switch (type) {
				case CAMBRIDGE: intent.putExtra("content", adapter.getItem(position-1).getSummary());
				intent.putExtra("author",  adapter.getItem(position-1).getAuthor());
				intent.putExtra("time",  adapter.getItem(position-1).getSummary().split("/")[3].split("DOI")[0].trim());
				//case WEIPU:intent.putExtra("weipu_title", adapter.getItem(position-1).getTitle().trim());
				case SINOMED:intent.putExtra("author",adapter.getItem(position-1).getAuthor().trim());
				case FMJS:intent.putExtra("fmjs_title",adapter.getItem(position-1).getTitle().trim());
				case FANGZHENG:
				case WEIPU:
				case CNKI:
				case WANFANG:
				case SCI:
				case THIEME:
				case SPRINGER: 
				case BMJ:
					String url = adapter.getItem(position - 1).getHref();					
					System.out.println("--->>KSearchListActivity url=" + url);
					intent.setClass(KSearchListActivity.this,KSearchDetailActivity.class);
					intent.putExtras(getIntent());
					intent.putExtra("url", url);
					startActivity(intent);
					break;
				default:
					break;
				}

			}
		});

		// 获取参数
		Bundle bundle = getIntent().getExtras();
		paramHost = G.HostType.valueOf(bundle.getInt("host"));
		paramKeyword = bundle.getString("keyword");
		asyncRequest(0);
	}

	@Override
	public void onRefresh() {
		asyncRequest(0);
		page = 1;
	}

	@Override
	public void onLoadMore() {
		page++;
		asyncRequest(1);
	}

	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		try {
			if(paramHost==HostType.FANGZHENG)
				return ApiKSearch.fangzhengKSearchList(paramKeyword, page+"");
			else 
				return ApiKSearch.queryKSearchList(paramHost, paramKeyword, page+ "");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void onAsyncInUI(Object object) {
		@SuppressWarnings("unchecked")
		ArrayList<KSearchBean> datasTemp = (ArrayList<KSearchBean>) object;
		// 如果第一页，先清除之前的数据
		if (page == 1) {
			datas.clear();
		}
		if (datasTemp == null) {
			showNoneTip();
			Toast.makeText(KSearchListActivity.this, "请求失败,请稍后再试...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 如果未请求到数据
		if (datasTemp.size() == 0) {
			// 首次请求，显示无结果视图
			if (page == 1) {
				showNoneTip();
			} else {// 加载更多未请求到，提示无更多
				Toast.makeText(KSearchListActivity.this, "没有更多内容",
						Toast.LENGTH_SHORT).show();
			}
			return;
		}

		datas.addAll(datasTemp);
		adapter.notifyDataSetChanged();

	}

}

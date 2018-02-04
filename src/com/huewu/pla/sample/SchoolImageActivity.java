package com.huewu.pla.sample;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.iMobileLib.NjutcmMobileLibrary.R;

import com.dodola.model.ImageItem;
import com.dodowaterfall.widget.ScaleImageView;
import com.huewu.pla.lib.internal.PLA_AdapterView;

public class SchoolImageActivity extends FragmentActivity implements IXListViewListener {
	
	public static final int COLUMNS = 2;

	private AssetManager asset_manager;
	private List<String> image_filenames;
	public static final String image_path = "school_images";
	
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    private int imageWidth;
    ContentTask task = new ContentTask(this);
    ProgressDialog progressDialog=null;

    private class ContentTask extends AsyncTask<String, Integer, List<ImageItem>> {

        public ContentTask(Context context) {
            super();
        }

        @Override
        protected List<ImageItem> doInBackground(String... params) {
            try {
                return loadImages();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ImageItem> result) {
            mAdapterView.stopLoadMore();
            mAdapter.addItemLast(result);
            mAdapter.notifyDataSetChanged();
            handler.sendEmptyMessage(0);
        }

        @Override
        protected void onPreExecute() {
        	
        }
    }

    private List<ImageItem> loadImages() throws Exception {
		asset_manager = getAssets();
		try {
			image_filenames = Arrays.asList(asset_manager.list(image_path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<ImageItem> items = new ArrayList<ImageItem>();
		for(int i = 0; i < image_filenames.size(); i++) {
			String filename = image_filenames.get(i);
			ImageItem item = new ImageItem();

			BufferedInputStream buf = new BufferedInputStream(SchoolImageActivity.this.getAssets()
						.open(image_path + "/" + filename));
			Bitmap bitmap = BitmapFactory.decodeStream(buf);

			float height = bitmap.getHeight() * ((float)imageWidth
						/ (float)bitmap.getWidth());
			item.image = bitmap;
			item.setHeight((int)height);
			item.fileName = filename;
			items.add(item);
		}
		return items;
    }
    Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		super.handleMessage(msg);
    		if(progressDialog!=null&&progressDialog.isShowing()){
    			progressDialog.dismiss();
    		}
    	};
    };
    private LinkedList<ImageItem> mInfos;
    public class StaggeredAdapter extends BaseAdapter {
        public StaggeredAdapter(Context context, XListView xListView) {
            mInfos = new LinkedList<ImageItem>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            ImageItem imageItem = mInfos.get(position);
            
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.infos_list, null);
                holder = new ViewHolder();
                holder.imageView = (ScaleImageView) convertView.findViewById(R.id.news_pic);
                holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
            holder.imageView.setImageWidth(imageWidth);
            holder.imageView.setImageHeight(imageItem.getHeight());
            holder.imageView.setImageBitmap(imageItem.image);
            holder.contentView.setText(ImageItem.imageInfoMap.get(imageItem.fileName));
            
            return convertView;
        }

        class ViewHolder {
            ScaleImageView imageView;
            TextView contentView;
            TextView timeView;
        }

        @Override
        public int getCount() {
            return mInfos.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mInfos.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        public void addItemLast(List<ImageItem> datas) {
            mInfos.addAll(datas);
        }

        public void addItemTop(List<ImageItem> datas) {
            for (ImageItem info : datas) {
                mInfos.addFirst(info);
            }
        }
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pull_to_refresh_sample);
        progressDialog=new ProgressDialog(SchoolImageActivity.this);
        progressDialog.setMessage("正在加载，请稍等...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    	int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
    	imageWidth = (screenWidth - 30) / COLUMNS;
    	
        mAdapterView = (XListView) findViewById(R.id.list);
        mAdapterView.setPullLoadEnable(false);
        mAdapterView.setPullRefreshEnable(false);
        mAdapterView.setXListViewListener(this);
        mAdapterView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(SchoolImageActivity.this, SchoolImageViewerActivty.class);
				i.putExtra("fileName", mInfos.get(position - 1).fileName);
				startActivity(i);
			}
		});

        mAdapter = new StaggeredAdapter(this, mAdapterView);
        task.execute();
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapterView.setAdapter(mAdapter);
    }

    @Override
    public void onLoadMore() {
    }

	@Override
	public void onRefresh() {
		
	}
	private int clicknum = 0;
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		clicknum=0;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			if(clicknum==0){
			Toast.makeText(getApplicationContext(), "再按一下返回键退出程序!",
					Toast.LENGTH_SHORT).show();	
			clicknum++;
			}
			else finish();
			return true;
		}	
		else
		return super.onKeyDown(keyCode, event);		
	}
}

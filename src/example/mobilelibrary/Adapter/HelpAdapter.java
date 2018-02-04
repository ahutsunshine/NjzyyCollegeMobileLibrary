package example.mobilelibrary.Adapter;

import java.util.List;



import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.View;




public class HelpAdapter extends PagerAdapter{

	List<View> lst;
	public HelpAdapter(List<View> l){
		lst=l;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return lst.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO 自动生成的方法存根
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(final View container, int position) {
		// TODO 自动生成的方法存根
		  
		((ViewPager) container).addView(lst.get(position), 0);
		return lst.get(position);
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO 自动生成的方法存根
		((ViewPager) container).removeView(lst.get(position));
	}
	
}

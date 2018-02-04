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
		// TODO �Զ����ɵķ������
		return lst.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO �Զ����ɵķ������
		return arg0==arg1;
	}
	@Override
	public Object instantiateItem(final View container, int position) {
		// TODO �Զ����ɵķ������
		  
		((ViewPager) container).addView(lst.get(position), 0);
		return lst.get(position);
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO �Զ����ɵķ������
		((ViewPager) container).removeView(lst.get(position));
	}
	
}

package example.defineView;





import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.iMobileLib.NjutcmMobileLibrary.R;

public class SlidingSwitcherView extends RelativeLayout implements OnTouchListener{
	
	public static final int SNAP_VELOCITY = 200;  
	private int switcherViewWidth;  
	private int currentItemIndex;  
	 private int itemsCount;
	 private int[] borders; 
	 private int leftEdge = 0; 
	 private int rightEdge = 0;
	 private float xDown; 
	 private float xMove;
	 private float xUp;
	 private LinearLayout itemsLayout;
	 private LinearLayout dotsLayout;
	 private View firstItem; 
	 private MarginLayoutParams firstItemParams; 
	 private VelocityTracker mVelocityTracker; 
	 public SlidingSwitcherView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO 自动生成的构造函数存根
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingSwitcherView);  
		    boolean isAutoPlay = a.getBoolean(R.styleable.SlidingSwitcherView_auto_play, false);  
		    if (isAutoPlay) {  
		        startAutoPlay();  
		    }  
		    a.recycle();  		   
		}
	 public void scrollToNext() {  
	        new ScrollTask().execute(-20);  
	    }  
	 public void scrollToPrevious() {  
	        new ScrollTask().execute(20);  
	    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自动生成的方法存根
		 createVelocityTracker(event);  
	        switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	            // 手指按下时，记录按下时的横坐标  
	            xDown = event.getRawX();  
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	            // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整左侧布局的leftMargin值，从而显示和隐藏左侧布局  
	            xMove = event.getRawX();  
	            int distanceX = (int) (xMove - xDown) - (currentItemIndex * switcherViewWidth);  
	            firstItemParams.leftMargin = distanceX;  
	            if (beAbleToScroll()) {  
	                firstItem.setLayoutParams(firstItemParams);  
	            }  
	            break;  
	        case MotionEvent.ACTION_UP:  
	            // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到左侧布局，还是滚动到右侧布局  
	            xUp = event.getRawX();  
	            if (beAbleToScroll()) {  
	                if (wantScrollToPrevious()) {  
	                    if (shouldScrollToPrevious()) {  
	                        currentItemIndex--;  
	                        scrollToPrevious();  
	                        refreshDotsLayout();  
	                    } else {  
	                        scrollToNext();  
	                    }  
	                } else if (wantScrollToNext()) {  
	                    if (shouldScrollToNext()) {  
	                        currentItemIndex++;  
	                        scrollToNext();  
	                        refreshDotsLayout();  
	                    } else {  
	                        scrollToPrevious();  
	                    }  
	                }  
	            }  
	            recycleVelocityTracker();  
	            break; 
	        }  
		return false;
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO 自动生成的方法存根
		super.onLayout(changed, l, t, r, b);
		if (changed) {  
            initializeItems();           
            initializeDots();  
        }  
	}
	 private void initializeItems() {  
	        switcherViewWidth = getWidth();  
	        itemsLayout = (LinearLayout) getChildAt(0);  
	        itemsCount = itemsLayout.getChildCount();  
	        borders = new int[itemsCount];  
	        for (int i = 0; i < itemsCount; i++) {  
	            borders[i] = -i * switcherViewWidth;  
	            View item = itemsLayout.getChildAt(i);  
	            MarginLayoutParams params = (MarginLayoutParams) item.getLayoutParams();  
	            params.width = switcherViewWidth;  
	            item.setLayoutParams(params);  
	            item.setOnTouchListener(this);  
	        }  
	        leftEdge = borders[itemsCount - 1];  
	        firstItem = itemsLayout.getChildAt(0);  
	        firstItemParams = (MarginLayoutParams) firstItem.getLayoutParams();  
	    }
	 private void initializeDots() {  
	        dotsLayout = (LinearLayout) getChildAt(1);  
	        refreshDotsLayout();  
	    }  
	 private boolean beAbleToScroll() {  
	        return firstItemParams.leftMargin < rightEdge && firstItemParams.leftMargin > leftEdge;  
	    }  
	 private boolean wantScrollToPrevious() {  
	        return xUp - xDown > 0;  
	    } 
	 private boolean wantScrollToNext() {  
	        return xUp - xDown < 0;  
	    } 
	 private boolean shouldScrollToNext() {  
	        return xDown - xUp > switcherViewWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
	    }  
	 private boolean shouldScrollToPrevious() {  
	        return xUp - xDown > switcherViewWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;  
	    }  
	 private void refreshDotsLayout() {  
	        dotsLayout.removeAllViews();  
	        for (int i = 0; i < itemsCount; i++) {  
	            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(0,  
	                    LayoutParams.FILL_PARENT);  
	            linearParams.weight = 1;  
	            RelativeLayout relativeLayout = new RelativeLayout(getContext());  
	            ImageView image = new ImageView(getContext());  
	            if (i == currentItemIndex) {  
	                image.setBackgroundResource(R.drawable.dot_1);  
	            } else {  
	                image.setBackgroundResource(R.drawable.dot_2);  
	            }	       
	            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(  
	                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
	            relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);  
	            relativeLayout.addView(image, relativeParams);  
	            dotsLayout.addView(relativeLayout, linearParams); 
	        }  
	    }  
	 private void createVelocityTracker(MotionEvent event) {  
	        if (mVelocityTracker == null) {  
	            mVelocityTracker = VelocityTracker.obtain();  
	        }  
	        mVelocityTracker.addMovement(event);  
	    }  
	 private int getScrollVelocity() {  
	        mVelocityTracker.computeCurrentVelocity(1000);  
	        int velocity = (int) mVelocityTracker.getXVelocity();  
	        return Math.abs(velocity);  
	    } 
	 private void recycleVelocityTracker() {  
	        mVelocityTracker.recycle();  
	        mVelocityTracker = null;  
	    }  
	 private boolean isCrossBorder(int leftMargin, int speed) {  
	        for (int border : borders) {  
	            if (speed > 0) {  
	                if (leftMargin >= border && leftMargin - speed < border) {  
	                    return true;  
	                }  
	            } else {  
	                if (leftMargin <= border && leftMargin - speed > border) {  
	                    return true;  
	                }  
	            }  
	        }  
	        return false;  
	    }
	 private int findClosestBorder(int leftMargin) {  
	        int absLeftMargin = Math.abs(leftMargin);  
	        int closestBorder = borders[0];  
	        int closestMargin = Math.abs(Math.abs(closestBorder) - absLeftMargin);  
	        for (int border : borders) {  
	            int margin = Math.abs(Math.abs(border) - absLeftMargin);  
	            if (margin < closestMargin) {  
	                closestBorder = border;  
	                closestMargin = margin;  
	            }  
	        }  
	        return closestBorder;  
	    } 
	 class ScrollTask extends AsyncTask<Integer, Integer, Integer> {  
		  
	        @Override  
	        protected Integer doInBackground(Integer... speed) {  
	            int leftMargin = firstItemParams.leftMargin;  
	            // 根据传入的速度来滚动界面，当滚动穿越border时，跳出循环。  
	            while (true) {  
	                leftMargin = leftMargin + speed[0];  
	                if (isCrossBorder(leftMargin, speed[0])) {  
	                    leftMargin = findClosestBorder(leftMargin);  
	                    break;  
	                }  
	                publishProgress(leftMargin);  
	                // 为了要有滚动效果产生，每次循环使线程睡眠10毫秒，这样肉眼才能够看到滚动动画。  
	                sleep(10);  
	            }  
	            return leftMargin;  
	        } 
	        @Override  
	        protected void onProgressUpdate(Integer... leftMargin) {  
	            firstItemParams.leftMargin = leftMargin[0];  
	            firstItem.setLayoutParams(firstItemParams);  
	        }  
	  
	        @Override  
	        protected void onPostExecute(Integer leftMargin) {  
	            firstItemParams.leftMargin = leftMargin;  
	            firstItem.setLayoutParams(firstItemParams);  
	        }  
	    }
	 private void sleep(long millis) {  
	        try {  
	            Thread.sleep(millis);  
	        } catch (InterruptedException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    class ScrollToFirstItemTask extends AsyncTask<Integer, Integer, Integer> {  
	        
	        @Override  
	        protected Integer doInBackground(Integer... speed) {  
	            int leftMargin = firstItemParams.leftMargin;  
	            while (true) {  
	                leftMargin = leftMargin + speed[0];  
	                // 当leftMargin大于0时，说明已经滚动到了第一个元素，跳出循环  
	                if (leftMargin > 0) {  
	                    leftMargin = 0;  
	                    break;  
	                }  
	                publishProgress(leftMargin);  
	                sleep(20);  
	            }  
	            return leftMargin;  
	        }  
	      
	        @Override  
	        protected void onProgressUpdate(Integer... leftMargin) {  
	            firstItemParams.leftMargin = leftMargin[0];  
	            firstItem.setLayoutParams(firstItemParams);  
	        }  
	      
	        @Override  
	        protected void onPostExecute(Integer leftMargin) {  
	            firstItemParams.leftMargin = leftMargin;  
	            firstItem.setLayoutParams(firstItemParams);  
	        }  
	      
	    }  
	    public void scrollToFirstItem() {  
	        new ScrollToFirstItemTask().execute(20 * itemsCount);  
	    }  
	    /** 
	     * 用于在定时器当中操作UI界面。 
	     */  
	    private Handler handler = new Handler();  
	      
	    /** 
	     * 开启图片自动播放功能，当滚动到最后一张图片的时候，会自动回滚到第一张图片。 
	     */  
	    public void startAutoPlay() {  
	        new Timer().scheduleAtFixedRate(new TimerTask() {  
	            @Override  
	            public void run() {  
	                if (currentItemIndex == itemsCount - 1) {  
	                    currentItemIndex = 0;  
	                    handler.post(new Runnable() {  
	                        @Override  
	                        public void run() {  
	                            scrollToFirstItem();  
	                            refreshDotsLayout();  
	                        }  
	                    });  
	                } else {  
	                    currentItemIndex++;  
	                    handler.post(new Runnable() {  
	                        @Override  
	                        public void run() {  
	                            scrollToNext();  
	                            refreshDotsLayout();  
	                        }  
	                    });  
	                }  
	            }  
	        }, 3000, 3000);  
	    }  
}


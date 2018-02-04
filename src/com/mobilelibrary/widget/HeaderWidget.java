package com.mobilelibrary.widget;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.iMobileLib.NjutcmMobileLibrary.R;

//*自定义控件的步骤
/**1.我们的自定义控件和其他的控件一样,应该写成一个类,而这个类的属性是是有自己来决定的.

2.我们要在res/values目录下建立一个attrs.xml的文件,并在此文件中增加对控件的属性的定义.

3.使用AttributeSet来完成控件类的构造函数,并在构造函数中将自定义控件类中变量与attrs.xml中的属性连接起来.

4.在自定义控件类中使用这些已经连接的属性变量.

5.将自定义的控件类定义到布局用的xml文件中去.

6.在界面中生成此自定义控件类对象,并加以使用.*/

public class HeaderWidget extends RelativeLayout {
	//
	private Activity activity;
	private int currentLight;
	private boolean toogleClose = false;
	//
	private TextView leftBtn, middleContent, rightBtn;

	public HeaderWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		//
		activity = (Activity) context;
		// 获取屏幕的亮度
		currentLight = getScreenBrightness(activity);
		// bind the view
		View view = LayoutInflater.from(context).inflate(R.layout.header, this,
				true);
		//
		leftBtn = (TextView) view.findViewById(R.id.left_btn);
		middleContent = (TextView) view.findViewById(R.id.middle_content);
		rightBtn = (TextView) view.findViewById(R.id.right_btn);
		// 加载属性
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.Header);
		// 赋值
		leftBtn.setText(a.getString(R.styleable.Header_left_text));
		// leftBtn.getPaint().setFakeBoldText(true);
		middleContent.setText(a.getString(R.styleable.Header_middle_text));
		// 绑定事件
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		//
		a.recycle();
	}

	//
	public void setLeftText(String text) {
		leftBtn.setText(text);
	}

	//
	public void setMiddleText(String text) {
		middleContent.setText(text);
	}

	public void setRightText(String text) {
		rightBtn.setText(text);
	}

	public void bindCickToRightBtn(OnClickListener onClickListener) {
		rightBtn.setOnClickListener(onClickListener);
	}

	public View getRightBtn() {
		return rightBtn;
	}

	public void hiddenRightBtn(boolean hidden) {
		if (hidden) {
			rightBtn.setVisibility(View.INVISIBLE);
		} else {
			rightBtn.setVisibility(View.VISIBLE);
		}
	}

	//
	@SuppressWarnings("unused")
	private void changeLight() {
		if (!toogleClose) {
			setBrightness(activity, 2);
		} else {
			setBrightness(activity, currentLight);
		}
		toogleClose = !toogleClose;
	}

	/**
	 * 获取屏幕的亮度
	 */
	private int getScreenBrightness(Activity activity) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	/**
	 * 设置亮度
	 */
	private void setBrightness(Activity activity, int brightness) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
		activity.getWindow().setAttributes(lp);
	}

}

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

//*�Զ���ؼ��Ĳ���
/**1.���ǵ��Զ���ؼ��������Ŀؼ�һ��,Ӧ��д��һ����,�������������������Լ���������.

2.����Ҫ��res/valuesĿ¼�½���һ��attrs.xml���ļ�,���ڴ��ļ������ӶԿؼ������ԵĶ���.

3.ʹ��AttributeSet����ɿؼ���Ĺ��캯��,���ڹ��캯���н��Զ���ؼ����б�����attrs.xml�е�������������.

4.���Զ���ؼ�����ʹ����Щ�Ѿ����ӵ����Ա���.

5.���Զ���Ŀؼ��ඨ�嵽�����õ�xml�ļ���ȥ.

6.�ڽ��������ɴ��Զ���ؼ������,������ʹ��.*/

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
		// ��ȡ��Ļ������
		currentLight = getScreenBrightness(activity);
		// bind the view
		View view = LayoutInflater.from(context).inflate(R.layout.header, this,
				true);
		//
		leftBtn = (TextView) view.findViewById(R.id.left_btn);
		middleContent = (TextView) view.findViewById(R.id.middle_content);
		rightBtn = (TextView) view.findViewById(R.id.right_btn);
		// ��������
		TypedArray a = context
				.obtainStyledAttributes(attrs, R.styleable.Header);
		// ��ֵ
		leftBtn.setText(a.getString(R.styleable.Header_left_text));
		// leftBtn.getPaint().setFakeBoldText(true);
		middleContent.setText(a.getString(R.styleable.Header_middle_text));
		// ���¼�
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
	 * ��ȡ��Ļ������
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
	 * ��������
	 */
	private void setBrightness(Activity activity, int brightness) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
		activity.getWindow().setAttributes(lp);
	}

}

package example.mobilelibrary.entity;

import android.util.Log;

/**
 * 调试类
 * 
 * @author 郭建亮
 * 
 */
public class D {
	public static final boolean debug = true;

	/**
	 * 打印：System.out
	 * 
	 * @param msg
	 * 
	 * 
	 */
	public static void out(String msg) {
		if (debug)
			System.out.println(msg);
	}

	/**
	 * 打印：debug
	 * 
	 * @param msg
	 * @param tag
	 * 
	 * 
	 */
	public static void d(String tag, String msg) {
		if (debug)
			Log.d(tag, msg);
	}
}

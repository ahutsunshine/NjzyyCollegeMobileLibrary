package example.mobilelibrary.entity;

import android.util.Log;

/**
 * ������
 * 
 * @author ������
 * 
 */
public class D {
	public static final boolean debug = true;

	/**
	 * ��ӡ��System.out
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
	 * ��ӡ��debug
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

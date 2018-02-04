package example.mobilelibrary.entity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public final class G {

	/**
	 * �Ƿ���debugģʽ
	 */
	public static boolean debug = false;
	/**
	 * ������
	 */
	public static Context context;
	/**
	 * ��������
	 * 
	 */
	public static enum HostType {
		// ����Ϊ���ķ�����
		CNKI(0), WANFANG(1), WEIPU(2), FANGZHENG(5), CHAOXING(3), SINOMED(4),
		// ����ΪӢ�ķ�����
		FMJS(19), SCI(20), THIEME(21), CAMBRIDGE(22), BMJ(23), BMJGROUP(24), SPRINGER(25);

		private int value = -1;

		private HostType(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		public static HostType valueOf(int value) {
			HostType type = null;
			switch (value) {
			// ����Ϊ����
			case 0:
				type = CNKI;
				break;
			case 1:
				type = WANFANG;
				break;
			case 2:
				type = WEIPU;
				break;
			case 3:
				type = CHAOXING;
				break;
			case 4:
				type = SINOMED;
				break;
			case 5:
				type = FANGZHENG;
				break;
			// ����Ӣ��
			case 19:
				type = FMJS;
				break;
			case 20:
				type = SCI;
				break;
			case 21:
				type = THIEME;
				break;
			case 22:
				type = CAMBRIDGE;
				break;
			case 23:
				type = BMJ;
				break;
			case 24:
				type = BMJGROUP;
				break;
			case 25:
				type = SPRINGER;
				break;

			default:
				break;
			}

			return type;
		}
	}

	/**
	 * ��������
	 * 
	 */
	public static enum LanguageType {
		CH, EN;
	}

	/**
	 * ��������
	 * 
	 */
	public static enum NewsType {
		SCHOOL, SOCIAL, LIBRARY;
	}

	/**
	 * @param context
	 * @return �������ӷ�ʽ ��WIFI��3G��2G��
	 */
	public static boolean Network=true;
	public static String getNetworkType(android.content.Context context) {
	    String netType = null;
	    ConnectivityManager cm =
	    		(ConnectivityManager) context
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    if (null != ni) {
	        int nType = ni.getType();
	        if (nType == ConnectivityManager.TYPE_MOBILE) {
	            netType="2G/3G";
	        } else {
	            netType="else";
	        }
	    }
	    System.out.println("---->>newsType="+netType);
	    if(netType==null) return "else"; 
	    return netType;
	}
	/**
	 * �쳣����
	 */
	public static void abnormal(int flag,Context context){
		if(flag==1){
			Toast.makeText(context, "����ʧ�ܣ����Ժ�����",
					Toast.LENGTH_SHORT).show();
			System.out.println("shibai");
			return;
		}
		else{
			Toast.makeText(context, "��������",
					Toast.LENGTH_SHORT).show();
			System.out.println("shi");
			return;
		}
	}

	/**
	 * �ļ�������
	 * 
	 */
	public static final class asset {
		public static String readAllText(String filename) {
			String txt = "";
			BufferedReader reader = null;
			try {
				InputStream inputStream = G.context.getAssets().open(filename);
				reader = new BufferedReader(new InputStreamReader(inputStream));
				String t = "";
				while ((t = reader.readLine()) != null) {
					txt += t + "\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Log.i("gjl", filename + "<>" + txt);
			return txt;
		}
	}
}

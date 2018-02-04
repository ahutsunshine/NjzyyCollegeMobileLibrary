package cn.iMobileLib.NjutcmMobileLibrary;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class PushReceiver extends BroadcastReceiver {

	private static final String TAG = "PushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收Registration Id : " + regId);
            //send the Registration Id to your server...
        }else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())){
        	String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收UnRegistration Id : " + regId);
          //send the UnRegistration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        	SharedPreferences.Editor editor = prefs.edit();
        	editor.putBoolean("hasNews", true);
        	editor.commit();
        	String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        	try {
        		JSONTokener jsonParser = new JSONTokener(message);
        		JSONObject ret = (JSONObject) jsonParser.nextValue();
        		String type = ret.getString("type");
        		if(!type.contentEquals("")) {
        			String id = ret.getString("id");
        			String content = ret.getString("content");
        			pushNotification(context, type, id, content);
        		}
        	} catch (Exception ex) {
        		Log.d(TAG, ex.getMessage());
        	}
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        	SharedPreferences.Editor editor = prefs.edit();
        	editor.putBoolean("hasNews", false);
        	editor.commit();
        	//打开自定义的Activity
        	Intent i = new Intent(context, MainActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            //Log.d(TAG, "用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else {
        	Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	private void pushNotification(Context context, String type, String id, String message) {
		Intent i;
		i = new Intent(context, DetailShowActivity.class);
		if (type.contentEquals("libnews")) {
			i.putExtra("newstype", 2);
		} else if (type.contentEquals("schnews")) {
			i.putExtra("newstype", 0);
		} else return;
		i.putExtra("link", id);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationManager nm = (NotificationManager) context
		.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.icon, message,
		System.currentTimeMillis());
		n.defaults |= Notification.DEFAULT_SOUND;
		n.defaults |= Notification.DEFAULT_VIBRATE;
		n.flags = Notification.FLAG_AUTO_CANCEL;
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.ledARGB = 0xff00ff00;
		n.ledOnMS = 300;
		n.ledOffMS = 1000;// LED
		n.setLatestEventInfo(context, "移动图书馆通知", message, pendingIntent);
		nm.notify(0, n);
	}

}

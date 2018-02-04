package cn.iMobileLib.NjutcmMobileLibrary;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

/**
 * 
 * 
 * 
 * @author Administrator
 * 
 */
public class BaseSearchActivity extends Activity implements
		RecognizerDialogListener {
	// 语音功能
	private RecognizerDialog soundDialog;
	// 输入框
	private EditText inputText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		initSoundFun();
	}

	// 初始化语音功能
	private void initSoundFun() {
		// 语音功能
		soundDialog = new RecognizerDialog(this, this.getResources().getString(
				R.string.sound_appid));
		// ===注册监听器
		soundDialog.setListener(this);
		// ===类型：sms普通文本
		soundDialog.setEngine("sms", null, null);
		// ===采样频率--考虑android支持情况8k、16k；所以一般采用16k
		soundDialog.setSampleRate(RATE.rate16k);
	}

	/**
	 * 绑定输入框，语音结果可以在此view上显示内容
	 * 
	 * @param editText
	 */
	public void bindSoundView(EditText editText) {
		inputText = editText;
	}

	// bind the sound view click event
	public void soundClick(View v) {
		soundDialog.show();
	}

	@Override
	public void onEnd(SpeechError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(ArrayList<RecognizerResult> arg0, boolean arg1) {
		// TODO Auto-generated method stub
		String result = "";
		for (int i = 0; i < arg0.size(); i++) {
			result += arg0.get(i).text;
		}
		result = result.replace("。", "");
		inputText.append(result);
		inputText.setSelection(inputText.getText().length());
	}
}

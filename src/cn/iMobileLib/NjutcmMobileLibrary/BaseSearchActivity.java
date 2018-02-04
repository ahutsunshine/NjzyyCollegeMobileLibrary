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
	// ��������
	private RecognizerDialog soundDialog;
	// �����
	private EditText inputText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		initSoundFun();
	}

	// ��ʼ����������
	private void initSoundFun() {
		// ��������
		soundDialog = new RecognizerDialog(this, this.getResources().getString(
				R.string.sound_appid));
		// ===ע�������
		soundDialog.setListener(this);
		// ===���ͣ�sms��ͨ�ı�
		soundDialog.setEngine("sms", null, null);
		// ===����Ƶ��--����android֧�����8k��16k������һ�����16k
		soundDialog.setSampleRate(RATE.rate16k);
	}

	/**
	 * �������������������ڴ�view����ʾ����
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
		result = result.replace("��", "");
		inputText.append(result);
		inputText.setSelection(inputText.getText().length());
	}
}

package mars.ClassUtil;

import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;

public class WebScale {
	// ���ִ�С
	private TextSize[] textSizes = { TextSize.SMALLEST, TextSize.SMALLER,
			TextSize.NORMAL, TextSize.LARGER, TextSize.LARGEST };
	// ��ǰ���ִ�С
	private int currentTextSize = 4;// Ĭ��ΪNORMAL

	private WebView webView;

	public WebScale(WebView webView) {
		this.webView = webView;
	}

	// change the webView TextSize
	// -1 ��С��1 �Ŵ�
	private void changeTextSize(int director) {
		currentTextSize += director;
		if (currentTextSize > 4) {// �Ŵ����
			currentTextSize = 4;
		}
		if (currentTextSize < 0) {// ��С����С
			currentTextSize = 0;
		}
		webView.getSettings().setTextSize(textSizes[currentTextSize]);
	}

	// ����Ŵ�
	public void scaleMax() {
		changeTextSize(1);
	}

	// ������С
	public void scaleMin() {
		changeTextSize(-1);
	}

	// �Ŵ���ͼ
	public void zoomIn() {
		webView.zoomIn();
	}

	// ��С��ͼ
	public void zoomOut() {
		webView.zoomOut();
	}
}

package mars.ClassUtil;

import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;

public class WebScale {
	// 文字大小
	private TextSize[] textSizes = { TextSize.SMALLEST, TextSize.SMALLER,
			TextSize.NORMAL, TextSize.LARGER, TextSize.LARGEST };
	// 当前文字大小
	private int currentTextSize = 4;// 默认为NORMAL

	private WebView webView;

	public WebScale(WebView webView) {
		this.webView = webView;
	}

	// change the webView TextSize
	// -1 缩小；1 放大
	private void changeTextSize(int director) {
		currentTextSize += director;
		if (currentTextSize > 4) {// 放大到最大
			currentTextSize = 4;
		}
		if (currentTextSize < 0) {// 缩小到最小
			currentTextSize = 0;
		}
		webView.getSettings().setTextSize(textSizes[currentTextSize]);
	}

	// 字体放大
	public void scaleMax() {
		changeTextSize(1);
	}

	// 字体缩小
	public void scaleMin() {
		changeTextSize(-1);
	}

	// 放大视图
	public void zoomIn() {
		webView.zoomIn();
	}

	// 缩小视图
	public void zoomOut() {
		webView.zoomOut();
	}
}

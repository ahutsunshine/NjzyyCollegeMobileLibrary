package com.mobilelibrary.api;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

/**
 * 解析xml
 * 
 * 
 */
public class ApiBaseXml {

	/**
	 * 解析xml文件
	 * 
	 * @param xmlStr
	 * @throws Exception
	 */
	public static void parseXml(String xmlStr, OnXmlCallback onXmlCallback) {
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(new StringReader(xmlStr));
			int evtType = xmlParser.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tagName = xmlParser.getName();
				switch (evtType) {
				case XmlPullParser.START_TAG:// 标签开始
					onXmlCallback.onTagStart(tagName, xmlParser);
					break;

				case XmlPullParser.END_TAG:// 标签结束
					onXmlCallback.onTagEnd(tagName, xmlParser);
					break;
				default:
					break;
				}
				evtType = xmlParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * xml解析回调
	 * 
	 */
	public static interface OnXmlCallback {
		public void onTagStart(String tagName, XmlPullParser xmlParser)
				throws Exception;

		public void onTagEnd(String tagName, XmlPullParser xmlParser)
				throws Exception;
	}
}

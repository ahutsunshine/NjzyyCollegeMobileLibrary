package com.mobilelibrary.api;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

/**
 * ����xml
 * 
 * 
 */
public class ApiBaseXml {

	/**
	 * ����xml�ļ�
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
				case XmlPullParser.START_TAG:// ��ǩ��ʼ
					onXmlCallback.onTagStart(tagName, xmlParser);
					break;

				case XmlPullParser.END_TAG:// ��ǩ����
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
	 * xml�����ص�
	 * 
	 */
	public static interface OnXmlCallback {
		public void onTagStart(String tagName, XmlPullParser xmlParser)
				throws Exception;

		public void onTagEnd(String tagName, XmlPullParser xmlParser)
				throws Exception;
	}
}

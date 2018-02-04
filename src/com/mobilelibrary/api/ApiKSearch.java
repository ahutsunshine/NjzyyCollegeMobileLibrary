package com.mobilelibrary.api;

import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mars.getdata.GetDataFromWeb;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import com.mobilelibrary.api.ApiBaseXml.OnXmlCallback;
import com.umeng.common.Log;

import example.mobilelibrary.entity.D;
import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.HostType;
import example.mobilelibrary.entity.KSearchBean;
import example.mobilelibrary.entity.KSearchDetailBean;

public class ApiKSearch {
	
	/**
	 * 列表
	 * 
	 * @param hostType
	 *            服务器类型
	 * @param keyword
	 *            搜索关键词
	 * @param page
	 *            页码
	 * @return
	 */
	public static String contentURI=null;
	public static List<KSearchBean> queryKSearchList(G.HostType hostType,
			String keyword, String page) throws Exception {
		// 地址
		String url = "";
		String startTag = "";
		switch (hostType) {
		case WEIPU:
			url += "cnkiTestService.asmx/";
			page=Integer.parseInt(page)+1+"";
			url += "searchMoreCnkiList";
			startTag = "cnkiDoc";
			break;
		case CNKI:
		
			url += "cnkiTestService.asmx/";
			if (page.equals("1")) {
				url += "searchCnkiList";
			} else {
				url += "searchMoreCnkiList";
			}
			startTag = "cnkiDoc";
			break;
		case WANFANG:
			url += "wanfangService.asmx/searchPaper";
			startTag = "wanfangPaper";
			break;
		case FANGZHENG:
			url += "apabiService.asmx/searchApabi";
			startTag = "apabiBook";
			break;
		case FMJS:
			url += "fmjsService.asmx/searchFmjs";
			startTag = "fmjsJournal";
			break;
		case SCI:
			url += "sciService.asmx/";
			if (page.equals("1")) {
				url += "searchSci";
			} else {
				url += "searchMoreSci";
			}
			startTag = "sciPaper";
			break;
		case CHAOXING:
			url += "chaoxingService.asmx/searchChaoxingBook";
			startTag = "chaoxingBook";
			break;
		/*case WEIPU:
			url += "weipuService.asmx/searchWeipu";
			startTag = "weipuPaper";
			break;*/
		case SINOMED:
			url += "sinomedService.asmx/";
			if (page.equals("1")) {
				url += "searchSinomed";
			} else {
				url += "searchMoreSinomed";
			}
			startTag = "sinomedPaper";
			break;
		case THIEME:
			url += "thiemeService.asmx/searchThieme";
			startTag = "thiemeJournal";
			break;
		case CAMBRIDGE:
			url += "cambridgeService.asmx/";
			if (page.equals("1")) {
				url += "searchCambridge";
			} else {
				url += "searchMoreCambridge";
			}
			startTag = "cambridgeJournal";
			break;
		case BMJ:
			url += "bmjService.asmx/searchBmj";
			startTag = "bmjJournal";
			break;
		case BMJGROUP:
			url += "bmjCeService.asmx/searchBmjCE";
			startTag = "bmjCe";
			break;
		case SPRINGER:
			url += "springerService.asmx/searchSpringer";
			startTag = "springerArticle";
			break;
		default:
			break;
		}
		// 根tag
		final String rootTag = startTag;
		// 返回结果
		final List<KSearchBean> items = new ArrayList<KSearchBean>();
		int i=0;
		String ret=null;
		while(i!=3&&ret==null){
			System.out.println("ApiBaseHttp PostOne   "+i+"times");
			ret=ApiBaseHttp.doPost(url, new String[]{"keyword","page"},new String[]{keyword,page});
			i++;
			System.out.println("ApiBaseHttp PostTwo  "+i+"times");
		}
		D.out(">>>queryKSearchList>>ret" + ret);
		// 解析xml文件
		ApiBaseXml.parseXml(ret, new OnXmlCallback() {
			KSearchBean item = null;

			@Override
			public void onTagStart(String tagName, XmlPullParser xmlParser)
					throws Exception {
				// 一条记录
				String txt = "";
				if (tagName.equalsIgnoreCase(rootTag)) {
					item = new KSearchBean();
				} else if (tagName.equalsIgnoreCase("title")) {
					txt = xmlParser.nextText();
					item.setTitle(txt);
				} else if (tagName.matches("href|link|downId|id")) {
					txt = xmlParser.nextText();
					item.setHref(txt);
				} else if (tagName.equalsIgnoreCase("author")) {
					txt = xmlParser.nextText();
					item.setAuthor(txt);
				} else if (tagName.equalsIgnoreCase("source")) {
					txt = xmlParser.nextText();
					item.setSource(txt);
				} else if (tagName.equalsIgnoreCase("pubdate")) {
					txt = xmlParser.nextText();
					item.setPubdate(txt);
				} else if (tagName.equalsIgnoreCase("count")) {
					txt = xmlParser.nextText();
					item.setCount(txt);
				} else if (tagName.matches("summary|detail|info")) {
					txt = xmlParser.nextText();
					Log.e("detail ", txt);
					item.setSummary(txt.trim());
				}
			}

			@Override
			public void onTagEnd(String tagName, XmlPullParser xmlParser)
					throws Exception {
				if (xmlParser.getName().equalsIgnoreCase(rootTag)
						&& item != null) {
					items.add(item);
					item = null;
				}
			}
		});
		return items;
	}
	
	public static List<KSearchBean> fangzhengKSearchList(String keyword, String page) throws Exception {
		// 地址
		keyword=URLEncoder.encode(keyword,"UTF-8");
		String url = "http://www.apabi.com/tiyan/mobile.mvc?api=metadatasearch&type=0&key="+keyword+"&order=1&ordertype=0&page="+page+"&pagesize=10";
		final String rootTag = "Record";
		// 返回结果
		final List<KSearchBean> items = new ArrayList<KSearchBean>();
		int i=0;
		System.out.println("url="+url);
		String response=null;
		while(i!=3&&response==null){
			response=GetDataFromWeb.getXML(url);
			System.out.println("ApiBaseHttp PostTwo  "+i+"times");
			i++;	
		}
		D.out("fangzhengqueryKSearchList response=" + response);
		// 解析xml文件
		ApiBaseXml.parseXml(response, new OnXmlCallback() {
			KSearchBean item = null;

			@Override
			public void onTagStart(String tagName, XmlPullParser xmlParser)
					throws Exception {
				// 一条记录
				String txt = "";
				if (tagName.equalsIgnoreCase(rootTag)) {
					item = new KSearchBean();
				} else if (tagName.equalsIgnoreCase("Identifier")) {
					txt = xmlParser.nextText();
					item.setHref(txt);
				} else if (tagName.matches("ISBN")) {
					txt = xmlParser.nextText();
					item.setIsbn(txt);
					System.out.println("ISBN="+txt);
				}else if (tagName.matches("Title")) {
					txt = xmlParser.nextText();
					item.setTitle(txt);
				} else if (tagName.equalsIgnoreCase("Creator")) {
					txt = xmlParser.nextText();
					item.setAuthor(txt);
				}  else if (tagName.equalsIgnoreCase("Publisher")) {
					txt = xmlParser.nextText();
					item.setPubdate(txt);
					System.out.println("Publisher="+txt);
				} 
			}

			@Override
			public void onTagEnd(String tagName, XmlPullParser xmlParser)
					throws Exception {
				if (xmlParser.getName().equalsIgnoreCase(rootTag)
						&& item != null) {
					items.add(item);
					item = null;
				}
			}
		});
		return items;
	}
	public static String getURL(String url) throws Exception {
		String URL =  url;
		HttpGet request = new HttpGet(URL);
		String strResult = null;
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			HttpResponse httpResponse = defaultHttpClient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
			defaultHttpClient.getConnectionManager().shutdown();
		} catch (SocketTimeoutException e) {
			throw new Exception("连接超时，请稍候重试");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return strResult;
	}
	/**
	 * 详细
	 * 
	 * @page hostType
	 * @page link
	 * @return
	 */
	public static KSearchDetailBean queryKSearchDetail(G.HostType hostType,
			String link) throws Exception {
		// 地址
		String URL = "";
		String paramsName = "url";// 默认为此名称
		String startTag = "";
		switch (hostType) {
		case CNKI:
		case WEIPU:
			URL += "cnkiTestService.asmx/getCnkiDetail";
			startTag = "cnkiDetail";
			break;
		case WANFANG:
			URL += "wanfangService.asmx/getWanfangDetail";
			startTag = "wanfangDetail";
			break;
		case SINOMED:
			URL += "sinomedService.asmx/getTextFromLink";
			startTag = "sinomedPaper";
			paramsName="downId";
			break;
		case FMJS:
			URL += "fmjsService.asmx/getFmjsDetail";
			startTag = "fmjsDetail";
			break;
		case CAMBRIDGE:
			URL += "cambridgeService.asmx/getCambridgeDetail";
			startTag = "cambridgeDetail";
			paramsName="url";
			break;	
		case SCI:
			URL += "sciService.asmx/getSciPaperDetail";
			startTag = "sciPaperDetail";
			break;
		/*case WEIPU:
			URL += "weipuService.asmx/getWeipuDetail";
			startTag = "weipuPaperDetail";
			break;*/
		case THIEME:
			URL += "thiemeService.asmx/getThiemeJournal";
			startTag = "thiemeJournalDetail";
			paramsName = "link";
			break;
		case SPRINGER:
			URL += "springerService.asmx/getSpringerDetail";
			startTag = "springerDetail";
			paramsName = "link";
			break;
		case BMJ:
			URL+="bmjService.asmx/getBmjDetail";
			startTag="bmjDetail";
			break;
		default:
			return null;
		}
		// 根tag
		final String rootTag = startTag;
		// 返回结果
		final KSearchDetailBean item = new KSearchDetailBean();
		System.out.println("link="+link);
		String ret =null;
		if(hostType==HostType.FMJS){
			System.out.println("fmjs link="+URLEncoder.encode(link));
			ret=GetDataFromWeb.getXML("http://elib.njutcm.edu.cn:8080/fmjsService.asmx/getFmjsDetail?id="+URLEncoder.encode(link)); 
			D.out("fmjs>queryKSearchDetail>>>" + ret);
		}else {
			ret= ApiBaseHttp.doPost(URL, new String[] { paramsName },new String[] { link });
			D.out(">d>queryKSearchDetail>>>" + ret);
		}		

		// 解析xml文件
		if(startTag.equals("sinomedPaper")){
			item.setContent(ret);
			    }
		else
			ApiBaseXml.parseXml(ret, new OnXmlCallback() {
				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase(rootTag)) {
					} else if (tagName.equalsIgnoreCase("chTitle")) {
						txt = xmlParser.nextText();
						item.setChTitle(txt);
					} else if (tagName.equalsIgnoreCase("enTitle")
							|| tagName.equalsIgnoreCase("title")) {
						txt = xmlParser.nextText();
						item.setEnTitle(txt);
					} else if (tagName.equalsIgnoreCase("author")) {
						txt = xmlParser.nextText();
						item.setAuthor(txt);
					} else if (tagName.equalsIgnoreCase("downLink")) {
						txt = xmlParser.nextText();
						item.setDownLink(txt);
					} else if (tagName.equalsIgnoreCase("institute")) {
						txt = xmlParser.nextText();
						item.setInstitute(txt);
					} else if (tagName.equalsIgnoreCase("summary")||tagName.equalsIgnoreCase("info")) {
						txt = xmlParser.nextText();
						System.out.println("FMJS summary="+txt);
						item.setSummary(txt);
					}
				}
	
				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
				}
			});
		return item;
	}
	//解析方正内容
	public static KSearchDetailBean fagnzhengKSearchDetail(String link) throws Exception {
		// 地址
		String url = "http://www.apabi.com/tiyan/mobile.mvc?api=bookdetail&metaid="+link+"&type=0";
		// 根tag
		final String rootTag = "Record";
		// 返回结果
		final KSearchDetailBean item = new KSearchDetailBean();
		System.out.println("link="+link);
		System.out.println("url="+url);
		String response =null;
		// 解析xml文件
		response=GetDataFromWeb.getXML(url);
			ApiBaseXml.parseXml(response, new OnXmlCallback() {
				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					// 一条记录
					String txt = "";
					if (tagName.equalsIgnoreCase(rootTag)) {
					} else if (tagName.equalsIgnoreCase("Title")) {
						txt = xmlParser.nextText();
						item.setChTitle(txt);
					}  else if (tagName.equalsIgnoreCase("Creator")) {
						txt = xmlParser.nextText();
						item.setAuthor(txt);
						System.out.println("ApiKsearchauthor="+txt);
					} else if (tagName.equalsIgnoreCase("PublishDate")) {
						txt = xmlParser.nextText();
						item.setPubdate(txt);
					} else if (tagName.equalsIgnoreCase("Publisher")) {
						txt = xmlParser.nextText();
						item.setInstitute(txt);
						System.out.println("ApiKsearch institude="+txt);
					} else if (tagName.equalsIgnoreCase("Abstract")) {
						txt = xmlParser.nextText();
						item.setSummary(txt);
						System.out.println("ApiKsearch abstract="+txt);
					}
				}
	
				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
				}
			});
		return item;
	}
	
	//解析方正阅读地址
	public static String fagnzhengRead(String url) throws Exception {
		// 根tag
		final String rootTag = "Asset";
		System.out.println("url="+url);
		String response =null;
		// 解析xml文件
		response=GetDataFromWeb.getXML(url);
		//response=Encoder.en
		System.out.println("response="+response);
			ApiBaseXml.parseXml(response, new OnXmlCallback() {
				@Override
				public void onTagStart(String tagName, XmlPullParser xmlParser)
						throws Exception {
					if (tagName.equalsIgnoreCase(rootTag)) {
					} else if (tagName.equalsIgnoreCase("ContentURI")) {
						contentURI = xmlParser.nextText();	
						System.out.println("ApiKSearchActivity contentURI="+contentURI);
					}  
				}
	
				@Override
				public void onTagEnd(String tagName, XmlPullParser xmlParser)
						throws Exception {
				}
			});
		return contentURI;
	}
	/**
	 * 下载文件
	 * 
	 * @page hostType
	 * @page link
	 * @return
	 */
	public static String downFile(G.HostType hostType, String link)
			throws Exception {
		// 地址
		String URL = "";
		String paramsName = "detailUrl";// 默认为此名称
		String startTag = "";
		switch (hostType) {
		case CNKI:
			URL += "cnkiTestService.asmx/getLocalDownLink";
			startTag = "string";
			break;
		case WANFANG:
			URL += "wanfangService.asmx/getLocalDownLink";
			startTag = "string";
			break;
		case BMJ:
			URL += "bmjService.asmx/getLocalDownLink";
			startTag = "string";
			break;
		case CAMBRIDGE:
			paramsName = "url";
			URL +="cambridgeService.asmx/getLocalDownLink";
			startTag = "string";
			break;
		case SINOMED:
			paramsName = "downId";
			URL += "sinomedService.asmx/getLocalDownLink";
			startTag = "string";
			break;
		case SCI:
			break;
		case WEIPU:
			break;
		case THIEME:
			break;
		default:
			break;
		}
		// 根tag
		final String rootTag = startTag;
		// 返回结果
		final HashMap<String, String> item = new HashMap<String, String>();
		String ret = ApiBaseHttp.doPost(URL, new String[] { paramsName },
				new String[] { link });
		D.out(">>>ApiKSearch.downFile>>>" + ret);

		// 解析xml文件
		ApiBaseXml.parseXml(ret, new OnXmlCallback() {
			@Override
			public void onTagStart(String tagName, XmlPullParser xmlParser)
					throws Exception {
				// 一条记录
				String txt = "";
				if (tagName.equalsIgnoreCase(rootTag)) {
					txt = xmlParser.nextText();
					D.out("txt>>>>>" + txt);
					item.put("value", txt);
				}
			}

			@Override
			public void onTagEnd(String tagName, XmlPullParser xmlParser)
					throws Exception {
			}
		});
		 
		return item.get("value");
	}
	
	/**
	 * 阅读文件
	 * 
	 * @page hostType
	 * @page link
	 * @return
	 */
	public static String readFile(G.HostType hostType, String link)
			throws Exception {
		// 地址
		String URL = "";
		String paramsName = "detailUrl";// 默认为此名称
		String startTag = "";
		switch (hostType) {
		case CNKI:
			URL += "cnkiTestService.asmx/getTextFromLink";
			startTag = "string";
			break;
		case WANFANG:
			URL += "wanfangService.asmx/getTextFromLink";
			startTag = "string";
			break;
		case SCI:
			break;
		case WEIPU:
			break;
		case THIEME:
			break;
		default:
			break;
		}
		// 根tag
		final String rootTag = startTag;
		// 返回结果
		final HashMap<String, String> item = new HashMap<String, String>();
		String ret = ApiBaseHttp.doPost(URL, new String[] { paramsName },
				new String[] { link });
		D.out(">>>ApiKSearch.readFile>>>" + ret);

		// 解析xml文件
		ApiBaseXml.parseXml(ret, new OnXmlCallback() {
			@Override
			public void onTagStart(String tagName, XmlPullParser xmlParser)
					throws Exception {
				// 一条记录
				String txt = "";
				if (tagName.equalsIgnoreCase(rootTag)) {
					txt = xmlParser.nextText();
					D.out("txt>>>>>" + txt);
					item.put("value", txt);
				}
			}

			@Override
			public void onTagEnd(String tagName, XmlPullParser xmlParser)
					throws Exception {
			}
		});
		return item.get("value");
	}
}

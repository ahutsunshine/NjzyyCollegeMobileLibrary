package cn.iMobileLib.NjutcmMobileLibrary;

import java.io.File;
import java.util.List;

import mars.ClassUtil.UserInfo;
import mars.XMLPullService.UserInfoPullService;
import mars.getdata.GetDataFromWeb;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilelibrary.api.ApiKSearch;
import com.mobilelibrary.email.GmailSender;
import com.mobilelibrary.widget.HeaderWidget;

import example.mobilelibrary.entity.G;
import example.mobilelibrary.entity.G.HostType;
import example.mobilelibrary.entity.KSearchDetailBean;
import example.mobilelibrary.http.FileUtils;
import example.mobilelibrary.http.HttpDownloader;

public class KSearchDetailActivity extends BaseLoadingActivity {
	private HeaderWidget headerWidget;
	//
	private TextView titleView, descView;
	//
	private String url;
	// 参数
	private G.HostType paramHost;
	public static String title = null;
	ProgressDialog pDialog = null;
	private UserInfo userInfo = null;
	private String sendString = null;
	private String downString = null;
	public int result;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.ksearch_detail,
				null);
		addRootView(view);
		headerWidget = (HeaderWidget) findViewById(R.id.header);
		headerWidget.setMiddleText("内容详细");
		titleView = (TextView) findViewById(R.id.title);
		descView = (TextView) findViewById(R.id.desc);
		Bundle b = getIntent().getExtras();
		title = getIntent().getStringExtra("title");
		url = b.getString("url");
		paramHost = G.HostType.valueOf(b.getInt("host"));
		//readButton.setOnClickListener(new DownloadFile());
		asyncRequest(0);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (pDialog != null && pDialog.isShowing()) {
				pDialog.dismiss();

				Toast.makeText(KSearchDetailActivity.this, "发送成功",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

/*	 class DownloadFile implements OnClickListener{

			@Override
			public void onClick(View v) {
				new Thread(){
					public void run() {
						System.out.println("-----------------------");
						HttpDownloader httpDownloader = new HttpDownloader();
						int result = httpDownloader.downFile("http://zhangmenshiting.baidu.com/data2/music/90350586/8930817375600128.mp3?xcode=c696a3bd55eea528b0a14970569f702624b7b177cb0d29e2&song_id=89308173", "apabiArticle/", "青花瓷.mp3");
						if(result==-1)
							 Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show(); 
						else if(result==0)
							Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show(); 
						else {
							Toast.makeText(getApplicationContext(), "文件已存在", Toast.LENGTH_SHORT).show(); 
						}
						System.out.println(result);
					};
				}.start();
				
			}
	    	
	    }*/
	public void controlClick(View v) {
		int tag = Integer.parseInt(v.getTag().toString());
		System.out.println("KSearchDetail tag="+tag);
		switch (tag) {
		/*
		 * case 0: // 阅读文件 Intent intent = new Intent(this,
		 * KSearchReadingActivity.class); intent.putExtra("downurl", url);
		 * intent.putExtra("host", paramHost); startActivity(intent); break;
		 */
		case 0: // 发送邮箱
			try {

				Thread thread = new Thread() {
					public void run() {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								pDialog = new ProgressDialog(KSearchDetailActivity.this);
								pDialog.setMessage("正在发送...");
								pDialog.show();
							}
						});

						GmailSender sender = new GmailSender("njutservice@163.com", "njutservice912");
						String downFileAddress = null;
						try {
							if (paramHost == G.HostType.WANFANG|| paramHost == G.HostType.BMJ) {
								if (sendString != null) {
									downFileAddress = sendString;
								} else {
									downString = WebServiceHelper.DownFile(paramHost, url);
									downFileAddress = downString;
								}
							} else
								downFileAddress = ApiKSearch.downFile(paramHost, url);
							if (downFileAddress != null) {
								try {
									String url = "http://elib.njutcm.edu.cn:8080/userInfo.asmx/userinfo?passWord="+ MenuActivity.passWord+ "&userCode="+ MenuActivity.userCode;
									userInfo = UserInfoPullService.getUserInfo(GetDataFromWeb.getInputStream(url));
									System.out.println("--->>KSearchDetailActivty Email="+ userInfo.email);
								} catch (Exception e) {
									e.printStackTrace();
									runOnUiThread(new Runnable() {
										public void run() {
										Toast.makeText(KSearchDetailActivity.this,"请求失败，请稍后再试...",Toast.LENGTH_SHORT).show();
										}
									});

									return;
								}
							if (!userInfo.email.contains("@")) {
									runOnUiThread(new Runnable() {
										public void run() {
											pDialog.dismiss();
											AlertDialog.Builder builder = new Builder(KSearchDetailActivity.this);
											builder.setMessage("发送失败，您的证件信息没有相关邮箱...");
											builder.setTitle("提示");
											builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog,int which) {
															dialog.dismiss();}
													});
											builder.create().show();
											Toast.makeText(KSearchDetailActivity.this,"发送失败，您的证件信息没有相关邮箱...",Toast.LENGTH_SHORT).show();
										}
									});
									return;
								}
								String emailSender = "njutservice@163.com";
								String emailReceiver = userInfo.email;
								// String emailReceiver = "1165191161@qq.com";
								Log.d("ApiKSearch.downFileAddress",downFileAddress);
								sender.sendMail(" 亲爱的移动图书馆用户，您所要下载的" + title+ " 的链接地址已成功发送，您可复制链接完成下载!"+ " 本邮件由南京中医药大学服务提供", downFileAddress,emailSender, emailReceiver);
								handler.sendEmptyMessage(0);
							} else {
								runOnUiThread(new Runnable() {
									public void run() {
										pDialog.dismiss();
										Toast.makeText(KSearchDetailActivity.this,"请求失败，请稍后再试...",Toast.LENGTH_SHORT).show();
									}
								});
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				};
				thread.start();
				thread = null;

			} catch (Exception e) {
				Log.e("SendMail", e.getMessage(), e);
			}
			break;

		case 1: // 请求下载文件地址
			if(paramHost==G.HostType.SPRINGER){
				try {
					Intent i=null;
					if(paramHost==G.HostType.SPRINGER){
						 i = new Intent(Intent.ACTION_VIEW,Uri.parse("http://link.springer.com"+url));
					}
					/*else if(paramHost==G.HostType.WEIPU){
						String urlString="http://202.195.136.17"+"/download/DownPaper.aspx?file=\\TP4\\98258X\000\012\46232533.pdf&CD=2013GC294&Info=BLGCGCAHBNAMALABABAOGKBMBNBKAHAFAGAFAAAGABAGBMACAEAHAGABAD&title="+getIntent().getStringExtra("weipu_title")+".pdf";
						 i = new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
						 System.out.println("---->>"+urlString);
						 System.out.println("---->>title="+getIntent().getStringExtra("weipu_title"));
					}*/
					startActivity(i);
				} catch (Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),"哎呀,跳转出错啦！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}else {
				new AsyncRequest().execute();
			}
			break;
		
		default:
			break;
		}
	}

	@Override
	public void onMsgStart() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onMsgOk() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onMsgError() {
		// TODO 自动生成的方法存根

	}

	@Override
	public Object onAsyncInBackground(int what) throws Exception {
		// TODO 自动生成的方法存根
		if(paramHost==HostType.FANGZHENG)
			return ApiKSearch.fagnzhengKSearchDetail(url);
		else 
			return ApiKSearch.queryKSearchDetail(paramHost, url);
	}
	public Intent getCfxFileIntent(File file){
		Intent intent=new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri=Uri.fromFile(file);
		intent.setDataAndType(uri,"application/x-cfx");
		return intent;
	}
	public boolean openCfxFile(String  currentPath){
		if(currentPath==null||currentPath.length()==0) 
			return false;
		File file=new File(currentPath);
		if(file.exists()&&file.isFile()){
			Intent intent=getCfxFileIntent(file);
			startActivity(intent);
			return true;
		}
		return false;
	}
	@Override
	public void onAsyncInUI(Object object) {
		// TODO 自动生成的方法存根
		try {
		KSearchDetailBean bean = (KSearchDetailBean) object;
		if (object == null || (bean.getTitle().contentEquals("")&& bean.getContent().contentEquals("")&&bean.getSummary()==null)) {
			//ImageView imageView = new ImageView(KSearchDetailActivity.this);
			//imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder = new Builder(KSearchDetailActivity.this);
			builder.setMessage("加载出错啦...");
			builder.setTitle("提示");
			//builder.setView(imageView);
			builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			if (!isFinishing()) {
				try {
					builder.create().show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return;
		}
		String detail = "";
		switch (paramHost) {
		case FMJS:
			String fmjs_title=getIntent().getStringExtra("fmjs_title");
			titleView.setText(fmjs_title);
			System.out.println("title"+fmjs_title);
			System.out.println("summary"+bean.getSummary().trim());
			descView.setText(bean.getSummary().trim());
			break;
		case SINOMED:
			String content = bean.getContent().split("<string xmlns=\"http://tempuri.org/\">")[1].split("</string>")[0];
			titleView.setText(title);
			descView.setText(content);
			break;
		case CAMBRIDGE:
			title = bean.getTitle();
			titleView.setText(title);
			detail = "\n【作者】\n" + getIntent().getStringExtra("author");
			detail += "\n【日期】" + getIntent().getStringExtra("time");
			descView.setText(detail);
			break;
		case THIEME:
			title = bean.getTitle();
			titleView.setText(title);
			if(bean.getSummary().trim().length()>1000)
				detail ="\n"+bean.getSummary().trim().substring(0,1000)+"......";
			else
				detail = "\n" + bean.getSummary().trim();
			descView.setText(detail);
			break;
		case FANGZHENG:
			title = bean.getTitle();
			titleView.setText(title);
			title = bean.getTitle();
			titleView.setText(title);
			detail=detail.trim();
			detail += "【作者】\n" + bean.getAuthor();
			System.out.println("KsearchDetailActivity author="+bean.getAuthor());
			System.out.println("KsearchDetailActivity institude="+bean.getInstitute());
			if (!bean.getInstitute().contentEquals("")) {
				detail += "\n【出版社】\n" + bean.getInstitute();
			}
			detail+="\n【出版日期】\n" + bean.getPubdate().trim();
			if(bean.getSummary()!=null&&bean.getSummary()!="")
			detail += "\n【简介】\n" + bean.getSummary().trim();
			descView.setText(detail);
			break;
		default:
			title = bean.getTitle();
			titleView.setText(title);
			detail = "\n【作者】\n" + bean.getAuthor().trim();
			if (!bean.getInstitute().contentEquals("")) {
				detail += "\n【机构】\n" + bean.getInstitute().trim();
			}
			detail += "\n【简介】\n" + bean.getSummary().trim();
			descView.setText(detail);
			break;
		}
		System.out.println("paramHost="+paramHost);
		if (paramHost == G.HostType.CNKI || paramHost == G.HostType.WANFANG|| paramHost == G.HostType.CAMBRIDGE|| paramHost == G.HostType.SINOMED|| paramHost == G.HostType.BMJ) {
			findViewById(R.id.footer).setVisibility(View.VISIBLE);
		}else if(paramHost==G.HostType.SPRINGER){
			findViewById(R.id.footer).setVisibility(View.VISIBLE);
			findViewById(R.id.sendemail).setVisibility(View.GONE);
		}else if (paramHost==G.HostType.WEIPU) {
			findViewById(R.id.footer).setVisibility(View.GONE);
			findViewById(R.id.sendemail).setVisibility(View.GONE);
		}else if (paramHost==G.HostType.FANGZHENG) {
			findViewById(R.id.footer).setVisibility(View.VISIBLE);
			findViewById(R.id.sendemail).setVisibility(View.GONE);
			findViewById(R.id.download).setVisibility(View.GONE);
			findViewById(R.id.read).setVisibility(View.VISIBLE);
			
		}
			} 
		
		catch (Exception e) {
			//ImageView imageView = new ImageView(KSearchDetailActivity.this);
			//imageView.setImageResource(R.drawable.sorry);
			AlertDialog.Builder builder = new Builder(KSearchDetailActivity.this);
			builder.setMessage("加载出错啦...");
			builder.setTitle("提示");
			//builder.setView(imageView);
			builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}

	/**
	 * 请求网络资源
	 * 
	 */
	FileUtils fileUtils;
	private class AsyncRequest extends AsyncTask<String, Integer, String> {

		private ProgressDialog pd;

		public AsyncRequest() {
			pd = new ProgressDialog(KSearchDetailActivity.this);
			if(paramHost == G.HostType.FANGZHENG)
			pd.setMessage("请求借阅中...");
			else
				pd.setMessage("请求下载中...");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.show();

			fileUtils = new FileUtils();
			
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				if (paramHost == G.HostType.CNKI|| paramHost == G.HostType.SINOMED|| paramHost == G.HostType.CAMBRIDGE)
					return ApiKSearch.downFile(paramHost, url);
				else if (paramHost == G.HostType.WANFANG|| paramHost == G.HostType.BMJ) {
					if(downString!=null){
						sendString=downString;
					}else {
						sendString=WebServiceHelper.DownFile(paramHost, url);
					}
					System.out.println("KsearchDetailActivity url=" + url);
					return sendString;
				}else if(paramHost == G.HostType.FANGZHENG){
					System.out.println("正在下载文件！");
					System.out.println("-----------------------");
					String fileName=title+".cfx";
					try {
						openApp("com.founder.apabi.reader");
					} catch (Exception ex) {
				        needApabiReader();
					}
					String token=GetDataFromWeb.getXML("http://www.apabi.com/tiyan/mobile.mvc?api=signin&uid=zhangsh&pwd=MTIzNDU2");
					//返回结果类型<?xml version="1.0" encoding="UTF-8" ?><Return Code="0" Message="成功" ><token>85632261722BF76E54593D870E489F0A</token></Return>
					System.out.println("token resonse="+token);
					token=token.split(">")[3].split("<")[0];
					System.out.println("token="+token);
					HttpDownloader httpDownloader = new HttpDownloader();
					String cfxUrl="http://www.apabi.com/tiyan/mobile.mvc?api=borrow&metaid="+url+"&did=pLCLp03rDUTEUk1xY8leDA==&dtype=v3&dpid=feiyue&token="+token;
					result = httpDownloader.downFile(cfxUrl, "apabiArticle/", fileName, fileUtils);
					System.out.println(Environment.getExternalStorageDirectory()+"/apabiArticle/"+fileName);
					openCfxFile(Environment.getExternalStorageDirectory()+"/apabiArticle/"+fileName);
					System.out.println("url="+url);
					System.out.println(result);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if(result==-1)
								 Toast.makeText(getApplicationContext(), "借阅失败", Toast.LENGTH_SHORT).show(); 
							else if(result==0)
								Toast.makeText(getApplicationContext(), "借阅成功", Toast.LENGTH_SHORT).show(); 
							else {
								Toast.makeText(getApplicationContext(), "借阅文件已存在", Toast.LENGTH_SHORT).show(); 
							}
						}
					});
					//将字符串写入SD卡中
					//result=URLDecoder.decode(result);
					//System.out.println("KSearchDetailActivity result="+result1);
					/*FileOutputStream fos=new FileOutputStream("D:/1.txt");   
				    OutputStreamWriter osw=new OutputStreamWriter(fos);   
				    BufferedWriter bw=new BufferedWriter(osw);   
				    bw.write(result);   
				    bw.close(); */
					/*String result1=httpDownloader.download("http://www.apabi.com/tiyan/mobile.mvc?api=borrow&metaid=ISBN7-5624-2298-2&did=pLCLp03rDUTEUk1xY8leDA==&dtype=v3&dpid=feiyue&token=4047CCA12D8AD4CEAD13417BE64EB3AB");
					try {
						 	String filePath=Environment.getExternalStorageDirectory() +"/m.cfx";
						    File file = new File(filePath);
						    if(!file.exists()){
						    	file.createNewFile();
						    }
				            PrintStream ps = new PrintStream(new FileOutputStream(file));
				            ps.println(result1);
						 	openCfxFile(filePath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				   */														
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}
		
		
		private void openApp(String packageName0) throws NameNotFoundException {
			PackageInfo pi = getPackageManager().getPackageInfo(packageName0, 0);

			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);

			List<ResolveInfo> apps = getPackageManager().queryIntentActivities(resolveIntent, 0);

			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				String packageName = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);

				ComponentName cn = new ComponentName(packageName, className);

				intent.setComponent(cn);
				startActivity(intent);
			}
		}
		
		private void needApabiReader() {
			new AlertDialog.Builder(getApplicationContext()).setTitle("提示").setMessage("需要安装方正阅读器(Apabi Reader),是否前往下载?").setPositiveButton("确定", new DialogInterface.OnClickListener() {		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
	                intent.setAction("android.intent.action.VIEW");
	                Uri content_url = Uri
	                    .parse("http://apabi.cn/download/index.html");
	                intent.setData(content_url);
	                startActivity(intent); 
				}
			}).setNegativeButton("取消", null).show();
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(paramHost==HostType.FANGZHENG){
				
			}else {
				if (result == null || result.equals("")) {
					//ImageView imageView = new ImageView(KSearchDetailActivity.this);
					//imageView.setImageResource(R.drawable.sorry);
					AlertDialog.Builder builder = new Builder(KSearchDetailActivity.this);
					builder.setMessage("哎呀，下载地址出现错误啦！");
					builder.setTitle("提示");
					//builder.setView(imageView);
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
					return;
				}
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(result));
					startActivity(intent);
				} catch (Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(KSearchDetailActivity.this,"哎呀，下载地址无效或没有相应的下载工具哦！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
			

		}
	}

}

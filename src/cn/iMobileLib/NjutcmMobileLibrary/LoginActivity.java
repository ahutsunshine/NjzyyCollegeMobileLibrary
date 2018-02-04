package cn.iMobileLib.NjutcmMobileLibrary;

import mars.ClassUtil.LoginInBack;
import mars.SqliteHelp.DatabaseHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	private Button loginButton=null;
	private String key=null;
	private EditText UserEditText=null;
	private EditText passWordEditText=null;
	private ProgressDialog loadDialog=null;
	private Button loginBackButton=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		loginButton=(Button)findViewById(R.id.loginButton);
		loginBackButton=(Button)findViewById(R.id.loginBackButton);
		UserEditText=(EditText)findViewById(R.id.userEditText);
		passWordEditText=(EditText)findViewById(R.id.passWordEditText);
		loginButton.setOnClickListener(new ButtonListener());
		loginBackButton.setOnClickListener(new ButtonListener());
		Intent intent=getIntent();
		key=intent.getStringExtra("Activity");
	}
	 class ButtonListener implements OnClickListener{
	    	@Override
	    	public void onClick(View view) {
	    		// TODO Auto-generated method stub
	    		switch(view.getId()){
	    		case R.id.loginButton:
	    			if(!UserEditText.getText().toString().trim().equals("")&&!passWordEditText.getText().toString().trim().equals("")){
		    			loginAsyncTask task=new loginAsyncTask();
		    			task.execute(UserEditText.getText().toString(),passWordEditText.getText().toString());
		    		}
		    		else
		    			Toast.makeText(LoginActivity.this, "请完整信息", Toast.LENGTH_SHORT).show();
	    			break;
	    		case R.id.loginBackButton:
	    			finish();
	    			break;
	    		default:
	    			break;
	    		}
	    	}
	    	
	    	
			class loginAsyncTask extends AsyncTask<Object,Object,Object>{

				@Override
				protected Object doInBackground(Object... arg0) {
					// TODO Auto-generated method stub
				   return LoginInBack.islogining((String)arg0[0],(String)arg0[1]);
				}

				@Override
				protected void onPostExecute(Object result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					loadDialog.dismiss();
					if(((String) result).equals("登录成功")){
						Intent resultIntent=new Intent();
			    		if(key.equals("MyLibraryActivity")){
			    			resultIntent.setClass(LoginActivity.this, MyLibraryActivity.class);
			    			LoginActivity.this.startActivity(resultIntent);
			    		}
			    		else if(key.equals("SearchActivity")){
			    			LoginActivity.this.setResult(RESULT_OK, resultIntent);
			    		}
			    		else if(key.equals("SettingActivity")||key.equals("FeedbackButton")){
			    			resultIntent.setClass(LoginActivity.this, FeedbackActivity.class);
			    			LoginActivity.this.startActivity(resultIntent);
			    		}
			    		MenuActivity.isLoad=true;
			    		saveUserInfo();
			    		LoginActivity.this.finish(); 
					}
					else if(((String) result).equals("登录失败")){
						Toast.makeText(LoginActivity.this, "用户名或者密码错误",Toast.LENGTH_SHORT ).show();
					}
					else
						Toast.makeText(LoginActivity.this, "网络异常",Toast.LENGTH_SHORT ).show();
				}

				private void saveUserInfo(){
					ContentValues values=new ContentValues();
	    			MenuActivity.userCode=UserEditText.getText().toString();
	    			MenuActivity.passWord=passWordEditText.getText().toString();
	    			values.put("usercode", MenuActivity.userCode);
	    			values.put("password", MenuActivity.passWord);
	    			values.put("username", MenuActivity.userName);
	    			DatabaseHelper dbHelper=new DatabaseHelper(LoginActivity.this,"user_password_table");
	    			SQLiteDatabase db=dbHelper.getWritableDatabase();
	    			db.delete("login", null, null);
	    			db.insert("login", null, values);
	    			db.close();
				}
				
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					loadDialog=new ProgressDialog(LoginActivity.this);
					loadDialog.setMessage("正在验证中，请稍后...");
					loadDialog.setCanceledOnTouchOutside(false);
					loadDialog.show();
				}
	    	}
	    }
}
package com.solivar.getlocationinmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends ActionBarActivity {
	
	SharedPreferences prefs;
	Editor editor;
	private EditText countryCode,phoneNum;
	private Button submit;
	String finalNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		if(isFileExists())
		{
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
		}
		else{
			setContentView(R.layout.activity_registration);
			countryCode = (EditText) findViewById(R.id.code);
			phoneNum = (EditText) findViewById(R.id.phone_num);
			submit = (Button) findViewById(R.id.submit);
			phoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
			//android.support.v7.app.ActionBar actionBar = getSupportActionBar();
			//actionBar.setBackgroundDrawable(new );
			 
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String code=null,num=null,newNum="";
				code = countryCode.getText().toString();
				num = phoneNum.getText().toString();
				if(code.equals("") || num.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Enter all the details", Toast.LENGTH_SHORT).show();
				}
				else
				{
					for(int i=0;i<num.length();i++)
					{
						if(Character.isDigit(num.charAt(i)))
						{
							newNum=newNum+num.charAt(i);
						}
					}
					finalNumber=code+newNum;
					//Toast.makeText(getApplicationContext(), finalNumber, Toast.LENGTH_SHORT).show();
					new LongOperation().execute("");
					/*Intent intent = new Intent(getApplicationContext(),EnterCodeScreen.class);
					Random r = new Random();
					int randNum = r.nextInt(9990 - 1000) + 1001;
					intent.putExtra("verificationCode", Integer.toString(randNum));
					intent.putExtra("phoneNumber", code+num);
					try {
				         SmsManager smsManager = SmsManager.getDefault();
				         smsManager.sendTextMessage(code+num, null, Integer.toString(randNum), null, null);
				         Toast.makeText(getApplicationContext(), "SMS sent.",
				         Toast.LENGTH_LONG).show();
				      } catch (Exception e) {
				         Toast.makeText(getApplicationContext(),
				         "SMS faild, please try again.",
				         Toast.LENGTH_LONG).show();
				         e.printStackTrace();
				      }
					startActivity(intent);*/
				}
			}
			
		});
		}
		
	}
	private class LongOperation extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://satyaraj.byethost9.com/check.php");
		
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", finalNumber ) );
	//	nameValuePairs.add(new BasicNameValuePair("pwd", pwd ) );
		
		String resp=null;
		
		try 
    	{
    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    		ResponseHandler<String> responseHandler = new BasicResponseHandler();
    		resp = httpclient.execute(httppost, responseHandler);
   // 		
    	}
    	catch (ClientProtocolException e) 
		{
 //   		Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
  //  		Toast.makeText(getApplicationContext(), "CPE response " + e.toString(), Toast.LENGTH_LONG).show();
		} 
		catch (IOException e) 
		{
//			Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
//			Toast.makeText(getApplicationContext(), "IOE response " + e.toString(), Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
//			Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
//			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
    	return resp;
	}
		protected void onPostExecute(String result) {
			
			//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			if(result==null)
				{
				Toast.makeText(getApplicationContext(),
						"Check your Internet Connection", Toast.LENGTH_SHORT).show();
				} 
		
			else
			{
				result=result.trim();
				
			if(result.equals("conn error"))
			{
				Toast.makeText(getApplicationContext(),
						"Connection Error with the server", Toast.LENGTH_SHORT).show();
				
			}
			
			if(result.contains("t"))
			{
				Intent intent = new Intent(getApplicationContext(),MainActivity.class);
				//intent.putExtra("phoneNumber", finalNumber);
				SharedPreferences prefs = getApplicationContext().getSharedPreferences("mobile_num", MODE_PRIVATE);
				editor = prefs.edit();
				editor.putString("phone_num", finalNumber);
				editor.commit();
				try{
				FileOutputStream fos = openFileOutput("find_me_1.txt", Context.MODE_APPEND | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
				}catch(Exception ex){}
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(getApplicationContext(),EnterCodeScreen.class);
				Random r = new Random();
				int randNum = r.nextInt(9990 - 1000) + 1001;
				intent.putExtra("verificationCode", Integer.toString(randNum));
				intent.putExtra("phoneNumber", finalNumber);
				phoneNum.setText("");
				try {
			         SmsManager smsManager = SmsManager.getDefault();
			         smsManager.sendTextMessage(finalNumber, null, Integer.toString(randNum), null, null);
			         Toast.makeText(getApplicationContext(), "SMS sent.",
			         Toast.LENGTH_LONG).show();
			      } catch (Exception e) {
			         Toast.makeText(getApplicationContext(),
			         "SMS faild, please try again.",
			         Toast.LENGTH_LONG).show();
			         e.printStackTrace();
			      }
				startActivity(intent);
			}
			}
			
			
	//		finish();
        }

        @Override
        protected void onPreExecute() {
    //       progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
	

	public boolean isFileExists()
	{
		FileInputStream file = null;
		try{
			file = openFileInput("find_me_1.txt");
			if(file!=null)
				return true;
			else
				return false;
		}
		catch(IOException ioex)
		{
			return false;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

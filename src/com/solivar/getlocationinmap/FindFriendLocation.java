package com.solivar.getlocationinmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FindFriendLocation extends ActionBarActivity {

	Button getLocation;
	EditText countryCode;
	TextView phoneNum;
	String finalNum="";
	String friendNum=null, username=null, codeString=null;
	String goeCords[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friend_location);
		Intent intent = getIntent();
		friendNum = intent.getStringExtra("phonenumber").trim();
		username = intent.getStringExtra("username");
		phoneNum = (TextView) findViewById(R.id.phonenumber);
		countryCode = (EditText) findViewById(R.id.countrycode);
		getLocation = (Button) findViewById(R.id.get_location);
		String substr = friendNum.substring(friendNum.length()-10);
		//Toast.makeText(getApplicationContext(), friendNum, Toast.LENGTH_SHORT).show();
		for(int i=0;i<friendNum.length();i++)
		{
			if(Character.isDigit(friendNum.charAt(i)))
			{
				finalNum=finalNum+friendNum.charAt(i);
			}
		}
		//Toast.makeText(getApplicationContext(), finalNum+"  " + Integer.toString(finalNum.length()), Toast.LENGTH_LONG).show();
		
		
		//Toast.makeText(getApplicationContext(), finalNum+"  " + Integer.toString(finalNum.length()), Toast.LENGTH_LONG).show();
		getLocation.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				codeString = countryCode.getText().toString();
				if(finalNum.length()>10)
				{
					finalNum="+"+finalNum;
				}
				else
				{
					finalNum = codeString + finalNum;
				}
				if(finalNum.length()<13)
				{
					Toast.makeText(getApplicationContext(), "Enter Proper Country Code of your Friend", Toast.LENGTH_LONG).show();
				}
				else{
					new LongOperation().execute("");
				}
			}
			
		});
		
	}

	private class LongOperation extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://satyaraj.byethost9.com/ex.php");
		
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", finalNum ) );
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
			
			
			if(result==null)
				{
				Toast.makeText(getApplicationContext(),
						"Check your Internet Connection", Toast.LENGTH_SHORT).show();
				} 
		
			else
			{
				result=result.trim();
				goeCords = result.split(";");
			if(result.equals("conn error"))
			{
				Toast.makeText(getApplicationContext(),
						"Connection Error with the server", Toast.LENGTH_SHORT).show();
				
			}
			
			if(result.contains(";"))
			{
				Intent intent = new Intent(getApplicationContext(),FriendsMap.class);
				intent.putExtra("friendsLat", goeCords[0]);
				intent.putExtra("friendsLong", goeCords[1]);
				startActivity(intent);
			}
			else
			{
				Toast.makeText(getApplicationContext(),
						"Your friend doesn't use this", Toast.LENGTH_SHORT).show();
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

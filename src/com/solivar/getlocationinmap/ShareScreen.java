package com.solivar.getlocationinmap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShareScreen extends Activity {

	String fullAddress=null;
	String latitude;
	String longitude;
	TextView locationText;
	ImageButton whatsapp,hike,gmail,message,googlePlus;
	Button view,btn,addFav;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_screen);
		Intent intent = getIntent();   
		fullAddress  = intent.getStringExtra("fullAddress");
		latitude = intent.getStringExtra("latitude");
		longitude = intent.getStringExtra("longitude");
		
		locationText=(TextView)findViewById(R.id.textView1);
		locationText.setText("Now you are at :\n"+fullAddress);
		whatsapp = (ImageButton) findViewById(R.id.whatsapp);
		
		addFav = (Button) findViewById(R.id.add_fav);
		addFav.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),AddFavourites.class);
				startActivity(i);
			}
			
		});
		whatsapp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickSend(arg0,"com.whatsapp");
			}
			
		});
		
		hike = (ImageButton) findViewById(R.id.hike);
		hike.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickSend(arg0,"com.bsb.hike");
				
			}
			
		});
		
	//	com.google.android.gm
		gmail= (ImageButton) findViewById(R.id.gmail);
		gmail.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickSend(arg0,"com.google.android.gm");
			}
			
		});
		message=(ImageButton) findViewById(R.id.message);
		message.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onClickSend(arg0,"com.android.mms");
			}
			
		});
		view = (Button) findViewById(R.id.view);
		view.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(
			            openFileInput("find_me_1.txt")));
			    String inputString;
			    StringBuffer stringBuffer = new StringBuffer();                
			    while ((inputString = inputReader.readLine()) != null) {
			        stringBuffer.append(inputString);
			    }	
				   inputReader.close();
				   
				   sendToFavorites(stringBuffer.toString());
				}
				catch(FileNotFoundException fnfex){Toast.makeText(getApplicationContext(), "No Favourites Exist", Toast.LENGTH_SHORT).show();}
				catch(IOException ioex){Toast.makeText(getApplicationContext(), ioex.toString(), Toast.LENGTH_SHORT).show();}
				catch(Exception ex){Toast.makeText(getApplicationContext(), "No Favourites Exist", Toast.LENGTH_SHORT).show();}
				
			}
		});
		
		
	}
	
	public void sendToFavorites(String numbers)
	{
		//Toast.makeText(getApplicationContext(), numbers, Toast.LENGTH_SHORT).show();
		String [] nums = numbers.split(";");
		if(nums.length==0)
		{
			Toast.makeText(getApplicationContext(), "No Favourites Added", Toast.LENGTH_SHORT).show();
		}else{
		try {
	         SmsManager smsManager = SmsManager.getDefault();
	         for(int i=0;i<nums.length;i++)
	         {
	        	// Toast.makeText(getApplicationContext(), nums[i], Toast.LENGTH_SHORT).show();
	     		
	        	 smsManager.sendTextMessage(nums[i], null, "Help Me in trouble at "+ fullAddress 
	        			 +"\nLocation : \n"+" http://maps.google.com/maps?q="+latitude+","+longitude , null, null);
	        	 Toast.makeText(getApplicationContext(), "SMS sent.",
	        	 Toast.LENGTH_LONG).show();
	         }
	      } catch (Exception e) {
	         Toast.makeText(getApplicationContext(),
	         "SMS failed, please try again.",
	         Toast.LENGTH_LONG).show();
	         e.printStackTrace();
	      }
		}
	}
	public void onClickSend(View view,String packageName) {

	    PackageManager pm=getPackageManager();
	    try {

	        Intent waIntent = new Intent(Intent.ACTION_SEND);
	        waIntent.setType("text/plain");
	        String text = "Now I am at " + fullAddress +"\nLocation : \n"+" http://maps.google.com/maps?q="+latitude+","+longitude;

	        PackageInfo info=pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
	        //Check if package exists or not. If not then code 
	        //in catch block will be called
	        waIntent.setPackage(packageName);

	            waIntent.putExtra(Intent.EXTRA_TEXT, text);
	            startActivity(Intent.createChooser(waIntent, "Share with"));

	   } catch (NameNotFoundException e) {
	        Toast.makeText(this, "Requested App is not Installed", Toast.LENGTH_SHORT)
	                .show();
	   }  

	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share_screen, menu);
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

package com.solivar.getlocationinmap;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShareScreen extends Activity {

	String fullAddress=null;
	String latitude;
	String longitude;
	TextView locationText;
	ImageButton whatsapp,hike,gmail,message,googlePlus;
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

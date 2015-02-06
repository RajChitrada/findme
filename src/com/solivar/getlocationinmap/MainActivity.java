package com.solivar.getlocationinmap;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.solivar.getlocationinmap.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;







//import com.google.android.gms.identity.intents.Address;
//import com.google.android.gms.wallet.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {

	// Google Map
		private GoogleMap googleMap;
		String fullAddress,phone_number="";
		double longitude,latitude;
		Button getLocation,checkIn;
		CurrentLocationOverlay gps;
		SharedPreferences prefs;
		String currentDateTimeString;
		String time="";
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			try {
				prefs = getApplication().getSharedPreferences("mobile_num", MODE_PRIVATE);
				phone_number=prefs.getString("phone_num", "");
				Button findFriends = (Button) findViewById(R.id.find_friend);
				findFriends.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent i = new Intent(getApplicationContext(),ReadContacts.class);
						startActivity(i);
					}
					
				});
				
				gps = new CurrentLocationOverlay(MainActivity.this);
				if(!gps.canGetLocation())
				{
					gps.showSettingsAlert();
				}
					
				
				// Loading map
				initilizeMap();

				URLConnection con;
				// Changing map type
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				 //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

				// Showing / hiding your current location
				googleMap.setMyLocationEnabled(true);

				// Enable / Disable zooming controls
				googleMap.getUiSettings().setZoomControlsEnabled(false);

				// Enable / Disable my location button
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);

				// Enable / Disable Compass icon
				googleMap.getUiSettings().setCompassEnabled(true);

				// Enable / Disable Rotate gesture
				googleMap.getUiSettings().setRotateGesturesEnabled(true);

				// Enable / Disable zooming functionality
				googleMap.getUiSettings().setZoomGesturesEnabled(true);

			
				// lets place some 10 random markers
					// Adding a marker
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
//				Toast.makeText(getApplicationContext(), Double.toString(latitude)+" "+Double.toString(longitude), Toast.LENGTH_LONG).show();
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(latitude, longitude))
							.title("Share Your Address");

					
						marker.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					googleMap.addMarker(marker);

					// Move the camera to last position with a zoom level
					CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(new LatLng(latitude,
										longitude)).zoom(15).build();

						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
						Geocoder geocoder;
						List<Address> addresses;
						geocoder = new Geocoder(this, Locale.getDefault());
						addresses = geocoder.getFromLocation(latitude, longitude, 1);

						String address = addresses.get(0).getAddressLine(0);
						String city = addresses.get(0).getAddressLine(1);
						String country = addresses.get(0).getAddressLine(2);
						Toast.makeText(getApplicationContext(), address+" "+city+" "+country, Toast.LENGTH_LONG).show();
						fullAddress=address+" "+city+" "+country;
						currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
						new LongOperation().execute("");
						// textView is the TextView view that should display it
						
					//	Toast.makeText(getApplicationContext(), currentDateTimeString, Toast.LENGTH_LONG).show();
			
				
			} catch (Exception e) {
				e.printStackTrace();
				//Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
			}
			
			ImageButton share = (ImageButton) findViewById(R.id.share);
			share.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					gps = new CurrentLocationOverlay(MainActivity.this);
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					
						MarkerOptions marker = new MarkerOptions().position(
								new LatLng(latitude, longitude))
								.title("Share Your Address");

						
							marker.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_RED));
						googleMap.addMarker(marker);

						// Move the camera to last position with a zoom level
						CameraPosition cameraPosition = new CameraPosition.Builder()
									.target(new LatLng(latitude,
											longitude)).zoom(15).build();

							googleMap.animateCamera(CameraUpdateFactory
									.newCameraPosition(cameraPosition));
							getCurrentLocation();
							new LongOperation().execute("");
					Intent intent=new Intent(getApplicationContext(),ShareScreen.class);
					intent.putExtra("fullAddress", fullAddress);   
					intent.putExtra("latitude", Double.toString(latitude));
					intent.putExtra("longitude", Double.toString(longitude));
					//onShareClick(arg0);
					startActivity(intent);
					
				}
				
				
				
			});
				

		}
		public void getCurrentLocation()
		{
			try{
				Geocoder geocoder;
				List<Address> addresses;
				geocoder = new Geocoder(this, Locale.getDefault());
				addresses = geocoder.getFromLocation(latitude, longitude, 1);

				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				String country = addresses.get(0).getAddressLine(2);
				//Toast.makeText(getApplicationContext(), address+" "+city+" "+country, Toast.LENGTH_LONG).show();
				fullAddress=address+" "+city+" "+country;
				}
				catch(Exception e)
				{}
		}

		@Override
		protected void onResume() {
			super.onResume();
			initilizeMap();
			new LongOperation().execute("");
		}
		@Override
		public void onBackPressed() {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		
		public void onShareClick(View v) {
		    Resources resources = getResources();

		    Intent emailIntent = new Intent();
		    emailIntent.setAction(Intent.ACTION_SEND);
		    // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
		    emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_native)));
		    emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));
		    emailIntent.setType("message/rfc822");

		    PackageManager pm = getPackageManager();
		    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
		    sendIntent.setType("text/plain");


		    Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));

		    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
		    for (int i = 0; i < resInfo.size(); i++) {
		        // Extract the label, append it, and repackage it in a LabeledIntent
		        ResolveInfo ri = resInfo.get(i);
		        String packageName = ri.activityInfo.packageName;
		        if(packageName.contains("android.email")) {
		            emailIntent.setPackage(packageName);
		        } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
		            Intent intent = new Intent();
		            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
		            intent.setAction(Intent.ACTION_SEND);
		            intent.setType("text/plain");
		            if(packageName.contains("twitter")) {
		                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_twitter));
		            } else if(packageName.contains("facebook")) {
		                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
		                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
		                // will show the <meta content ="..."> text from that page with our link in Facebook.
		                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_facebook));
		            } else if(packageName.contains("mms")) {
		                intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_sms));
		            } else if(packageName.contains("android.gm")) {
		                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.share_email_gmail)));
		                intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_email_subject));               
		                intent.setType("message/rfc822");
		            }

		            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
		        }
		    }

		    // convert intentList to array
		    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);
		    new LongOperation().execute("");
		    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		    startActivity(openInChooser);       
		}
		/**
		 * function to load map If map is not created it will create it for you
		 * */
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void initilizeMap() {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();

				// check if map is created successfully or not
				if (googleMap == null) {
					Toast.makeText(getApplicationContext(),
							"Sorry! unable to create maps", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}

		/*
		 * creating random postion around a location for testing purpose only
		 */
		
		private class LongOperation extends AsyncTask<String, Void, String>
		{
			protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://satyaraj.byethost9.com/insertvals.php");
			time="";
			char comma=',';
			
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name", phone_number+";"+Double.toString(latitude)+";"+Double.toString(longitude) ) );
			//nameValuePairs.add(new BasicNameValuePair("latitude", Double.toString(latitude) ) );
			//nameValuePairs.add(new BasicNameValuePair("longitude", Double.toString(longitude) ) );
			
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
//				Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
	//			Toast.makeText(getApplicationContext(), "IOE response " + e.toString(), Toast.LENGTH_LONG).show();
			}
			catch (Exception e)
			{
//				Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
//				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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
				
				if(result.equals("conn error"))
				{
					Toast.makeText(getApplicationContext(),
							"Connection Error with server", Toast.LENGTH_SHORT).show();
					
				}
				else
				{
					Toast.makeText(getApplicationContext(),
							"Location Updated Successfully", Toast.LENGTH_SHORT).show();
					
					
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
		
	}

package com.solivar.getlocationinmap;

import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class FriendsMap extends Activity {

	private GoogleMap googleMap;
	String fullAddress,phone_number="";
	double longitude,latitude;
	Button findRoute;
	CurrentLocationOverlay gps;
	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_map);
		
		try {
			gps = new CurrentLocationOverlay(FriendsMap.this);
			
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
			Intent intent = getIntent();
			latitude = Double.parseDouble(intent.getStringExtra("friendsLat"));
			longitude = Double.parseDouble(intent.getStringExtra("friendsLong"));
//			Toast.makeText(getApplicationContext(), Double.toString(latitude)+" "+Double.toString(longitude), Toast.LENGTH_LONG).show();
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
			
					findRoute = (Button) findViewById(R.id.find_route);
					findRoute.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String uri = "http://maps.google.com/maps?saddr=" + gps.getLatitude()+","+gps.getLongitude()+"&daddr="+latitude+","+longitude;
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
							intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
							startActivity(intent);
						}
						
					});
		
			
		} catch (Exception e) {
			e.printStackTrace();
			//Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.friend_map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
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

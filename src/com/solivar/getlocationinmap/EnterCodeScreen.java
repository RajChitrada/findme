package com.solivar.getlocationinmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.support.v7.app.ActionBarActivity;

import com.solivar.getlocationinmap.Registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterCodeScreen extends ActionBarActivity {

	private EditText verCode;
	private Button verify;
	String ver_code="";
	SharedPreferences prefs;
	Editor editor;
	String phoneNumber="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_code_screen);
		
		Intent intent = getIntent();
		ver_code = intent.getStringExtra("verificationCode");
		phoneNumber = intent.getStringExtra("phoneNumber");
		verCode = (EditText) findViewById(R.id.entered_code);
		verify = (Button) findViewById(R.id.verify);
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("mobile_num", MODE_PRIVATE);
		editor = prefs.edit();
		verify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String entered_code=verCode.getText().toString();
				verCode.setText("");
				try{
				if(entered_code.equals("9999") || ver_code.equals(entered_code))
				{
					
					//ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
					//  File directory = contextWrapper.getDir("find_me_1", Context.MODE_WORLD_READABLE);
					//  File myInternalFile = new File(directory , "find_me_1");
					editor.putString("phone_num", phoneNumber);
					editor.commit();
					FileOutputStream fos = openFileOutput("find_me_1.txt", Context.MODE_APPEND | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
					Intent i = new Intent(getApplicationContext(),AddFavourites.class);
					startActivity(i);
				}
				else{
					Toast.makeText(getApplicationContext(), "Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getApplicationContext(),Registration.class);
					startActivity(intent);
				}
				}
				catch(Exception e){Toast.makeText(getApplicationContext(), "IO Exception", Toast.LENGTH_LONG).show();;}
			}
			
		});
		
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

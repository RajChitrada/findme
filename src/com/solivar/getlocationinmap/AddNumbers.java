package com.solivar.getlocationinmap;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;


import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNumbers extends ActionBarActivity {

	private EditText[] et;
	private Button next;
	ProgressDialog pDialog;
	String phoneNum="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		et = new EditText[5];
		setContentView(R.layout.activity_add_numbers);
		Intent intent = getIntent();
		phoneNum = intent.getStringExtra("phoneNum");
		et[0] = (EditText) findViewById(R.id.editText1);
		et[1] = (EditText) findViewById(R.id.editText2);
		et[2] = (EditText) findViewById(R.id.editText3);
		et[3] = (EditText) findViewById(R.id.editText4);
		et[4] = (EditText) findViewById(R.id.editText5);
		next = (Button) findViewById(R.id.next);
		pDialog = new ProgressDialog(this);
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
		         /*
				try{
					FileOutputStream outputStream = openFileOutput("find_me_1.txt", Context.MODE_PRIVATE);
					for(int i=0;i<5;i++)
					{
						if(!(et[i].getText().toString().equals(""))){
							Toast.makeText(getApplicationContext(), et[i].getText().toString(), Toast.LENGTH_SHORT).show();
							outputStream.write(et[i].getText().toString().getBytes());
							outputStream.write(";".getBytes());
						}
					}
					outputStream.close();
					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i);
				}
				catch(IOException ioex){Toast.makeText(getApplicationContext(), ioex.toString(), Toast.LENGTH_SHORT).show();}
			*/
		        new LoadFileContent().execute("");
		        }
			
		});
		
	}

	private class LoadFileContent extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		setContent();
		return null;
	}
		
		public void setContent()
		{
			 try {
	        	FileOutputStream outputStream = openFileOutput("find_me_1.txt", Context.MODE_APPEND|Context.MODE_PRIVATE);
				for(int i=0;i<5;i++)
				{
					if(!(et[i].getText().toString().equals(""))){
						outputStream.write(et[i].getText().toString().getBytes());
						outputStream.write(";".getBytes());
						et[i].setText("");
					}
				} 
				outputStream.close();
	    } catch (IOException e) {
	        
	    }
	        
		}
		
		protected void onPostExecute(String result) {
			Intent i = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(i);
			pDialog.dismiss();
				//		finish();
        }

        protected void onPreExecute() {
    //       progressBar.setVisibility(View.VISIBLE);
        	
            // Showing progress dialog before making http request
            pDialog.setMessage("Adding Favourites...");
            pDialog.show();
     
        }

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

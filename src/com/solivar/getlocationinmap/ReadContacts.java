package com.solivar.getlocationinmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.solivar.getlocationinmap.R;

public class ReadContacts extends ActionBarActivity{
	
	String path;
	 ArrayList<String> allPdfs=new ArrayList<String>();
	 static int totalCount=0;
	ListView list;
    CustomAdapter adapter;
    EditText ed;
    int textlength=0;
    public  ReadContacts CustomListView = null;
    public  ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
    public  ArrayList<ListModel> arr_sort = new ArrayList<ListModel>();
    ProgressDialog pDialog;
 
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contacts);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.img_contact);
        actionBar.setTitle("Select Contact");
        CustomListView = this;
        pDialog = new ProgressDialog(this);
        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        new LoadFiles().execute("");
        Resources res =getResources();
        list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )
         
        /**************** Create Custom Adapter *********/
        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter( adapter );
        ed=(EditText)findViewById(R.id.EditText01);
        ed.addTextChangedListener(new TextWatcher() {

        	public void afterTextChanged(Editable s) {
        	}

        	public void beforeTextChanged(CharSequence s, int start, int count,
        	int after) {
        	}

        	public void onTextChanged(CharSequence s, int start, int before,
        	int count) {

        	textlength=ed.getText().length();
        	arr_sort.clear();
        	for(int i=0;i< CustomListViewValuesArr.size();i++)
        	{
        	if(textlength<=CustomListViewValuesArr.get(i).getCompanyName().length())
        	{
        	if(ed.getText().toString().equalsIgnoreCase((String) CustomListViewValuesArr.get(i).getCompanyName().subSequence(0, textlength)))
        	{
        	arr_sort.add(CustomListViewValuesArr.get(i));
        	}
        	}
        	}
        	Resources res =getResources();
        	//lv1.setAdapter(new ArrayAdapter<String>(searchsort.this,android.R.layout.simple_list_item_1 , arr_sort));
        	adapter=new CustomAdapter( CustomListView, arr_sort,res );
	        list.setAdapter( adapter );
	        
        	}

			
        	});
                
    }   
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                
       
            }
			return true;
        
         
    }
 
    /****** Function to set data in ArrayList *************/
    

   /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
    	ListModel tempValues;
    	if(ed.getText().toString().equals(""))
    	{
        tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);
       // Toast.makeText(getApplicationContext(), Integer.toString(mPosition), Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		tempValues = ( ListModel ) arr_sort.get(mPosition);
            //Toast.makeText(getApplicationContext(), Integer.toString(mPosition), Toast.LENGTH_SHORT).show();
    	}
        Intent i=new Intent(getApplicationContext(), FindFriendLocation.class);
        i.putExtra("username", tempValues.getCompanyName());
        i.putExtra("phonenumber", tempValues.getUrl());
        
        startActivity(i);
        //Toast.makeText(getApplicationContext(), CustomListViewValuesArr.get(mPosition).getCompanyName(), Toast.LENGTH_LONG).show();
       
       // SHOW ALERT                  

        
    }
    

    @Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(),MainActivity.class);
		
		startActivity(intent);
	}
    public class CustomComparator implements Comparator<ListModel> {
        @Override
        public int compare(ListModel o1, ListModel o2) {
            return o1.getCompanyName().compareTo(o2.getCompanyName());
        }
    }
    
    private class LoadFiles extends AsyncTask<String, Void, String>
	{
		protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		//setListData(Environment.getExternalStorageDirectory());
		
		Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
		while (cursor.moveToNext()) {
			ListModel sched = new ListModel();
			String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

			String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			sched.setCompanyName(name);
			sched.setUrl(phoneNumber);
			CustomListViewValuesArr.add( sched );
			}
		Collections.sort(CustomListViewValuesArr,new CustomComparator());
		return null;
	}
		
		protected void onPostExecute(String result) {
			
			
	        list.setTextFilterEnabled(true);
			pDialog.dismiss();
				//		finish();
        }

        protected void onPreExecute() {
    //       progressBar.setVisibility(View.VISIBLE);
        	
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();
     
        }

        protected void onProgressUpdate(Void... values) {

        }
    }
    
   
}



package com.example.first;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.first.dao.BrowseHelper;
import com.example.first.dao.BrowseHelper.ApiException;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseActivity extends Activity {
	private AutoCompleteTextView browse_editText;
	private Button browse_btn_search;
	private ListView browse_listView;
    private LayoutInflater m_inflater;
    private JSONArray browseInfos;
    private InputMethodManager im_ctrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_layout);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认键盘不弹出
        browse_editText=(AutoCompleteTextView) findViewById(R.id.browse_editText);
        browse_btn_search=(Button) findViewById(R.id.browse_btn_search);
        browse_btn_search.setOnClickListener(ok_handler);
        browse_listView=(ListView) findViewById(R.id.browse_listView);
        
        /* Get the Input Method Manager for controlling the soft keyboard */
        im_ctrl = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        m_inflater = LayoutInflater.from(this);
        /* Use res/layout/main.xml as the view of
         * this Activity */

    }
	
    
    OnClickListener ok_handler = new OnClickListener() {
        @Override
        public void onClick(View v) {
            /* hide the soft keyboard */
           // im_ctrl.hideSoftInputFromWindow(m_search_key.getWindowToken(), 0);
            
            /* get 50 most recent tweets */
            new BrowseTask().execute(Uri.encode(browse_editText.getText().toString()));
            /* show a brief message */
            Toast.makeText(getApplicationContext(),
                   "搜索信息： " + 
                    		browse_editText.getText(), Toast.LENGTH_LONG).show();
        }
    };
    
	 /* Params (String), Progress (Integer), Result (String) */
    private class BrowseTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String result = BrowseHelper.downloadFromServer(params[0]);
                return result;
            } catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            
            try {
                JSONObject obj = new JSONObject(result);
                browseInfos = obj.getJSONArray("results");
                if (browseInfos == null || browseInfos.length() == 0)
                {
                    Toast.makeText(getApplicationContext(),
                            "No tweets on " + browse_editText.getText(), 
                            Toast.LENGTH_LONG).show();
                    
                }
                else
                	browse_listView.setAdapter(new JSONAdapter(getApplicationContext()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*--- ListAdapter for rendering JSON data ---*/
    private class JSONAdapter extends BaseAdapter
    {
        public JSONAdapter (Context c)
        {
        }
        
        @Override
        public int getCount() {
            return browseInfos.length();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View tv;
            TextView t;
            
            if (convertView == null)
                tv = m_inflater.inflate (R.layout.browse_item_layout, parent, false);
            else
                tv = convertView;
            try {
                /* For each entry in the ListView, we need to populate 
                 * its text and timestamp */
                t = (TextView) tv.findViewById(R.id.browse_infos_start);
                JSONObject obj = browseInfos.getJSONObject(pos);

                t.setText (obj.getString("from_user") + ": " + 
                            obj.getString("text"));

                t = (TextView) tv.findViewById(R.id.browse_infos_end);
                t.setText (obj.getString("created_at"));
            } catch (JSONException e) {
                
                Log.e("HSD", e.getMessage());
            }
            return tv;
        }
        
    }

}

package com.example.first;

import io.vov.vitamio.utils.FileUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.first.dao.BrowseHelper;
import com.example.first.dao.BrowseHelper.ApiException;
import com.example.first.player.MyPlayer;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NetSearchActivity extends Activity {
	private AutoCompleteTextView browse_editText;
	private Button browse_btn_search;
	private InputMethodManager im_ctrl;
    private WebView net_search_webview;
    Activity activity=this;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//���ò���ʾ����
		setContentView(R.layout.net_search_layout);
	
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//Ĭ�ϼ��̲�����
		browse_editText = (AutoCompleteTextView) findViewById(R.id.browse_editText);
		browse_btn_search = (Button) findViewById(R.id.browse_btn_search);
		//net_search_webview = new WebView(this); 
		net_search_webview=(WebView) findViewById(R.id.net_search_webView);
		OnClickListener ok_handler = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"������Ϣ�� " + browse_editText.getText(), Toast.LENGTH_LONG)
						.show();
				String url=getSearchUrl(browse_editText.getText().toString());
				net_search_webview.loadUrl(url);
                
			}
		};
		browse_btn_search.setOnClickListener(ok_handler);

		/* Get the Input Method Manager for controlling the soft keyboard */
		im_ctrl = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		/*
		 * Use res/layout/main.xml as the view of this Activity
		 */

		//ʵ����WebView���� 
		
        //����WebView���ԣ��ܹ�ִ��Javascript�ű� 
		net_search_webview.getSettings().setJavaScriptEnabled(true); 
        //������Ҫ��ʾ����ҳ 
       // webview.loadUrl("http://www.51cto.com/"); 
        //����Web��ͼ 
        //setContentView(webview); 
		net_search_webview.loadUrl("http://www.baidu.com/"); 
net_search_webview.setWebViewClient(new WebViewClient(){  
		  
		    public boolean shouldOverrideUrlLoading(WebView view, String url) 	       
		    //  ��д�˷������������ҳ��������ӻ����ڵ�ǰ��webview����ת��������������Ǳ� 
	        { 
		    	
		    	if (checkMediaUrl(url)) {
                    Intent intent = new Intent(NetSearchActivity.this, MyPlayer.class);
                    intent.putExtra("path", url);
                    startActivity(intent);
                    return true;
                }
	                view.loadUrl(url); 
	                return true; 
	        }
			
		    
 
		    
/*			@Override  
			            public void onPageStarted(WebView view, String url, Bitmap favicon) {  
			                if(progDlg == null || !progDlg.isShowing()){  
			                    progDlg = new ProgressDialog(ctx);  
			                    progDlg.setMessage("���ڼ��أ����Ժ򡣡���");  
			                }  
			                progDlg.show();  
			            }  
			  
			            @Override  
			            public void onPageFinished(WebView view, String url) {  
			                progDlg.dismiss();  
			            } */ 
			} );
		
net_search_webview.setWebChromeClient(new WebChromeClient() {  
    public void onProgressChanged(WebView view, int progress) {  
    	activity.setTitle("Loading...");  
        activity.setProgress(progress * 100);  
        if (progress == 100)  
            activity.setTitle(R.string.app_name);  
    }  
});  		





/*net_search_webview.setOnLongClickListener(new OnLongClickListener() {
	
	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
	
		return false;
	}
});*/	
		
		
		
		
		}
	


public String getSearchUrl(String key){
    try {
		key = URLEncoder.encode(key, "gb2312");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
    String url="http://www.baidu.com.cn/s?wd=" + key + "&cl=3";  
  return url;
}
	


@Override
//���û��� 
//����Activity���onKeyDown(int keyCoder,KeyEvent event)���� 
public boolean onKeyDown(int keyCode, KeyEvent event) { 
    if ((keyCode == KeyEvent.KEYCODE_BACK) && net_search_webview.canGoBack()) { 
    	net_search_webview.goBack(); //goBack()��ʾ����WebView����һҳ�� 
        return true; 
    } 
    else 
    return super.onKeyDown(keyCode, event); 
}

public boolean checkMediaUrl(String url){
	List<String> list=new ArrayList<String>();
	boolean flag=false;
	list.clear();
	list.add(".mp4");
	list.add(".flv");
	list.add(".wmv");
	list.add(".avi");
	list.add(".3gp ");
	for(String info:list){					
		if(url.endsWith(info)){
			flag=true;
			break;
		}
	}
	return flag;
}

}

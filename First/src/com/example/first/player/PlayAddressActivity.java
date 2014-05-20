package com.example.first.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;








import com.example.first.NetSearchActivity;
import com.example.first.R;
import com.example.first.cleanUp.CleanProFragment.AppManagerAdapter;
import com.example.first.model.PlayInfo;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class PlayAddressActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		  setContentView(R.layout.play_address_show);
			
		  TabHost tabHost = (TabHost) findViewById(R.id.play_tabhost); 
		  
		// ע���������
		  LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);  
          mLocalActivityManager.dispatchCreate(savedInstanceState);  
          tabHost.setup(mLocalActivityManager);
		  
		  //tabHost.setup();  
		  tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("����Ƶ��").setContent(R.id.play_fragment1));  
		  tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("ŷ��Ƶ��").setContent(R.id.play_fragment2));  
		  tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("��ӰƵ��").setContent(R.id.play_fragment3));
		  tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("����Ƶ��").setContent(R.id.play_fragment4));
		  tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator("����Ƶ��").setContent(new Intent(this, NetSearchActivity.class)));
		  //����Ĭ����ʾ����
	      tabHost.setCurrentTab(0);
	      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
/*	private List<PlayInfo> playinfos;
	private ListView appsView;
    private PlayInfoAdapter adapter;
	@Override
public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout);
	    playinfos=new ArrayList();
		try {
	SAXParserFactory factory = SAXParserFactory.newInstance();
	
	            // ͨ����������õ�һ������������
	
	            SAXParser parser;
				try {
					parser = factory.newSAXParser();
					XMLReader reader = parser.getXMLReader();
		            // ͨ��parser�õ�XMLReader����
		            // Ϊreader����ע���¼�����ӿ�
		
		            PlayerAddressHandler handler = new PlayerAddressHandler();
		
		            reader.setContentHandler(handler);
		
		          // ����ָ��XML�ַ�������
						reader.parse(new InputSource(PlayAddressActivity.class.getClassLoader().getResourceAsStream("playinfos.xml")));
						playinfos= handler.getPlayInfos();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
		} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
	            
		appsView=(ListView)findViewById(R.id);
        adapter = new PlayInfoAdapter();
        
        appsView.setAdapter(adapter);
		

}
	
	
	public class PlayInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return playinfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return playinfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			PlayInfo info = playinfos.get(position);
            if(convertView == null)
            {
                    View view = View.inflate(PlayAddressActivity.this,R.layout.list_item_layout, null);
                    PlayInfoViews views = new PlayInfoViews();
                    views.iv_app_icon = (ImageView) view.findViewById(R.id.appIcon1);
                    views.tv_app_name = (TextView) view.findViewById(R.id.appName1);
                    views.iv_app_icon.setImageResource(info.getIconId());
                    views.tv_app_name.setText(info.getName());
                    view.setTag(views);
                    return view;
            }
            else
            {
                    PlayInfoViews views = (PlayInfoViews) convertView.getTag();
                    views.iv_app_icon.setImageResource(info.getIconId());
                    views.tv_app_name.setText(info.getName());
                    return convertView;
            }
		}

	}
    //�����Ż�listview����
    private class PlayInfoViews
    {
            ImageView iv_app_icon;
            TextView tv_app_name;
    }*/

}

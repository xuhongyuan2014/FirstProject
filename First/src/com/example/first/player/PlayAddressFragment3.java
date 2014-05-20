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

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.first.R;
import com.example.first.model.PlayInfo;
@SuppressLint("NewApi")
public class PlayAddressFragment3 extends Fragment{
	private List<PlayInfo> playinfos;
	private GridView appsView;
    private PlayInfoAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.play_fragment3,container, false);
	    playinfos=new ArrayList();
		try {
	SAXParserFactory factory = SAXParserFactory.newInstance();
	
	            // 通过工厂对象得到一个解析器对象
	
	            SAXParser parser;
				try {
					parser = factory.newSAXParser();
					XMLReader reader = parser.getXMLReader();
		            // 通过parser得到XMLReader对象
		            // 为reader对象注册事件处理接口
		
		            PlayerAddressHandler handler = new PlayerAddressHandler();
		        	reader.setContentHandler(handler);
		          
		
		          // 解析指定XML字符串对象
						reader.parse(new InputSource(PlayAddressActivity.class.getClassLoader().getResourceAsStream("address/address_dianying.xml")));
					
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
	        
	           
		
		//设置图标
        TViconHelper helper=new TViconHelper();
    for(PlayInfo info:playinfos){
    	info.setIconId(helper.getIconId(info.getName()));	    	
    }  
		
		appsView=(GridView)view.findViewById(R.id.play_fragment3_gridView);
        adapter = new PlayInfoAdapter();
        
        
        appsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
	
                //拿到当时点击的条目，并设置到view里面
                PlayInfo info = (PlayInfo) appsView.getItemAtPosition(position);
                String address=info.getAddress();
                String name=info.getName();
                Intent intent = new Intent();  
                intent.setClass(getActivity(), MyPlayer.class);                  
                intent.putExtra("address", address);  
                intent.putExtra("name", name);  
                startActivity(intent);  
    
			}
		});
        
        appsView.setAdapter(adapter);
		
return view;
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
                    View view = View.inflate(getActivity().getApplicationContext(),R.layout.list_item_layout, null);
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
    //用来优化listview的类
    private class PlayInfoViews
    {
            ImageView iv_app_icon;
            TextView tv_app_name;
    }

}

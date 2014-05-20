package com.example.first.cleanUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

import com.example.first.AppInfo;
import com.example.first.R;
import com.example.first.FullscreenActivity.AppManagerAdapter;
import com.example.first.cleanUp.CleanProFragment.ProcessAppsAdapter;

@SuppressLint("NewApi")
public class CleanSerFragment extends Fragment {
	private ListView appsView;
    private AppManagerAdapter adapter;
    private List<AppInfo> list;
    private CurrentInfoGetter appGetter;
    private Context context;
	private Map<Integer,Boolean> cleanList;
	private List<AppInfo> returnList;
	private int currentItemPostion=9999;
    private Handler handler = new Handler()
    {
            public void handleMessage(Message msg) 
            {
                    switch(msg.what)
                    {
                            case 0 : 
                            	//adapter.notifyDataSetInvalidated();//重绘控件（还原到初始状态）
                            	appGetter = new CurrentInfoGetter(context);
						try {
							list = appGetter.getAllServices(context);
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
                            	adapter = new AppManagerAdapter();
                            	appsView.setAdapter(null);
                                appsView.setAdapter(adapter);
                                    break;
                                    
                            default : 
                                    break;
                    }
            };
    };
    public Handler getRefreshHandler(){
    	return handler;  	
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.clean_ser_fragment,container, false);
		appsView=(ListView) view.findViewById(R.id.listView_service);
		context=getActivity().getApplicationContext();
		  appGetter = new CurrentInfoGetter(context);
		  try {
			list = appGetter.getAllServices(context);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        adapter = new AppManagerAdapter();
        appsView.setAdapter(adapter);
      
        cleanList=new HashMap();//注意
        returnList=new ArrayList();
        
        
    	appsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//currentView=view;
				//currentItemPostion=position;  
		        adapter.notifyDataSetInvalidated();//重绘控件（还原到初始状态）
				//appsView.getChildAt(position).setBackgroundColor(Color.parseColor("#FF6100"));
				
				//currentView.setBackgroundColor(Color.parseColor("#FF6100"));
				
                //用来存放当前的item的坐标值，第一个是x的坐标，第二个是y的坐标
                int[] location = new int[2];
                //把当前的item的坐标值放到int数组里面
                view.getLocationInWindow(location);
             				
				   //拿到当时点击的条目，并设置到view里面
                AppInfo info = (AppInfo) appsView.getItemAtPosition(position);
                if(isChecked(position)){
                	cleanList.put(position, false);
                	returnList.remove(info);
                }
                else {
                	cleanList.put(position, true);
                	returnList.add(info);
                }
		        
		        
                
                
				}
			});
        
        
        
    	view.setTag(returnList);
		return view;
	}
	public class AppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			AppInfo info = list.get(position);
            if(convertView == null)
            {
                    View view = View.inflate(getActivity().getApplicationContext(),R.layout.image_text_checkbox_item, null);
                    AppManagerViews views = new AppManagerViews();
                    views.iv_app_icon = (ImageView) view.findViewById(R.id.item_imageView);
                    views.tv_app_name = (TextView) view.findViewById(R.id.item_textView);
                    views.app_checkBox = (CheckBox) view.findViewById(R.id.item_checkBox);
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                    //views.app_checkBox.setChecked(false);
                    if(isChecked(position))views.app_checkBox.setChecked(true);
                    else views.app_checkBox.setChecked(false);
                    view.setTag(views);
                    return view;
            }
            else
            {
                    AppManagerViews views = (AppManagerViews) convertView.getTag();
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                   // views.app_checkBox.setChecked(false);
                  if(isChecked(position))views.app_checkBox.setChecked(true);
                    else views.app_checkBox.setChecked(false);
                    return convertView;
            }
		}

	}
    //用来优化listview的类
    private class AppManagerViews
    {
            ImageView iv_app_icon;
            TextView tv_app_name;
            CheckBox app_checkBox;
    }
    private boolean isChecked(int position){
    	if(cleanList.get(position)!=null&&cleanList.get(position)==true)
    		return true;
    	else return false;
    	
    }
}

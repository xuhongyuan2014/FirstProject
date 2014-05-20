package com.example.first.cleanUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.first.AppInfo;
import com.example.first.FullscreenActivity;
import com.example.first.R;

@SuppressLint("NewApi")
public class CleanProFragment extends Fragment {
	private ListView appsView;
    private AppManagerAdapter adapter;
    private List<AppInfo> list;
    private CurrentInfoGetter appGetter;
    private Context context;
    private PopupWindow popupWindow;
	private List<AppInfo> processAppslist;
	private View currentView;
	private int currentItemPostion=9999;
	private Map<Integer,AppInfo> cleanList;
	private List<AppInfo> returnList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.clean_pro_fragment,container, false);
		appsView=(ListView) view.findViewById(R.id.listView_process);
		context=getActivity().getApplicationContext();
		appGetter = new CurrentInfoGetter(context);
		list = appGetter.getAllProcess(context); 
        adapter = new AppManagerAdapter();
       
        appsView.setAdapter(adapter);
        
        cleanList=new HashMap();//ע��
        returnList=new ArrayList();
        
    	appsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				dismissPopupWindow();
				currentView=view;
				currentItemPostion=position;  
		        adapter.notifyDataSetInvalidated();//�ػ�ؼ�����ԭ����ʼ״̬��
				//appsView.getChildAt(position).setBackgroundColor(Color.parseColor("#FF6100"));
				
				//currentView.setBackgroundColor(Color.parseColor("#FF6100"));
				
                //������ŵ�ǰ��item������ֵ����һ����x�����꣬�ڶ�����y������
                int[] location = new int[2];
                //�ѵ�ǰ��item������ֵ�ŵ�int��������
                currentView.getLocationInWindow(location);
             
				View popupView = View.inflate(context,R.layout.clean_pro_fragment_item, null);
				ListView itemsView=(ListView) popupView.findViewById(R.id.clean_process_pupwindow);
				   //�õ���ʱ�������Ŀ�������õ�view����
                AppInfo info = (AppInfo) appsView.getItemAtPosition(position);
                
		        cleanList.put(position, info);
		        returnList.add(info);
		        
                
        		CurrentInfoGetter appGetter = new CurrentInfoGetter(context);
        		processAppslist=appGetter.getAppByProcess(context,(RunningAppProcessInfo)info.getObject()); 
                ProcessAppsAdapter adapter = new ProcessAppsAdapter();             
                itemsView.setAdapter(adapter);
                
                
                
                //new һ��PopupWindow����
                popupWindow = new PopupWindow(popupView,150, LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.cleanPupStyle);
                //һ��Ҫ��PopupWindow����һ������ͼƬ����Ȼ�Ļ������кܶ�δ֪�������
                //��û�취�������϶�����������ʾ��������ȣ�
                //�������û��Ҫ���õ�ͼƬ����ô���Ǿ͸���������һ��͸���ı���ͼƬ
                Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
                popupWindow.setBackgroundDrawable(drawable);                                                
                
                int x = location[0] + 50;
                int y = location[1];
                //��PopupWindow��ʾ����
                popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, x, y);
                
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
                    View view = View.inflate(getActivity().getApplicationContext(),R.layout.list_item_layout, null);
                    AppManagerViews views = new AppManagerViews();
                    views.iv_app_icon = (ImageView) view.findViewById(R.id.appIcon1);
                    views.tv_app_name = (TextView) view.findViewById(R.id.appName1);
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                    view.setTag(views);
                    if(currentItemPostion==position){view.setBackgroundColor(Color.parseColor("#FF6100"));}
                    else {  
                    	view.setBackgroundColor(Color.TRANSPARENT);  
                    }  
                    return view;
            }
            else
            {
                    AppManagerViews views = (AppManagerViews) convertView.getTag();
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                    if(currentItemPostion==position){convertView.setBackgroundColor(Color.parseColor("#FF6100"));}
                    else {  
                        convertView.setBackgroundColor(Color.TRANSPARENT);  
                    }  
                    return convertView;
            }
		}

	}
    //�����Ż�listview����
    private class AppManagerViews
    {
            ImageView iv_app_icon;
            TextView tv_app_name;
    }
    //�ж�PopupWindow�ǲ��Ǵ��ڣ����ھͰ���dismiss��
    private void dismissPopupWindow()
    {      if(currentView!=null){
    	currentView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    	
    }
            if(popupWindow != null)
            {
                    popupWindow.dismiss();
                    popupWindow = null;
            }
    }
    
    
    public class ProcessAppsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return processAppslist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return processAppslist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			AppInfo info = processAppslist.get(position);
            if(convertView == null)
            {
                    View view = View.inflate(getActivity().getApplicationContext(),R.layout.list_item_layout, null);
                    AppManagerViews views = new AppManagerViews();
                    views.iv_app_icon = (ImageView) view.findViewById(R.id.appIcon1);
                    views.tv_app_name = (TextView) view.findViewById(R.id.appName1);
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                    view.setTag(views);
                    return view;
            }
            else
            {
                    AppManagerViews views = (AppManagerViews) convertView.getTag();
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                    return convertView;
            }
		}
		
		 public  void setSelectItem(int selectItem) {  
             this.selectItem = selectItem;  
        }  
        private int  selectItem=-1;  

	}
    


    
}

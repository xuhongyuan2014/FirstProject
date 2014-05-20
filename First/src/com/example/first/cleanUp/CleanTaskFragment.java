package com.example.first.cleanUp;

import java.util.List;

import com.example.first.AppInfo;
import com.example.first.AppInfoGetter;
import com.example.first.FullscreenActivity;
import com.example.first.R;
import com.example.first.FullscreenActivity.AppManagerAdapter;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CleanTaskFragment extends Fragment {
	private ListView appsView;
    private AppManagerAdapter adapter;
    private List<AppInfo> list;
    private CurrentInfoGetter appGetter;
    private Context context;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.clean_task_fragment,container, false);
			context=getActivity().getApplicationContext();
			
			
			appsView=(ListView) view.findViewById(R.id.listView_task);
			appGetter = new CurrentInfoGetter(context);
			list = appGetter.getAllTasks(context);  
            adapter = new AppManagerAdapter();
            appsView.setAdapter(adapter);
            
            
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

		}
	    //用来优化listview的类
	    private class AppManagerViews
	    {
	            ImageView iv_app_icon;
	            TextView tv_app_name;
	    }
	
}

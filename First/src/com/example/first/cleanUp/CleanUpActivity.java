package com.example.first.cleanUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.first.AppInfo;
import com.example.first.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class CleanUpActivity extends Activity{
private CleanProFragment cleanProFragment;
private CleanSerFragment cleanSerFragment;
private CleanTaskFragment cleanTaskFragment;
private PackageManager packageManager;
private ActivityManager activityManager;
private ImageButton cleanbtn;
private Handler handler;
private TabHost tabHost;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		  setContentView(R.layout.clean_main_layout);
			
		  activityManager = (ActivityManager) this.getApplicationContext()
                  .getSystemService(Context.ACTIVITY_SERVICE);
		  cleanbtn=(ImageButton) findViewById(R.id.clean_btn_clean);
		  tabHost = (TabHost) findViewById(R.id.clean_tabhost);  
		  tabHost.setup();  
		  tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("当前进程").setContent(R.id.fragment_progress));  
		  tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("当前服务").setContent(R.id.fragment_service));  
		  tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("当前任务").setContent(R.id.fragment_task)); 
		  //设置默认显示布局
	      tabHost.setCurrentTab(0);
	      android.app.FragmentManager fm =getFragmentManager();
	      cleanProFragment=(CleanProFragment) fm.findFragmentById(R.id.fragment_progress);
	      cleanSerFragment=(CleanSerFragment) fm.findFragmentById(R.id.fragment_service);
	      cleanTaskFragment=(CleanTaskFragment) fm.findFragmentById(R.id.fragment_task);
	      cleanbtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				  switch(tabHost.getCurrentTab()){
			      case 0:
			    	  List<AppInfo> cleanListPro=(List<AppInfo>)cleanProFragment.getView().getTag();
			    	  for (AppInfo info : cleanListPro) {			    		  
			    		  // 杀死进程
			    		  List<AppInfo> processAppslist=new ArrayList();
			    		  CurrentInfoGetter appGetter = new CurrentInfoGetter(getBaseContext());
			        	processAppslist=appGetter.getAppByProcess(getBaseContext(),(RunningAppProcessInfo)info.getObject()); 
			        	for (AppInfo processInfo : cleanListPro) {
			        		activityManager.killBackgroundProcesses(processInfo.getPackageName());
			                  Toast.makeText(getApplicationContext(),
		            					"杀死进程：(packageName："+info.getPackageName()+"/appName:"+info.getAppName(), Toast.LENGTH_SHORT).show();
			        	}
		                  
			    	  }
			    	  handler=cleanProFragment.getRefreshHandler();
			      break;
			      case 1:
			    	  List<AppInfo> cleanListSer=(List<AppInfo>)cleanSerFragment.getView().getTag();
			    	  for (AppInfo info : cleanListSer) {			    		  
			    		  // 杀死进程
		                  activityManager.killBackgroundProcesses(info.getPackageName());
		                  Toast.makeText(getApplicationContext(),
	            					"杀死进程：(packageName："+info.getPackageName()+"/appName:"+info.getAppName(), Toast.LENGTH_SHORT).show();
			    	  }

                      handler=cleanSerFragment.getRefreshHandler();

			      break;
			      case 2:
			    	  List<AppInfo> cleanListTask=(List<AppInfo>)cleanTaskFragment.getView().getTag();
			    	  for (AppInfo info : cleanListTask) {			    		  
			    		  // 杀死进程
		                  activityManager.killBackgroundProcesses(info.getPackageName());
		                  Toast.makeText(getApplicationContext(),
	            					"杀死进程：(packageName："+info.getPackageName()+"/appName:"+info.getAppName(), Toast.LENGTH_SHORT).show();
			    	  }
			    	  handler=cleanTaskFragment.getRefreshHandler();
			      break;
			      
			      }
				  
		    	  Message msg = new Message();
                  msg.what = 0;
                  handler.sendMessage(msg);
				
			}
		});
	    
	      
	}
}

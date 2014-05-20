package com.example.first;


import java.util.ArrayList;
import java.util.List;

import com.example.first.FullscreenActivity.AppManagerAdapter;
import com.example.first.cleanUp.CleanUpActivity;
import com.example.first.player.MyPlayer;
import com.example.first.player.PlayAddressActivity;
import com.example.first.service.IService;
import com.example.first.service.WatchService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class ManageActivity extends Activity{
	private ManageInfo manageInfo;
	private static final int GET_ALL_APP_FINISH = 1;
	 private ManagerAdapter adapter;
	 private GridView appsView;
	 private boolean on_off_flag;
	 private Intent appLockIntent ;
		private MyConnection connection;
		private IService iService;
/*     private Handler handler = new Handler()
     {
             public void handleMessage(Message msg) 
             {
                     switch(msg.what)
                     {
                             case GET_ALL_APP_FINISH : 
                             	    //progressBar.setVisibility(View.GONE);
                                     adapter = new ManagerAdapter();
                                     appsView.setAdapter(adapter);
                                     break;
                                     
                             default : 
                                     break;
                     }
             };
     };*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manage);
	
		
	    manageInfo=new ManageInfo();
	    if(isWorked(this.getApplicationContext(),"com.example.first.service.WatchService")){
			 manageInfo.getList().get(1).setAppName("关闭锁定");
			 manageInfo.getList().get(1).setIconId(R.drawable.manage_lock);	
			 on_off_flag=true;//锁定服务是否开启的标志
	    }
	    else {
	    	
	    	on_off_flag=false;//锁定服务是否开启的标志
	    }
		/*ManageInfo.getList().add(new AppInfo(R.drawable.play,"服务管理",001));
		ManageInfo.getList().add(new AppInfo(R.drawable.play,"启动锁定",002));
		ManageInfo.getList().add(new AppInfo(R.drawable.play,"信息服务",003));*/
		
        
        appLockIntent= new Intent(this.getBaseContext(), WatchService.class);//
        appsView=(GridView) findViewById(R.id.manage_gridView);
       
        
        adapter = new ManagerAdapter();
        appsView.setAdapter(null);
        appsView.setAdapter(adapter);

        connection = new MyConnection();
        // 绑定服务，主要是为了能够调用服务里面的方法
        Intent intent = new Intent(this, WatchService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        
        
        
/*        new Thread()
        {
                public void run() 
                {                       
                        Message msg = new Message();
                        msg.what = GET_ALL_APP_FINISH;
                        handler.sendMessage(msg);
                };
        }.start();*/
/*                        new ManageInfo();                     
                        Message msg = new Message();
                        msg.what = GET_ALL_APP_FINISH;
                        handler.sendMessage(msg);*/

        
        
        appsView.setOnItemClickListener(new OnItemClickListener() {

    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position,
    				long id) {
    			// TODO Auto-generated method stub
    			AppInfo info = (AppInfo) appsView.getItemAtPosition(position);
    			switch (info.getFlagId()) {
    			case 001:
    				Intent intentNet= new Intent(ManageActivity.this,NetSearchActivity.class);
    				startActivity(intentNet);
    				break;
    			case 002:
    				on_off_Lock_service();
    				adapter = new ManagerAdapter();
    				 appsView.setAdapter(null);
    				 appsView.setAdapter(adapter);
    				//appsView.refreshDrawableState();
    				break;
    			case 003:
    				Intent intentManege= new Intent(ManageActivity.this, BrowseActivity.class);
    				startActivity(intentManege);
    				break;
    			case 004:
    				Intent intentCurrentInfo= new Intent(ManageActivity.this, CleanUpActivity.class);
    				startActivity(intentCurrentInfo);
    				break;
    			case 005:
    				Intent intentMyPlayer= new Intent(ManageActivity.this,PlayAddressActivity.class);
    				startActivity(intentMyPlayer);
    				break;
    			
    			}
    		
    		}
    	});
        
		
      
        
	}
	
	
    public void on_off_Lock_service(){
    	
    	if(on_off_flag==false){
    		
    		final EditText inputServer = new EditText(this);
    		new AlertDialog.Builder(this).setTitle("请输入").setIcon(
               android.R.drawable.ic_dialog_info).setView(
            		   inputServer).setPositiveButton("确定", new DialogInterface.OnClickListener(){ 
                 public void onClick(DialogInterface dialoginterface, int i){ 
                	 
               		 Toast.makeText(getApplicationContext(),
            					"开启服务", Toast.LENGTH_SHORT).show();
            		 manageInfo.getList().get(1).setAppName("关闭锁定");
            		 manageInfo.getList().get(1).setIconId(R.drawable.manage_lock);
            		 on_off_flag=true;
             		startService(appLockIntent);   	
             		iService.setPass(inputServer.getText().toString()); //按钮事件 

                	 
                  } 
          }).setNegativeButton("取消", null).show();
    		
    		
    		

		 }
		else { 
			stopService(appLockIntent);
		 Toast.makeText(getApplicationContext(),
					"关闭服务", Toast.LENGTH_SHORT).show();
    	 manageInfo.getList().get(1).setAppName("启动锁定");
    	 manageInfo.getList().get(1).setIconId(R.drawable.manage_unlock);
		 on_off_flag=false;  
		}

    }
	
	
	
	
	
	
	
	public class ManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return  manageInfo.getList().size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return  manageInfo.getList().get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			AppInfo info =  manageInfo.getList().get(position);
            if(convertView == null)
            {
                    View view = View.inflate(ManageActivity.this,R.layout.list_item_layout, null);
                    ManagerViews views = new ManagerViews();
                    views.iv_app_icon = (ImageView) view.findViewById(R.id.appIcon1);
                    views.tv_app_name = (TextView) view.findViewById(R.id.appName1);
                    views.iv_app_icon.setImageResource(info.getIconId());
                    views.tv_app_name.setText(info.getAppName());
                    view.setTag(views);
                    return view;
            }
            else
            {
                    ManagerViews views = (ManagerViews) convertView.getTag();
                    views.iv_app_icon.setImageResource(info.getIconId());
                    views.tv_app_name.setText(info.getAppName());
                    return convertView;
            }
		}

	}
    //用来优化listview的类
    private class ManagerViews
    {
            ImageView iv_app_icon;
            TextView tv_app_name;
    }
    
    private class MyConnection implements ServiceConnection
    {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                    // 我们之前在Service里面已经实现了IService接口了
                    iService = (IService) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }

			}
    
    @Override
    protected void onDestroy()//注意解除绑定
    {
            if (connection != null)
            {
                    unbindService(connection);
                    connection = null;
            }
            super.onDestroy();
    } 

	public  boolean isWorked(Context mContext,String className) { 
		ActivityManager myManager=(ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(100); 
		for(int i = 0 ; i<runningService.size();i++) { 
			if(runningService.get(i).service.getClassName().toString().equals(className)) 
			{ return true;
			} 
			} return false; 
			}
}

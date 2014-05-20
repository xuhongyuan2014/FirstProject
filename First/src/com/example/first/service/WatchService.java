package com.example.first.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.first.AppInfo;
import com.example.first.LockActivity;
import com.example.first.dao.AppLockDao;

public class WatchService extends Service{
	        private AppLockDao dao;
	        private List<String> apps;
	        private ActivityManager activityManager;
	        private Intent intent;
	        private boolean flag = true;
	        private List<String> stopApps;
	        private IBinder myBinder;
	        private String password="";

	        @Override
	        public void onCreate()
	        {
	                super.onCreate();

	                dao = new AppLockDao(this);
	                apps = dao.getAllPackageName();
	                stopApps=new ArrayList();
	                myBinder=new MyBinder();
	                activityManager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);

	                intent = new Intent(this, LockActivity.class);
	                // 服务里面是没有任务栈的，所以要指定一个新的任务栈，不然是无法在服务里面启动activity的
	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	                new Thread()
	                {
	                        public void run()
	                        {
	                                while (flag)
	                                {
	                                        try
	                                        {
	                                                // 得到当前运行的任务栈，参数就是得到多少个任务栈，1就是只拿一个任务栈
	                                                // 1对应的也就是正在运行的任务栈啦
	                                                List<RunningTaskInfo> runningTaskInfos = activityManager
	                                                                .getRunningTasks(1);
	                                                // 拿到当前运行的任务栈
	                                                RunningTaskInfo runningTaskInfo = runningTaskInfos
	                                                                .get(0);
	                                                // 拿到要运行的Activity的包名
	                                                String packageName = runningTaskInfo.topActivity
	                                                                .getPackageName();
	                                                if (stopApps.contains(packageName))
	                                                {
	                                                        sleep(1000);
	                                                        continue;
	                                                }
	                                                if (apps.contains(packageName))
	                                                {
	                                                        intent.putExtra("packageName", packageName);
	                                                        startActivity(intent);
	                                                }
	                                                else
	                                                {

	                                                }
	                                                sleep(1000);
	                                        }
	                                        catch (InterruptedException e)
	                                        {
	                                                e.printStackTrace();
	                                        }
	                                }
	                        }
	                }.start();
	        }

	        
	        
	        
	        
	      //临时开启对某个应用的保护
	        private void invokeMethodStartApp(String packageName)
	        {
	                if(stopApps.contains(packageName))
	                {
	                        stopApps.remove(packageName);
	                }
	        }
	        
	        //临时停止对某个应用的保护
	        private void invokeMethodStopApp(String packageName)
	        {
/*	                
	                if(!stopApps.contains(packageName))
	                {
	                	stopApps.add(packageName);
	                }*/
	        	stopApps.add(packageName);
	        }
	        
	        
        @Override
	        public void onDestroy()
	        {
	                super.onDestroy();
	                flag = false;
	        }

	        
  
            public IBinder onBind(Intent intent)
            {
                    return myBinder;
            }
	        
	        private class MyBinder extends Binder implements IService
	        {

	                @Override
	                public void startApp(String packageName)
	                {
	                        invokeMethodStartApp(packageName);
	                }

	                @Override
	                public void stopApp(String packageName)
	                {
	                        invokeMethodStopApp(packageName);
	                }

					@Override
					public void setPass(String pass) {
						// TODO Auto-generated method stub
						setPassword(pass);
					}

					@Override
					public String getPass() {
						// TODO Auto-generated method stub
						return getPassword();
					}
	                
	        }
	        public void setPassword(String pass){
	        	password=pass;
	        	
	        }
	        public String getPassword(){
	        	
	        	return password;
	        }
}

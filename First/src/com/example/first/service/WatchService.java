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
	                // ����������û������ջ�ģ�����Ҫָ��һ���µ�����ջ����Ȼ���޷��ڷ�����������activity��
	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	                new Thread()
	                {
	                        public void run()
	                        {
	                                while (flag)
	                                {
	                                        try
	                                        {
	                                                // �õ���ǰ���е�����ջ���������ǵõ����ٸ�����ջ��1����ֻ��һ������ջ
	                                                // 1��Ӧ��Ҳ�����������е�����ջ��
	                                                List<RunningTaskInfo> runningTaskInfos = activityManager
	                                                                .getRunningTasks(1);
	                                                // �õ���ǰ���е�����ջ
	                                                RunningTaskInfo runningTaskInfo = runningTaskInfos
	                                                                .get(0);
	                                                // �õ�Ҫ���е�Activity�İ���
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

	        
	        
	        
	        
	      //��ʱ������ĳ��Ӧ�õı���
	        private void invokeMethodStartApp(String packageName)
	        {
	                if(stopApps.contains(packageName))
	                {
	                        stopApps.remove(packageName);
	                }
	        }
	        
	        //��ʱֹͣ��ĳ��Ӧ�õı���
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

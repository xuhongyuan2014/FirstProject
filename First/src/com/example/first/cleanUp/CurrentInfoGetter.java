package com.example.first.cleanUp;

import java.util.ArrayList;
import java.util.List;

import com.example.first.AppInfo;
import com.example.first.AppInfoGetter;
import com.example.first.dao.AppLockDao;
import com.example.first.util.ApplicationInfoGetter;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class CurrentInfoGetter {
	private PackageManager packageManager;
	//private AppLockDao applockDao;
	private ActivityManager activityManager;
    public CurrentInfoGetter(Context context)
    {
            //�õ�һ����������
            //packageManager = context.getPackageManager();
            //applockDao=new AppLockDao(context);//ע��
    }
    public List<AppInfo> getAllServices(Context context) throws NameNotFoundException
    {   List<AppInfo> list = new ArrayList<AppInfo>();
        List<String> nameList=new ArrayList<String>();
        
        //packageManager = context.getPackageManager();
        activityManager = (ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
        for(RunningServiceInfo info : serviceList){
        	String packageName=info.service.getPackageName();
        	ApplicationInfoGetter infoGetter=new ApplicationInfoGetter();
    		AppInfo tempInfo=new AppInfo();
    		//tempInfo.setAppName(appInfo.loadLabel(packageManager).toString());
    		//tempInfo.setIcon(info.clientLabel);
    		//tempInfo.setAppName(info.process);
    		//tempInfo.setPackageName(info.clientPackage);
    		tempInfo=infoGetter.getAppInfoByPackageName(context, packageName);
    		tempInfo.setAppName(info.service.getClassName());
    		
				list.add(tempInfo);
    		}
        return list;
    }
    public List<AppInfo> getAllProcess(Context context)
    {   List<AppInfo> list = new ArrayList<AppInfo>();
        //List<String> nameList=new ArrayList<String>();
        
        packageManager = context.getPackageManager();
        activityManager = (ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> serviceList = activityManager.getRunningAppProcesses();
        for(RunningAppProcessInfo info : serviceList){
    		AppInfo tempInfo=new AppInfo();
    		//tempInfo.setAppName(appInfo.loadLabel(packageManager).toString());
    		//tempInfo.setIcon(info.clientLabel);
    		tempInfo.setAppName(info.processName);
    		tempInfo.setPackageName(info.pkgList.toString());
    		tempInfo.setObject(info);
    		 list.add(tempInfo);
    		}
        return list;
    }
    
    public List<AppInfo> getAllTasks(Context context)
    {   List<AppInfo> list = new ArrayList<AppInfo>();
        //List<String> nameList=new ArrayList<String>();
        
        packageManager = context.getPackageManager();
        activityManager = (ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = activityManager.getRunningTasks(100);
        for(RunningTaskInfo info : taskList){
        	String packageName=info.topActivity.getPackageName();
        	ApplicationInfoGetter infoGetter=new ApplicationInfoGetter();
       	 try {
				list.add(infoGetter.getAppInfoByPackageName(context, packageName));
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//AppInfo tempInfo=new AppInfo();
    		//tempInfo.setAppName(appInfo.loadLabel(packageManager).toString());
    		//tempInfo.setIcon(info.clientLabel);
    		//tempInfo.setAppName(info.topActivity.getPackageName());
    		//tempInfo.setPackageName(info.baseActivity.getPackageName());
    		// list.add(tempInfo);
    		}
        return list;
    }
    public List<AppInfo> getAppByProcess(Context context,RunningAppProcessInfo process){
    	List<AppInfo> list = new ArrayList<AppInfo>();
    	String[] pkgList=process.pkgList;
    	for(int i=0;i<=pkgList.length-1;i++)
    	{
    		String packageName=pkgList[i];
        	ApplicationInfoGetter infoGetter=new ApplicationInfoGetter();
         	 try { 				
         		 list.add(infoGetter.getAppInfoByPackageName(context, packageName));

 			} catch (NameNotFoundException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
    	}
    	return list;
    	
    }
    //�ж�ĳһ��Ӧ�ó����ǲ���ϵͳ��Ӧ�ó�������Ƿ���true�����򷵻�false
    public boolean filterApp(ApplicationInfo info)
    {
            //��ЩϵͳӦ���ǿ��Ը��µģ�����û��Լ�������һ��ϵͳ��Ӧ����������ԭ���ģ�
            //������ϵͳӦ�ã���������ж����������
            if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
            {
                    return true;
            }
            else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0)//�ж��ǲ���ϵͳӦ��
            {
                    return true;
            }
            return false;
    }
    public boolean findItem(List<String> nameList,String obj){
    	boolean result=false;
    	for(Object info : nameList){
    		if(info.equals(obj))
    			{result=true;
    			break;
    			}
    			
    	}
    	
    	return result;
    }
    
}

package com.example.first;

import java.util.ArrayList;
import java.util.List;

import com.example.first.dao.AppLockDao;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfoGetter {
	private PackageManager packageManager;
	private AppLockDao applockDao;
    public AppInfoGetter(Context context)
    {
            //拿到一个包管理器
            packageManager = context.getPackageManager();
            applockDao=new AppLockDao(context);//注意
    }
    public List<AppInfo> getAllApps()
    {   List<AppInfo> list = new ArrayList<AppInfo>();
        List<String> nameList=new ArrayList<String>();
        nameList=applockDao.getAllPackageName();
    	List<PackageInfo> infos=packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
    	/*.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);*/
    	for(PackageInfo info : infos){
    		AppInfo tempInfo=new AppInfo();
    		 ApplicationInfo appInfo = info.applicationInfo;
    		tempInfo.setAppName(appInfo.loadLabel(packageManager).toString());
    		tempInfo.setIcon(appInfo.loadIcon(packageManager));
    		tempInfo.setPackageName(info.packageName);
    		
    		if(findItem(nameList,info.packageName)==true){
    			tempInfo.setLocked(true);			
    		}
    		else {
    			tempInfo.setLocked(false);
    			
    		}
/*    		if(applockDao.find(info.packageName)){
    			tempInfo.setLocked(true);
    			}
    		else {
    			tempInfo.setLocked(false);
    			
    		}*/
    		if(filterApp(appInfo))
            {
    			tempInfo.setSystemApp(false);
            }
            else
            {
            	tempInfo.setSystemApp(true);
            }
            list.add(tempInfo);
    		
    	}
    	return list;
    }
    //判断某一个应用程序是不是系统的应用程序，如果是返回true，否则返回false
    public boolean filterApp(ApplicationInfo info)
    {
            //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，
            //它还是系统应用，这个就是判断这种情况的
            if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
            {
                    return true;
            }
            else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0)//判断是不是系统应用
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

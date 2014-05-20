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
            //�õ�һ����������
            packageManager = context.getPackageManager();
            applockDao=new AppLockDao(context);//ע��
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

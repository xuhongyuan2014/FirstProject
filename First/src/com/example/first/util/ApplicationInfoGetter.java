package com.example.first.util;

import com.example.first.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationInfoGetter {
private ApplicationInfo application;
private PackageManager packageManager;
private AppInfo appInfo;
public ApplicationInfoGetter(){


}
public AppInfo getAppInfoByPackageName(Context context,String packageName) throws NameNotFoundException{
	// TODO Auto-generated constructor stub
	packageManager=context.getPackageManager();
	application=packageManager.getPackageInfo(packageName, 0).applicationInfo;
	appInfo=new AppInfo();
	appInfo.setPackageName(packageName);
	appInfo.setIcon(application.loadIcon(packageManager));
	appInfo.setAppName(application.loadLabel(packageManager).toString());
	return appInfo;
}
}

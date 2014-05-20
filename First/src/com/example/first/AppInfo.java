package com.example.first;

import android.graphics.drawable.Drawable;

public class AppInfo
{
	    private int iconId;
	    private int flagId;
        private Drawable icon;
        private String appName;
		private String packageName;
        private boolean isSystemApp;
        private boolean isLocked;
        private String beizhu;
        private Object object;
        public AppInfo(){};
        AppInfo(int id,String name,int sid){
        	this.iconId=id;
        	this.appName=name; 
        	this.flagId=sid;
        };
        public Drawable getIcon() {
			return icon;
		}
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		public String getPackageName() {
			return packageName;
		}
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		public boolean isSystemApp() {
			return isSystemApp;
		}
		public void setSystemApp(boolean isSystemApp) {
			this.isSystemApp = isSystemApp;
		}
		public boolean isLocked() {
			return isLocked;
		}
		public void setLocked(boolean isLocked) {
			this.isLocked = isLocked;
		}
		public int getIconId() {
			return iconId;
		}
		public void setIconId(int iconId) {
			this.iconId = iconId;
		}
		public int getFlagId() {
			return flagId;
		}
		public void setFlagId(int flagId) {
			this.flagId = flagId;
		}
		public String getBeizhu() {
			return beizhu;
		}
		public void setBeizhu(String beizhu) {
			this.beizhu = beizhu;
		}
		public Object getObject() {
			return object;
		}
		public void setObject(Object object) {
			this.object = object;
		}

}
package com.example.first;

import java.util.ArrayList;
import java.util.List;

public class ManageInfo {
	
private  List<AppInfo> list;
ManageInfo(){
	list=new ArrayList();
	list.clear();
	list.add(new AppInfo(R.drawable.play,"网络搜索",001));
	list.add(new AppInfo(R.drawable.manage_unlock,"启动锁定",002));
	list.add(new AppInfo(R.drawable.play,"信息服务",003));
	list.add(new AppInfo(R.drawable.play,"当前运行",004));
	list.add(new AppInfo(R.drawable.myplayer,"电视直播",005));
}
public List<AppInfo> getList() {
	return list;
}
public void setList(List<AppInfo> list) {
	this.list = list;
}


}

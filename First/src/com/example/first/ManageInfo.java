package com.example.first;

import java.util.ArrayList;
import java.util.List;

public class ManageInfo {
	
private  List<AppInfo> list;
ManageInfo(){
	list=new ArrayList();
	list.clear();
	list.add(new AppInfo(R.drawable.play,"��������",001));
	list.add(new AppInfo(R.drawable.manage_unlock,"��������",002));
	list.add(new AppInfo(R.drawable.play,"��Ϣ����",003));
	list.add(new AppInfo(R.drawable.play,"��ǰ����",004));
	list.add(new AppInfo(R.drawable.myplayer,"����ֱ��",005));
}
public List<AppInfo> getList() {
	return list;
}
public void setList(List<AppInfo> list) {
	this.list = list;
}


}

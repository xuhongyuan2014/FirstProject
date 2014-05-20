package com.example.first.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.first.AppInfo;
import com.example.first.R;

public class TViconHelper {
	private List<KeyIcon> list;
	TViconHelper(){
		list=new ArrayList();
		list.clear();
		list.add(new KeyIcon("欧美",R.drawable.tv_meiguo));
		list.add(new KeyIcon("韩国",R.drawable.tv_hanguo));
		list.add(new KeyIcon("凤凰",R.drawable.tv_fenghuang));
		list.add(new KeyIcon("浙江",R.drawable.tv_zhejiang));
		list.add(new KeyIcon("CCTV",R.drawable.tv_cctv));
		list.add(new KeyIcon("江苏",R.drawable.tv_jiangsu));
		list.add(new KeyIcon("湖南",R.drawable.tv_hunan));
		list.add(new KeyIcon("东方",R.drawable.tv_dongfang));
	}
	public int getIconId(String name){
		int iconId=R.drawable.tv_default;
		for(KeyIcon info:list){
			String key=info.key;			
			if(name.startsWith(key)){
				iconId=info.iconId;
				break;
			}
			
		}
		
		return iconId;
		
	}

	private class KeyIcon{
		KeyIcon(String key,int iconId){
			this.key=key;
			this.iconId=iconId;			
		}
		private String key;
		private int iconId;
	}
	
}

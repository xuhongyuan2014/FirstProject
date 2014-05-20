package com.example.first;

import java.util.ArrayList;
import java.util.List;

import com.example.first.dao.AppLockDao;
import com.example.first.service.WatchService;
import com.example.first.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AnalogClock;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity implements OnClickListener {

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private EditText text;

	private static final int GET_ALL_APP_FINISH = 1;
	 
	private ListView appsView;
	
    private AppInfoGetter appGetter;
    
    private AppManagerAdapter adapter;
    
    private List<AppInfo> list;
	
    private LinearLayout progressBar;
    
    private PopupWindow popupWindow;
    
    private TextView show_current_appname;
    
    private ImageView show_current_app;
    
    private AppLockDao applockDao;
    
    private Intent appLockIntent ;
    
    private AnalogClock lock_on_off;
    
    private boolean on_off_flag;
    
    private InputMethodManager im_ctrl;
    
   // private ImageButton share_btn_search;
    
    private Handler handler = new Handler()
    {
            public void handleMessage(Message msg) 
            {
                    switch(msg.what)
                    {
                            case GET_ALL_APP_FINISH : 
                            	    progressBar.setVisibility(View.GONE);
                                    adapter = new AppManagerAdapter();
                                    appsView.setAdapter(adapter);
                                    break;
                                    
                            default : 
                                    break;
                    }
            };
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//默认键盘不弹出
		
		setContentView(R.layout.activity_fullscreen);
		applockDao=new AppLockDao(getBaseContext());//注意
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
		text = (EditText) findViewById(R.id.textInput);
		appsView=(ListView) findViewById(R.id.listViewForApps);
		progressBar=(LinearLayout) findViewById(R.id.waitLinear);
		progressBar.setVisibility(View.VISIBLE);
		show_current_app=(ImageView) findViewById(R.id.show_current_app);
		show_current_appname=(TextView) findViewById(R.id.show_current_appname);
		lock_on_off=(AnalogClock) findViewById(R.id.analogClock1);
		appLockIntent= new Intent(this.getBaseContext(), WatchService.class);
	    on_off_flag=false;
	    
	    im_ctrl = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    
	   // share_btn_search=(ImageButton) findViewById(R.id.net_search_webView);
		//share_btn_search.setOnClickListener(share_handler);
		

	    
	    
	    
		lock_on_off.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(on_off_flag==false){startService(appLockIntent);
				 Toast.makeText(getApplicationContext(),
							"开启服务", Toast.LENGTH_SHORT).show();
				 on_off_flag=true;
				 }
				else { stopService(appLockIntent);
				 Toast.makeText(getApplicationContext(),
							"关闭服务", Toast.LENGTH_SHORT).show();
				 on_off_flag=false; 
				}
			}
		});
		buttonConfirm.setOnClickListener(new Button.OnClickListener() {// 创建监听
					public void onClick(View v) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"你输入的是：" + text.getText(), Toast.LENGTH_SHORT);
						toast.show();
					}

				});


		appsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				
				dismissPopupWindow();
                //用来存放当前的item的坐标值，第一个是x的坐标，第二个是y的坐标
                int[] location = new int[2];
                //把当前的item的坐标值放到int数组里面
                view.getLocationInWindow(location);
				
				
				
				View popupView = View.inflate(FullscreenActivity.this, R.layout.item, null);
				// EditText text_show_info=(EditText) popupView.findViewById(R.id.text_show_info);
				ImageButton  btn_play = (ImageButton) popupView.findViewById(R.id.btn_play);
				ImageButton btn_show_info = (ImageButton) popupView.findViewById(R.id.btn_show_info);
				ImageButton btn_show_lock = (ImageButton) popupView.findViewById(R.id.btn_show_lock);		
				
                btn_play.setOnClickListener(FullscreenActivity.this);
                btn_show_info.setOnClickListener(FullscreenActivity.this);
                btn_show_lock.setOnClickListener(FullscreenActivity.this);
                
                //拿到当时点击的条目，并设置到view里面
                AppInfo info = (AppInfo) appsView.getItemAtPosition(position);
                btn_play.setTag(info);
                btn_show_info.setTag(info);
                btn_show_lock.setTag(info);
              //  text_show_info.setText(info.getAppName());
                btn_play.setBackgroundColor(Color.TRANSPARENT);
                btn_show_info.setBackgroundColor(Color.TRANSPARENT);
                btn_show_lock.setBackgroundColor(Color.TRANSPARENT);
                
                if(checkLockedByName(list,info.getAppName())==true){
                	btn_show_lock.setImageResource(R.drawable.lock_close);
                }
                else{
                	btn_show_lock.setImageResource(R.drawable.lock_open);              	
                }
                
                show_current_app.setImageDrawable(info.getIcon());
                show_current_appname.setText(info.getPackageName());
                
                
              //new 一个PopupWindow出来
                popupWindow = new PopupWindow(popupView, 500, 70);
                //一定要给PopupWindow设置一个背景图片，不然的话，会有很多未知的问题的
                //如没办法给它加上动画，还有显示会有问题等，
                //如果我们没有要设置的图片，那么我们就给它加上了一个透明的背景图片
                Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
                popupWindow.setBackgroundDrawable(drawable);
                
                
                
                
                int x = location[0] + 500;
                int y = location[1];
                //把PopupWindow显示出来
                popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, x, y);
			}
		});
		
		
		//因为搜索手机里面的应用程序有可能是非常耗时的，所以我们开启一个新的线程去进行搜索
        //当搜索完成之后，就把一个成功的消息发送给Handler，然后handler把搜索到的数据设置进入listview里面
        new Thread()
        {
                public void run() 
                {
                        appGetter = new AppInfoGetter(FullscreenActivity.this);
                        list = appGetter.getAllApps();                      
                        Message msg = new Message();
                        msg.what = GET_ALL_APP_FINISH;
                        handler.sendMessage(msg);
                };
        }.start();
		
		
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
/*		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});*/

		// Set up the user interaction to manually show or hide the system UI.
/*		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});*/

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
	}

/*	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}*/

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
/*	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};*/

	Handler mHideHandler = new Handler();
/*	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};*/

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
/*	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		/* the context menu currently has only one option */
		menu.add(0, Menu.FIRST, 2, R.string.menu1);
		menu.add(0, Menu.FIRST + 1, 1, R.string.menu2);
		menu.add(0, Menu.FIRST + 2, 3, R.string.menu3);
		menu.add(0, Menu.FIRST + 3, 4, "分享");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			showCopyright();
			break;
		case Menu.FIRST + 1:
			finish(); /* terminate the application */
			break;
		case Menu.FIRST + 2:
			goToMainPage(); 
			break;
		case Menu.FIRST + 3:
			fenxiang(); 
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showCopyright() {
		Toast toast = Toast.makeText(getApplicationContext(),
				R.string.copyright, Toast.LENGTH_SHORT);
		toast.show();

	}
	
	public class AppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			AppInfo info = list.get(position);
            if(convertView == null)
            {
                    View view = View.inflate(FullscreenActivity.this,R.layout.list_item_layout, null);
                    AppManagerViews views = new AppManagerViews();
                    views.iv_app_icon = (ImageView) view.findViewById(R.id.appIcon1);
                    views.tv_app_name = (TextView) view.findViewById(R.id.appName1);
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                    view.setTag(views);
                    return view;
            }
            else
            {
                    AppManagerViews views = (AppManagerViews) convertView.getTag();
                    views.iv_app_icon.setImageDrawable(info.getIcon());
                    views.tv_app_name.setText(info.getAppName());
                    return convertView;
            }
		}

	}
    //用来优化listview的类
    private class AppManagerViews
    {
            ImageView iv_app_icon;
            TextView tv_app_name;
    }
    
    //判断PopupWindow是不是存在，存在就把它dismiss掉
    private void dismissPopupWindow()
    {
            if(popupWindow != null)
            {
                    popupWindow.dismiss();
                    popupWindow = null;
            }
    }
    
	@SuppressWarnings("null")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 AppInfo item = (AppInfo) v.getTag();
         switch(v.getId())
         {
                 case R.id.btn_show_info : 
                         if(item.isSystemApp())
                         {
                                 Toast.makeText(FullscreenActivity.this, "系统应用程序", Toast.LENGTH_SHORT).show();
                         }
                         else
                         {
                        	     Toast.makeText(FullscreenActivity.this, "普通应用程序", Toast.LENGTH_SHORT).show();
                         }
                         break;
                         
                 case R.id.btn_play : 
                         try
                         {
                                 //拿到这个包对应的PackageInfo对象，这里我们指定了两个flag，
                                 //一个就是之前讲过的，所有的安装过的应用程序都找出来，包括卸载了但没清除数据的
                                 //一个就是指定它去扫描这个应用的AndroidMainfest文件时候的activity节点，
                                 //这样我们才能拿到具有启动意义的ActivityInfo，如果不指定，是无法扫描出来的
                                 PackageInfo packageInfo = getPackageManager().getPackageInfo(item.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
                                 //扫描出来的所以activity节点的信息
                                 ActivityInfo[] activityInfos = packageInfo.activities;
                                 //有些应用是无法启动的，所以我们就要判断一下
                                 if(activityInfos != null && activityInfos.length > 0)
                                 {
                                         //在扫描出来的应用里面，第一个是具有启动意义的
                                         ActivityInfo startActivity = activityInfos[0];
                                         //设置好Intent，启动activity
                                         Intent intent = new Intent();
                                         intent.setClassName(item.getPackageName(), startActivity.name);
                                         startActivity(intent);
                                 }
                                 else
                                 {
                                         Toast.makeText(FullscreenActivity.this, "这个应用程序无法启动", Toast.LENGTH_SHORT).show();
                                 }
                         }
                         catch (NameNotFoundException e)
                         {
                                 e.printStackTrace();
                         }
                         break;                        
                 case R.id.btn_show_lock : 
                	 if(item.isLocked()==true){
                		 applockDao.delete(item.getPackageName());  
                		// item.setLocked(false);
                		 updateAppInfoByName(list, item.getAppName(), false);
                		
                	 }
                	 else{
                		 applockDao.add(item.getPackageName(),"test");  
                		// item.setLocked(true);
                		 updateAppInfoByName(list, item.getAppName(),true);   
                		 
                	 }
                	 if(isWorked(this.getApplicationContext(),"com.example.first.service.WatchService"))
                	 { stopService(appLockIntent);
                	 startService(appLockIntent);
                	 }
                     break;
                         
                 default : 
                         break;
         }
         dismissPopupWindow();
	}
	
	public void updateAppInfoByName(List<AppInfo> infos,String name,boolean isLocked){
		for(AppInfo info : infos)
		{
			if(info.getAppName().equals(name)){
				info.setLocked(isLocked);	
				if(isLocked)Toast.makeText(FullscreenActivity.this, "加锁<"+name+">", Toast.LENGTH_SHORT).show();
				else Toast.makeText(FullscreenActivity.this, "解锁<"+name+">", Toast.LENGTH_SHORT).show();
				
			}			
		}
		
	}
	public boolean checkLockedByName(List<AppInfo> infos,String name){
		boolean flag=false;
		for(AppInfo info : infos)
		{
			if(info.getAppName().equals(name)){
				flag=info.isLocked();			
			}			
		}
		return flag;
	}
	public void goToMainPage(){
		Intent intent = new Intent(FullscreenActivity.this, ManageActivity.class);  
		startActivity(intent);
		
	}
	
	
	
	public  boolean isWorked(Context mContext,String className) { 
		ActivityManager myManager=(ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE); 
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30); 
		for(int i = 0 ; i<runningService.size();i++) { 
			if(runningService.get(i).service.getClassName().toString().equals(className)) 
			{ return true;
			} 
			} return false; 
			}


		public void fenxiang() {
 
	Intent intent=new Intent(Intent.ACTION_SEND); 
	intent.setType("text/plain"); 
	intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
	intent.putExtra(Intent.EXTRA_TEXT, "test");  
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(Intent.createChooser(intent, getTitle())); 
		}

}

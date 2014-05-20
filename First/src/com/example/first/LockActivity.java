package com.example.first;

import java.util.ArrayList;
import java.util.List;

import com.example.first.dao.AppLockDao;
import com.example.first.service.IService;
import com.example.first.service.WatchService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LockActivity extends Activity{
	private ImageView show_app_icon;
	private TextView show_app_name;
	private EditText show_app_password;
	private Button btn_ok;
	private AppLockDao applockDao;
	private List<String> unlockList;
	private String app_name;
	private IService iService;
	private MyConnection connection;
	private String packageName;
	private String password="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//Ĭ�ϼ��̲�����
		setContentView(R.layout.item_layout);
		applockDao=new AppLockDao(getBaseContext());//ע��
		show_app_icon=(ImageView) findViewById(R.id.unlock_app_icon);
		show_app_name=(TextView) findViewById(R.id.unlock_app_name);
		show_app_password=(EditText) findViewById(R.id.unlock_app_password);
		btn_ok=(Button) findViewById(R.id.btn_manage_confirm);
		unlockList=new ArrayList();
		try
        {
                packageName = getIntent().getStringExtra("packageName");//��ð���
                //ͨ�������õ�applicationInfo
                ApplicationInfo appInfo = getPackageManager().getPackageInfo(packageName, 0).applicationInfo;
                //Ӧ��ͼ��
                Drawable app_icon = appInfo.loadIcon(getPackageManager());
                //Ӧ�õ�����
                app_name = appInfo.loadLabel(getPackageManager()).toString();
                
                show_app_icon.setImageDrawable(app_icon);
                show_app_name.setText(app_name);
                
                
                connection = new MyConnection();
                // �󶨷�����Ҫ��Ϊ���ܹ����÷�������ķ���
                Intent intent = new Intent(this, WatchService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
                
        }
        catch (Exception e)
        {
                e.printStackTrace();
        }
		
		
		
		
		
		}
	//��ť�ĵ���¼�
    public void confirm(View v)
    {
            String input = show_app_password.getText().toString().trim();
           if(TextUtils.isEmpty(input))
            {
                    Toast.makeText(this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
            }
            else if(checkPassword(input)==true)
            {
            	    //unlockList.add(app_name);
            	
                    finish();
                    iService.stopApp(packageName);//���÷�������ķ���
            }
            else
            {
                    Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();
            }
    }
    
    private boolean checkPasswordBySql(String input) {
		// TODO Auto-generated method stub
    	boolean flag=false;
    	if(applockDao.checkPassword(input)==true){flag=true;}
		return flag;
	}
    private boolean checkPassword(String input) {
		// TODO Auto-generated method stub
    	boolean flag=false;
    	if(input.equals(password)){flag=true;}
		return flag;
	}
	//�����û������˼�
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
            //���κ��˼�
            if(KeyEvent.KEYCODE_BACK == event.getKeyCode())
            {
                    return true;//��ֹ�¼��������·ַ�
            }
            return super.onKeyDown(keyCode, event);
    }
    
    private class MyConnection implements ServiceConnection
    {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                    // ����֮ǰ��Service�����Ѿ�ʵ����IService�ӿ���
                    iService = (IService) service;
                    password=iService.getPass();
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }

			}
    
    @Override
    protected void onDestroy()//ע������
    {
            if (connection != null)
            {
                    unbindService(connection);
                    connection = null;
            }
            super.onDestroy();
    }

    }


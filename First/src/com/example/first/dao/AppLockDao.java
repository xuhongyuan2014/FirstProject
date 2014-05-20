package com.example.first.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {
	 private DBHelper dbHelper;
     
     public AppLockDao(Context context)
     {
             dbHelper = new DBHelper(context);
     }
     
     public boolean find(String packageName)
     {
             boolean result = false;
             SQLiteDatabase db = dbHelper.getReadableDatabase();
             if(db.isOpen())
             {
                     Cursor cursor = db.rawQuery("select packagename from applock where packagename = ? ", new String[] {packageName});
                     if(cursor.moveToNext())
                     {
                             result = true;
                     }
                     cursor.close();
                     db.close();
             }
             return result;
     }
     
     public boolean checkPassword(String password)
     {
             boolean result = false;
             SQLiteDatabase db = dbHelper.getReadableDatabase();
             if(db.isOpen())
             {
                     Cursor cursor = db.rawQuery("select packagename from applock where password = ? ", new String[] {password});
                     if(cursor.moveToNext())
                     {
                             result = true;
                     }
                     cursor.close();
                     db.close();
             }
             return result;
     }
     
     public void add(String packageName,String password)
     {
             if(find(packageName))
             {
                     return ;
             }
             SQLiteDatabase db = dbHelper.getWritableDatabase();
             if(db.isOpen())
             {
                     db.execSQL("insert into applock (packagename,password) values (?,?)", new Object[] {packageName,password});
                     db.close();
             }
     }
     
     public void delete(String packageName)
     {
             SQLiteDatabase db = dbHelper.getWritableDatabase();
             if(db.isOpen())
             {
                     db.execSQL("delete from applock where packagename = ? ", new Object[] {packageName});
             }
     }
     
     public List<String> getAllPackageName()
     {
             SQLiteDatabase db = dbHelper.getReadableDatabase();
             List<String> packageNames = new ArrayList<String>();
             if(db.isOpen())
             {
                     Cursor cursor = db.rawQuery("select packagename from applock", null);
                     while(cursor.moveToNext())
                     {
                             String packageName = cursor.getString(0);
                             packageNames.add(packageName);
                     }
                     cursor.close();
                     db.close();
             }
             return packageNames;
     }

}

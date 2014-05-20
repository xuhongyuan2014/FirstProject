package com.example.first.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

	public class DBHelper extends SQLiteOpenHelper
	{

	        public DBHelper(Context context)
	        {
	                super(context, "security.db", null, 2);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db)
	        {
	                db.execSQL("create table applock (_id integer primary key autoincrement, packagename varchar(20),password varchar(20))");
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	        {
	              //  db.execSQL("create table applock (_id integer primary key autoincrement, packagename varchar(30))");
	        }


	}


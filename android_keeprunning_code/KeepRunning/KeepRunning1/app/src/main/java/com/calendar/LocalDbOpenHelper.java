package com.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LocalDbOpenHelper extends SQLiteOpenHelper {

    //根据用户id创建数据库，假设为5
    private final static int UID=5;
    private final static String TABLE_SIGN_IN="signIn";//表名
    private final static String COLUMN_NAME_DATE="date";//日期列名
    private final static String USER_NAME="username";//用户名
    private SQLiteDatabase mdb=null;
    private static LocalDbOpenHelper mHelper=null;

    public static LocalDbOpenHelper getInstance(Context context,int version){
        if (version>0&&mHelper==null){
            mHelper=new LocalDbOpenHelper(context,version);
        }else if(mHelper==null){
            mHelper=new LocalDbOpenHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink(){
        if(mdb==null||!mdb.isOpen()){
            mdb=mHelper.getReadableDatabase();
        }
        return mdb;
    }

    public LocalDbOpenHelper(Context context,int version){
        super(context,DATABASE_NAME,null,version);
    }

    //定义签到表
    private static final String SQL_CREATE_SIGN="CREATE TABLE " +
            TABLE_SIGN_IN + " (" +USER_NAME+" VARCHAR(30) ,"+ COLUMN_NAME_DATE + " VARCHAR(30) " + ")";

    private static final String SQL_DELETE_SIGN_IN = "DROP TABLE IF EXISTS " + TABLE_SIGN_IN;

    public static final int DATABASE_VERSION = 1;         //数据库版本号
    public static final String DATABASE_NAME = "local_"+UID+".db"; //数据库名字




    public LocalDbOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SIGN);//创建签到表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_SIGN_IN);//删除旧表
        onCreate(db);//新建版本表
    }

    public void dropTable(){
        SQLiteDatabase db=getReadableDatabase();
        db.execSQL(SQL_CREATE_SIGN);//删除旧表
        onCreate(db);//新建版本表
    }
}

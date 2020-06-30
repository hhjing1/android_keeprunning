package com.DBOpenMessageUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bean.location;

public class DBOpenMessageUserlocation extends SQLiteOpenHelper
{
    final String db_location="create table db_location (_id integer primary key autoincrement,username varchar,date varchar,points text,distance varchar,time varchar,energy varchar,speed varchar)";
    public DBOpenMessageUserlocation(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_location);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("数据","更新"+"oldVerSion"+"-->"+newVersion);
    }

    /**
    *数据库的相关操作
    * **/
    //增加信息
    public long insertlocation(SQLiteDatabase sqLiteDatabase, location location) {
        ContentValues cv = new ContentValues();
        cv.put("username", location.getUsername());
        cv.put("date", location.getDate());
        cv.put("points", location.getPoints());
        cv.put("time", location.getTime());
        cv.put("distance", location.getDistance());
        cv.put("energy", location.getEnergy());
        cv.put("speed", location.getSpeed());
        long wen=sqLiteDatabase.insert("db_location", null, cv);
       return wen;
    }
//删除信息
public void deletebynameanddistance(String username,String date)
{
    SQLiteDatabase database = getWritableDatabase();
    database.execSQL("delete from db_location where username=? and date=?", new String[]{username,date});
}
    //查找
       // database.execSQL("delete from db_location where  date=? and username=?", new String[]{date,username});

    public Cursor getAllLocation(String username) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor=database.query("db_location", null, "username=?",new String[]{username},null, null,null);//"userdata desc"
        return cursor;
        //return database.query("db_location", null, "username=?",new String[]{username},null, null,null);//"userdata desc"
       // return database.query("db_location", null,null,null,null, null,null);//"userdata desc"
    }
    public Cursor gettodayLocation(String username,String date) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor=database.query("db_location", null,"date like '%" + date + "%'and username='"+username+"'", null, null, null, null);
        return cursor;

    }

//    public void updatauser(String username,String usercheck)
//    {
//        SQLiteDatabase database = getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put("usercheck",usercheck);
//        database.update("db_wen",values,"username=?",new String[]{username});
//    }
//    public void updatauserpicture(String username,String userpicture)
//    {
//        SQLiteDatabase database = getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put("userpicture",userpicture);
//        database.update("db_wen",values,"username=?",new String[]{username});
//    }
//    public void updatapassword(String username,String password)
//    {
//        SQLiteDatabase database = getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put("password",password);
//        database.update("db_wen",values,"username=?",new String[]{username});
//    }
}

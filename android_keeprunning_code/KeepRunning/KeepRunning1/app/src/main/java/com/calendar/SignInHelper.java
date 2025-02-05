package com.calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import java.util.List;


public class SignInHelper {

    private static final String TAG="SignInHelper--->>>";
    private SQLiteDatabase db;
    public SignInHelper(Context context){
        db=new LocalDbOpenHelper(context).getReadableDatabase();
    }

    public void insert(String signIn,String username){
        String sql = "insert into signIn values('"+username+"','"+signIn+"')";
        db.execSQL(sql);
    }

    public List<String> query(String username,int year,int month) {
        String mid = month > 9 ? "-" : "-0";
        String selection = "date like ? and username = ?";
        String[] selectArgs = new String[]{"%" + year + mid + month + "%",username};

        Cursor cursor = db.query("signIn",null,selection,selectArgs,null,null,null);
        List<String> result=new ArrayList<>();
        while (cursor.moveToNext()){
            String  date=cursor.getString(cursor.getColumnIndex("date"));
            result.add(date);
        }
        cursor.close();
        return result;
    }
}

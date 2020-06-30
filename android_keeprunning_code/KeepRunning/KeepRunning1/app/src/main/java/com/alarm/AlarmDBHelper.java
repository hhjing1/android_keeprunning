package com.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AlarmDBHelper extends SQLiteOpenHelper {
    public AlarmDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "alarm", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creat="create table useralarm ("
                +"id integer primary key autoincrement, "
                +"username varchar, "
                +"hour varchar, "
                +"minute varchar, "
                +"content text, "
                +"clockType int)";
        db.execSQL(creat);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

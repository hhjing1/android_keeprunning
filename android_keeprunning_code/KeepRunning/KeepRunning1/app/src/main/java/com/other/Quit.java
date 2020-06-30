package com.other;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


public class Quit extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        //设置提示框内容
        b.setMessage("真的忍心离开我们？");
        //设置标题栏
        b.setTitle("提示");
        b.setPositiveButton("离开", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                ActivityManager am = (ActivityManager)getContext().getSystemService (Context.ACTIVITY_SERVICE);
                am.killBackgroundProcesses(getContext().getPackageName());
                System.exit(0);
            }
        }).setNegativeButton("留下", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }
}

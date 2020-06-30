package com.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.keeprunning1.R;
import com.login.LoginActivity;
import com.user.User;
import com.user.UserOperator;

public class SettingFragment extends Fragment {
    View view;
    private ImageView userPicture;
    private String username;
    private UserOperator muserOperator;
    private LinearLayout perMessages;
    private LinearLayout cancel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hello", "hello");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.per_setting, null);
        userPicture = (ImageView) view.findViewById(R.id.user_picture);
        perMessages = (LinearLayout) view.findViewById(R.id.per_message);
        cancel = (LinearLayout) view.findViewById(R.id.cancel);
        username = getArguments().getString("username");
        Log.i("username", username);
        muserOperator = new UserOperator(view.getContext());
        User bean = muserOperator.isExit(username);
        String userpicture = bean.userpicture;
        Bitmap bitmap = BitmapFactory.decodeFile(userpicture);
        userPicture.setImageBitmap(bitmap);
        cancel.setOnClickListener(l);
        perMessages.setOnClickListener(l);
        return view;
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.per_message:
                    //向首页穿参数
                    Intent intent = new Intent(view.getContext(), PerMessageActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    break;
                case R.id.cancel:
                    usercancel();
                    break;
                default:
                    break;
            }
        }
    };

    public void usercancel() {
        AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
        //设置提示框内容
        b.setMessage("确认注销？");
        //设置标题栏
        b.setTitle("提示");
        b.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                muserOperator.usercancel(username);
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().onBackPressed();//销毁当前fragment
                Toast.makeText(view.getContext(), "注销成功", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext(), "注销失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        b.create().show();
    }
}
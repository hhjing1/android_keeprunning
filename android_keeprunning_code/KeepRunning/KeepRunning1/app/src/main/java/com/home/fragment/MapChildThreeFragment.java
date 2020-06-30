package com.home.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.DBOpenMessageUser.DBOpenMessageUserlocation;
import com.example.keeprunning1.R;
import com.user.User;
import com.user.UserOperator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapChildThreeFragment extends Fragment {
    private DBOpenMessageUserlocation dbOpenMessage;
    private String username;
    private TextView distance11,calor11,shiwu11,chronometer,userhealth,userhealth1;
    private Double  distance=0.0,energy=0.0;
    private EditText userheight,userweight;
    private long time=0;
    private String shiwuneirong;
    private String zhishu1="";
    private String zhishu2="";
    private UserOperator userOperator;
//    private Chronometer chronometer = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapchildthree, container, false);
        distance11=(TextView)view.findViewById(R.id.distance11);
        calor11=(TextView)view.findViewById(R.id.calor11);
        shiwu11=(TextView)view.findViewById(R.id.shiwu11);
        chronometer = (TextView)view.findViewById(R.id.time11);
        userhealth=(TextView)view.findViewById(R.id.user_health);
        userhealth1=(TextView)view.findViewById(R.id.user_health1);
        userOperator=new UserOperator(view.getContext());
        testRandom2();
        init();
        User bean = userOperator.isExit(username);
        zhishu2=bean.health;
        userhealth1.setText(zhishu2);
        Health();



        return view;
    }
    private void init()
    {
        dbOpenMessage = new DBOpenMessageUserlocation(getActivity(), "db_location", null, 1);
        Intent intent = getActivity().getIntent();//获取Intent对象
        username = intent.getStringExtra("username");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        Date date1 = new Date(System.currentTimeMillis());
        String date=simpleDateFormat.format(date1);
        getMessage1(username,date);

        DecimalFormat decimalFormat;
        decimalFormat = new DecimalFormat("0.00");

        chronometer.setText(decimalFormat.format(time/(60000)));
//        chronometer.setFormat("00:%s");
//        chronometer.setBase(SystemClock.elapsedRealtime()-time);
        distance11.setText(decimalFormat.format(distance));
        calor11.setText(decimalFormat.format(energy));
        if(distance>0.001)
            shiwu11.setText(shiwuneirong);
    }

    private void getMessage1(String username,String date) {
        Cursor cursor = dbOpenMessage.gettodayLocation(username,date);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                distance+=Double.parseDouble(cursor.getString(cursor.getColumnIndex("distance")));
                energy+= Double.parseDouble(cursor.getString(cursor.getColumnIndex("energy")));
                time+=Long.parseLong(cursor.getString(cursor.getColumnIndex("time"))) ;
            }
        }
    }

    private void testRandom2(){
        if(time/(60000)<60){
            shiwuneirong="1L水、新鲜水果";
        }
        else if (time/(60000)>60&&time/(60000)<180){
            shiwuneirong="运动饮料、水果干、谷物营养棒、果冻";
        }
        else{
            shiwuneirong="1.5L水、小黄油饼干、甜乳制品、新鲜水果";
        }
//       Random random=new Random();
//        int wen[]={0,0,0,0,0,0,0,0,0,0};
//        String wen1[]={"苹果","火龙果","土豆","猕猴桃","圣女果","西蓝花","米饭","香蕉","荔枝","西瓜"};
//        int i=0;
//        while (i<5) {
//     int wen=random.nextInt(9);
//            for(int j=0;j<i;j++)
//            {
//                if(wen[j]==wen[i])
//                    continue;
//            }
        //while(i<5) {
//        if(wen<4)
//             shiwuneirong="苹果、火龙果、猕猴桃";
//        else if(wen<7)
//            shiwuneirong="圣女果、西蓝花、淀粉类";
//        else
//            shiwuneirong="香蕉、荔枝、西瓜";
        //  i++;
        //}
//            i++;
//        }
    }
    //刷新作用
    @Override
    public void onResume() {
        super.onResume();
        distance=0.0;
        energy=0.0;
        time=0;
        shiwuneirong="";
        shiwu11.setText("");
        testRandom2();
        init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        distance=0.0;
        energy=0.0;
        time=0;
        shiwuneirong="";
        shiwu11.setText("");
        testRandom2();
        init();
    }
    private void Health() {
        userhealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getView().getContext());
                LayoutInflater inflater2 = LayoutInflater.from(getView().getContext());
                View viewDialog2 = inflater2.inflate(R.layout.health, null);
                userheight = (EditText) viewDialog2.findViewById(R.id.height);
                userweight = (EditText) viewDialog2.findViewById(R.id.weight);
                builder2.setView(viewDialog2);
                builder2.setTitle("身体指数BMI");
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final double userheight1 = Double.parseDouble(userheight.getText().toString());
                        final double userweight1 = Double.parseDouble(userweight.getText().toString());
                        if(userheight1<=0||userheight1>=2.5){
                            Toast.makeText(getView().getContext(), "请输入正确的身高", Toast.LENGTH_SHORT).show();
                        }else if(userweight1<=0||userweight1>=200){
                            Toast.makeText(getView().getContext(), "请输入正确的体重", Toast.LENGTH_SHORT).show();
                        }else {
                            double m = (userweight1/ (userheight1 * userheight1));
                            DecimalFormat df = new DecimalFormat("0.0");
                            double zhishu= Double.parseDouble(df.format(m));
                            if(zhishu< 18.5){
                                zhishu1=zhishu+"偏瘦";
                            }else if(18.5<=zhishu && zhishu< 24){
                                zhishu1=zhishu+"正常";
                            }else if(24<=zhishu && zhishu< 25){
                                zhishu1=zhishu+"正常";
                            }
                            else if(25<=zhishu && zhishu< 28){
                                zhishu1=zhishu+"偏胖";
                            }else if(28<=zhishu && zhishu< 30){
                                zhishu1=zhishu+"偏胖";
                            }else {
                                zhishu1=zhishu+"肥胖";
                            }
                            User bean = new User();
                            bean.setUsername(username);
                            bean.setHealth(zhishu1);
                            userOperator.add(bean);
                            User bean1 = userOperator.isExit(username);
                            zhishu2=bean1.health;
                            userhealth1.setText(zhishu2);
                        }
                    }
                });
                builder2.setNegativeButton("取消", null);
                builder2.create().show();

            }
        });
    }

}

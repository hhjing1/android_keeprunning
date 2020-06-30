package com.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calendar.DateAdapter;
import com.calendar.DateUtil;
import com.example.keeprunning1.R;


//签到控件

public class SignView extends LinearLayout {

    private TextView tvYear;
    private SignGridView gvDate;
    private DateAdapter adapterDate;

    public SignView(Context context) {
        super(context);
        init();
    }

    public SignView(Context context,AttributeSet attrs){
        super(context,attrs);
        init();
    }
    public SignView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init();
    }

    public void init(){
        init(DateUtil.year,DateUtil.month);
    }

    public void init(int year,int month){
        View view=View.inflate(getContext(), R.layout.layout_signdate,this);
        tvYear=view.findViewById(R.id.tvYear);
        gvDate=view.findViewById(R.id.gvDate);
        tvYear.setText(year+"-"+month);
        String username=getContext().getSharedPreferences("filter",Context.MODE_PRIVATE).getString("username","");
        adapterDate=new DateAdapter(getContext(),year,month,username);
        gvDate.setAdapter(adapterDate);
    }

    public void signIn(DateAdapter.OnSignListener onSignListener){
        adapterDate.signIn(onSignListener);
    }

    public boolean isSign(){
        return adapterDate.isSign();
    }
}

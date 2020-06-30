package com.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {

    private static final String TAG = "DateUtil--->>>";

    public static int year;
    public static int month;
    public static int day;
    public static String current;

    static{
        Calendar a=Calendar.getInstance();
        year=a.get(Calendar.YEAR);
        month=a.get(Calendar.MONTH)+1;
        day=a.get(Calendar.DAY_OF_MONTH);
        current=getDate(new Date(),"YYY-MM-dd");
    }

    public static String getDate(Date source,String style) {
        SimpleDateFormat mdhm = new SimpleDateFormat(style, Locale.getDefault());//年 月 日
        return mdhm.format(source);
    }

    public static Date getDate(int y,int m,int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y,m-1,d); //月减一
        return calendar.getTime();
    }

    public static String getDate(Date source) {
        SimpleDateFormat mdhm = new SimpleDateFormat("YYYY-MM-dd",Locale.getDefault());//年 月 日
        return mdhm.format(source);
    }

    public static int getCurrentMonthLastDay(int year,int month) {
        Calendar a = Calendar.getInstance();
        a.set(year,month-1,1);//把日期设置为当月第一天 月减一
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        return a.get(Calendar.DATE);
    }

    public static int getFirstDayOfMonth(int year,int month){
        Calendar a = Calendar.getInstance();
        a.set(year,month-1,1); //月要减一
        a.set(Calendar.DAY_OF_MONTH,1);//设为第一天
        return a.get(Calendar.DAY_OF_WEEK);
    }

    // 字符串类型日期转化成date类型

    public static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style,Locale.getDefault());
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String calendarToDateTime(Calendar calendar, String style) {
        Date time = calendar.getTime();
        return getDate(time,style);
    }



    public static int[] getymd(String formatDateString) {
        int[] ymd = new int[3];
        ymd[0] = Integer.parseInt(formatDateString.substring(0, 4));
        ymd[1] = Integer.parseInt(formatDateString.substring(formatDateString.indexOf("-") + 1, formatDateString.lastIndexOf("-")));
        ymd[2] = Integer.parseInt(formatDateString.substring(formatDateString.lastIndexOf("-") + 1, formatDateString.length()));
        return ymd;

    }





    public static List<Boolean> dateConvert(int year, int month, List<String> source, List<Boolean> record, int dif) {
        for (String s : source) {
            int[] ymd = getymd(s);
            if (year == ymd[0] && month == ymd[1]) {//年月相同
                record.set(ymd[2] + dif, true);
          }
        }
        return record;
    }


}

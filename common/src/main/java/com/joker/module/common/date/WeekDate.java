package com.joker.module.common.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Joker on 2017/4/30.
 */
public class WeekDate {
    private Date from;
    private Date end;

    public WeekDate() {
        Calendar calendar = Calendar.getInstance();

        //获取当前周第几天，从0开始
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);
        int second = calendar.get(calendar.SECOND);

        //系统周日为第一天，所以减掉1从周一开始算第一天
        day = day - 1;

        //如果为周日，系统则是第二周，则做个减1到周六，本周
        if (day == 0) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
            day = calendar.get(Calendar.DAY_OF_WEEK);
            day = day - 1;
        }

        //减去当前周第几天等于初始时间
        calendar.add(Calendar.DAY_OF_WEEK, -day + 1);
        calendar.add(Calendar.HOUR_OF_DAY, -hour);
        calendar.add(Calendar.MINUTE, -minute);
        calendar.add(Calendar.SECOND, -second + 1 );

        this.from = calendar.getTime();

        //减去当前周第几天再加上6等于本周最后一天
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 58);

        this.end = calendar.getTime();

    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


    public static void main(String[] args) {
        WeekDate wd = new WeekDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("from = [" + sdf.format(wd.getFrom()) + "]");
        System.out.println("end = [" + sdf.format(wd.getEnd()) + "]");
    }
}

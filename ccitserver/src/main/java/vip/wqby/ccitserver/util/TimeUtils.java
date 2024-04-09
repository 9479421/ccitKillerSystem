package vip.wqby.ccitserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getTimeStr(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd日hh:mm:ss");
        String nowTime = sdf.format(date);
        return nowTime;
    }
    public static String getTimeStr(String date){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date1= simpleDateFormat.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd日hh:mm");
            String nowTime = sdf.format(date1);
            return nowTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "时间异常";
    }
}

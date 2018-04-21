package br.com.neogis.agritopo.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by marci on 21/04/2018.
 */

public class DateUtils {
    public static Date addDays(Date date, int days){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date addDays(int days){
        Calendar c = Calendar.getInstance();
        c.setTime(getCurrentDateTime());
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    public static long getDaysBetween(Date startDate, Date endDate){
        return TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());
    }

    public static Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getCurrentDateTime(){
        return Calendar.getInstance().getTime();
    }
}

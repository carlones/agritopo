package br.com.neogis.agritopo.utils;

import android.content.Context;
import android.location.Location;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import br.com.neogis.agritopo.model.MyGpsMyLocationProvider;

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

    public static Date getDateWithOutTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String formatDate(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(date);
    }

    public static String formatDateddMMyyyyThhmmssZ(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ssZ");
        return df.format(date);
    }

    public static Date convertoToDateddMMyyyyThhmmssZ(String text) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ssZ");
        return df.parse(text);
    }

    public static Date getCurrentDateTime(){
        return Calendar.getInstance().getTime();
    }

    public static Date getCurrentDateFromGPS(Context contexto) {
        MyGpsMyLocationProvider gps = new MyGpsMyLocationProvider(contexto, null);
        try {
            Location location = gps.getLastKnownLocation();
            if (location != null)
                return DateUtils.getDateWithOutTime(new Date(location.getTime()));
            else
                return DateUtils.getCurrentDate();
        } finally {
            gps.stopLocationProvider();
        }
    }
}

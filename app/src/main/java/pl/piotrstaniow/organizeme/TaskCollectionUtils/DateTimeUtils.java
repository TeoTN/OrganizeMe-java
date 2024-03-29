package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created on 17.05.15.
 */
public class DateTimeUtils {
    //Function used for managing database
    public static String dateToString(Date date, boolean isTimeSet){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        String str = ""+d+'.'+m+'.'+y;
        if(isTimeSet){
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);

            str += " "+h+":"+min;
        }

        return str;
    }
    //Function used for managing database
    public static Date stringToDate(String str) {
        boolean isTimeSet = false;
        String[] date_time;
        String[] splitted_time = {};
        String[] splitted_date;
        if(str.contains(" ")){
            isTimeSet = true;
            date_time = str.split(" ");
            splitted_time = date_time[1].split(":");
            splitted_date = date_time[0].split("\\.");
        } else {
            splitted_date = str.split("\\.");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(splitted_date[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splitted_date[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitted_date[0]));
        if(isTimeSet){
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitted_time[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(splitted_time[1]));
        }
        return calendar.getTime();
    }
    //Function used while adding or editing task
    public static Date cutTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    //Function used while adding or editing task
    public static Date setTimeInDate(Date date, int hour, int min){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }
    //Function used while adding or editing task
    public static Date setDateInDate(Date date, int y, int m, int d, boolean isTimeSet){
        Calendar calendar = Calendar.getInstance();
        if(isTimeSet)
            date = DateTimeUtils.cutTime(date);
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, d);

        date = calendar.getTime();
        return date;
    }

    public static String dateToDisplay(Date date, boolean isTimeSet){
        String displayDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        String m = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        displayDate = String.valueOf(d) + ' ' + m + ' ' + y;
        int h, min = 0;
        if(isTimeSet){
            h = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
            displayDate += " " + h + ":";
            if(min < 10)
                displayDate += "0" + min;
            else
                displayDate += "" + min;

        }
        return displayDate;
    }
}

package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created on 17.05.15.
 */
public class TaskUtils {
    public static String dateToString(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        return ""+d+'.'+m+'.'+y;
    }

    public static Date stringToDate(String str) {
        String[] splitted = str.split("\\.");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(splitted[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(splitted[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitted[0]));
        return calendar.getTime();
    }

    public static Date cutTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}

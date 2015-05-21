package pl.piotrstaniow.organizeme;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * OrganizeMe
 * <p/>
 * Author: Piotr Staniów
 * Email: staniowp@gmail.com
 * Created: 12.05.15
 */
public class Task {
    private static long lastID = 0;
    private String taskDesc;
    private String taskDate;
    private Date date;
    private long myID;
    //Don't serialize following:
    private boolean isFirstInGroup;
    private String predicate;

    public Task() {
        date = new Date();
        myID = lastID++;
        isFirstInGroup = false;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String toString() {
        return taskDesc;
    }

    public long getID() {
        return myID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        String m = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

        taskDate = String.valueOf(d) + ' ' + m + ' ' + y;
        this.date = date;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public boolean isFirstInGroup() {
        return isFirstInGroup;
    }

    public void setIsFirstInGroup(boolean isFirstInGroup) {
        this.isFirstInGroup = isFirstInGroup;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }
}

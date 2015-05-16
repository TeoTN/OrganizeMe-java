package pl.piotrstaniow.organizeme;

import java.util.Calendar;
import java.util.Date;

/**
 * OrganizeMe
 * <p/>
 * Author: Piotr Staniów
 * Email: staniowp@gmail.com
 * Created: 12.05.15
 */
public class Task {
    private String taskDesc;
    private String taskDate;
    private Date date;
    private long myID;
    private static long lastID = 0;

    //Don't serialize following:
    private boolean isFirstInGroup;
    private String predicate;

    public Task(String taskDesc) {
        this.taskDesc = taskDesc;
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

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
        String[] datesplit = taskDate.split("\\.");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(datesplit[2]));
        cal.set(Calendar.MONTH, Integer.parseInt(datesplit[1])-1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datesplit[0]));
        date = cal.getTime();
    }

    public long getID() {
        return myID;
    }

    public Date getDate() {
        return date;
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

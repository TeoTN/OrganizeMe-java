package pl.piotrstaniow.organizeme.Models;

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
    private String taskDesc;
    private String displayDate;
    private Date date;
    private long myID;
    //Don't serialize following:
    private boolean isFirstInGroup;
    private boolean isDateSet = false;
    private String predicate;

    public Task() {
        date = new Date();
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

    public void setID(long myID){
        this.myID = myID;
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

        displayDate = String.valueOf(d) + ' ' + m + ' ' + y;
        this.date = date;
        isDateSet = true;
    }

    public String getDisplayDate() {
        return displayDate;
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

    public boolean isDateSet(){
        return isDateSet;
    }
}

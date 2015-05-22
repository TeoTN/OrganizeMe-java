package pl.piotrstaniow.organizeme.Models;

import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateTimeUtils;

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
    private String displayDate;
    private Date date;
    private long myID;
    private Category category;
    //Don't serialize following:
    private boolean isFirstInGroup;
    private boolean isDateSet = false;
    private boolean isTimeSet = false;
    private String predicate;

    public Task() {
        date = new Date();
        isFirstInGroup = false;
    }

    public static Task deserializeTask(String str) {
        Task task = new Task();
        String[] splitted = str.split("/");

        task.setTaskDesc(splitted[0]);
        task.setID(Integer.parseInt(splitted[1]));

        boolean isTimeSet = false;
        if (splitted[2].contains(" ")) {
            isTimeSet = true;
        }
        Date date = DateTimeUtils.stringToDate(splitted[2]);
        task.setDate(date, isTimeSet);
        Category cat = CategoryAggregator.getInstance().getByName(splitted[3]);
        task.setCategory(cat);

        return task;
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

    public void setDate(Date date, boolean isTimeSet) {
        if(isTimeSet)
            this.isTimeSet = true;
        displayDate = DateTimeUtils.dateToDisplay(date, isTimeSet);
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

    public boolean isTimeSet(){
        return isTimeSet;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public String serialize(){
        String str = taskDesc;
        str +="/" + myID + "/" + DateTimeUtils.dateToString(date, isTimeSet)+"/"+category.getName();
        return str;
    }
}

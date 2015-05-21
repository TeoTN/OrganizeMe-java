package pl.piotrstaniow.organizeme;

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
    private long myID;
    private static long lastID = 0;

    public Task(String taskDesc) {
        this.taskDesc = taskDesc;
        myID = lastID++;
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
    }

    public long getID() {
        return myID;
    }

    public String getTaskDate() {
        return taskDate;
    }
}

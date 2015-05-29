package pl.piotrstaniow.organizeme.Models;

/**
 * Created by piotr on 29.05.15.
 */
public class NotificationItem {
    private long notif_id;
    private long task_id;
    private String type;

    public NotificationItem() {
    }

    public void setNotifID(long notif_id) {
        this.notif_id = notif_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTaskID() {
        return task_id;
    }

    public void setTaskID(long task_id) {
        this.task_id = task_id;
    }

    public long getTaskId() {
        return task_id;
    }

    public String getType() {
        return type;
    }
}

package pl.piotrstaniow.organizeme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Models.NotificationItem;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by piotr on 29.05.15.
 */
public class NotificationCreator {
    private static final int REQUEST_CODE = 17;
    private NotificationItem notificationItem;
    private Context context;

    public NotificationCreator(Context context) {
        notificationItem = new NotificationItem();
        this.context = context;
    }

    private void setNotifID(long notif_id) {
        notificationItem.setNotifID(notif_id);
    }

    public void setTaskID(long task_id) {
        notificationItem.setTaskID(task_id);
    }

    public void setType(String type) {
        notificationItem.setType(type);
    }

    public void delegate(int time) {
        Task task = TaskAggregator.getInstance().filter(new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                return task.getID() == notificationItem.getTaskID();
            }
        }).get(0);

        if (!task.isTimeSet()) {
            Toast.makeText(context, "Time for the task wasn't set", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(context, TaskAlarmReceiver.class);
        intent.putExtra(TaskAlarmReceiver.TASK_ID, task.getID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);


        Calendar calendar = Calendar.getInstance();
        Date alarm_time = task.getDate();
        calendar.setTime(alarm_time);
        calendar.add(Calendar.SECOND, time);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public NotificationItem create() {
        return notificationItem;
    }
}

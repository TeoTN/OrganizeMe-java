package pl.piotrstaniow.organizeme;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;

/**
 * Created by piotr on 29.05.15.
 */
public class TaskAlarmReceiver extends BroadcastReceiver {
    public static final String TASK_ID = "pl.piotrstaniow.TaskAlarmReceiver.TASK_ID";

    public void onReceive(Context context, Intent intent) {
        final long task_id = intent.getLongExtra(TASK_ID, 0);

        Task task = TaskAggregator.getInstance().filter(new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                return task.getID() == task_id;
            }
        }).get(0);


        // try here

        // prepare intent which is triggered if the
        // notification is selected
        /*
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        */
        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new NotificationCompat.Builder(context)
                .setContentTitle(task.getTaskDesc())
                .setContentText(task.getDisplayDate())
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        //.setContentIntent(pIntent)
                .setAutoCancel(true)
                        //.addAction(R.drawable.icon, "Call", pIntent)
                        //.addAction(R.drawable.icon, "More", pIntent)
                        //.addAction(R.drawable.icon, "And more", pIntent)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);


    }
}

package pl.piotrstaniow.organizeme.Models;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotr on 29.05.15.
 */
public class NotificationAggregator implements ItemAggregator<NotificationItem> {
    private static NotificationAggregator instance = null;
    private List<NotificationItem> notificationItemList;

    private NotificationAggregator() {
        notificationItemList = new ArrayList<>();
        /*localQueryManager = LocalQueryManager.getInstance();
        localQueryManager.openWritable();
        taskList = localQueryManager.getAllTasks();
        localQueryManager.close();*/
    }

    public static NotificationAggregator getInstance() {
        if (instance == null) {
            synchronized (TaskAggregator.class) {
                if (instance == null) {
                    instance = new NotificationAggregator();
                }
            }
        }
        return instance;
    }


    @Override
    public void add(NotificationItem newItem) {
        notificationItemList.add(newItem);
    }

    @Override
    public void remove(NotificationItem item) {
        notificationItemList.remove(item);
    }

    @Override
    public void update(NotificationItem item) {

    }

    @Override
    public int getSize() {
        return notificationItemList.size();
    }

    @Override
    public NotificationItem getItem(int i) {
        return notificationItemList.get(i);
    }

    @Override
    public List<NotificationItem> filter(Predicate<NotificationItem> predicate) {
        return null;
    }

    @Override
    public List<NotificationItem> getAll() {
        return notificationItemList;
    }
}

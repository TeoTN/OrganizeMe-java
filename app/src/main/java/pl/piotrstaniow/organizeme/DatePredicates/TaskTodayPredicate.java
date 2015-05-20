package pl.piotrstaniow.organizeme.DatePredicates;

import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by piotr on 17.05.15.
 */
public class TaskTodayPredicate implements Predicate<Task> {
    @Override
    public boolean apply(Task task) {
        Calendar calendar = Calendar.getInstance();
        Date now = TaskUtils.cutTime(calendar.getTime());
        Date tdate = TaskUtils.cutTime(task.getDate());
        return (tdate.compareTo(now)==0);
    }

    public String toString() {
        return "Today";
    }
}

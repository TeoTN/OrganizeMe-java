package pl.piotrstaniow.organizeme.DatePredicates;

import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by piotr on 17.05.15.
 */
public class TaskFurtherPredicate implements Predicate<Task> {
    @Override
    public boolean apply(Task task) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 8);
        Date then = DateTimeUtils.cutTime(calendar.getTime());
        Date tdate = DateTimeUtils.cutTime(task.getDate());
        return (tdate.compareTo(then)>=0);
    }

    public String toString() {
        return "Further tasks";
    }
}

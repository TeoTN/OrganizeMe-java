package pl.piotrstaniow.organizeme.DatePredicates;

import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created by piotr on 17.05.15.
 */
public class TaskNextWeekPredicate implements Predicate<Task> {
    @Override
    public boolean apply(Task task) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = TaskUtils.cutTime(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 8);
        Date then = TaskUtils.cutTime(calendar.getTime());
        Date tdate = TaskUtils.cutTime(task.getDate());
        return (tdate.after(tomorrow) && tdate.before(then));
    }

    public String toString() {
        return "Next 7 days";
    }
}

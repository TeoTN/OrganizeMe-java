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
 * Created on 17.05.15.
 */
public class TaskOverduePredicate implements Predicate<Task> {
    @Override
    public boolean apply(Task task) {
        Calendar calendar = Calendar.getInstance();
        Date now = TaskUtils.cutTime(calendar.getTime());
        Date tdate = TaskUtils.cutTime(task.getDate());
        return (tdate.compareTo(now)<0);
    }

    public String toString() {
        return "Overdue";
    }
}

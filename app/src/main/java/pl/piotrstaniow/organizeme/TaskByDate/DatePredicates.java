package pl.piotrstaniow.organizeme.TaskByDate;

import android.util.Log;
import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by piotr on 16.05.15.
 */
public class DatePredicates {
    private List<Predicate> predicateList;
    public DatePredicates()
    {
        predicateList = new ArrayList<>();
        addPastPredicate();
        addTodayPredicate();
        addTomorrowPredicate();
        addNextWeekPredicate();
        addOtherPredicate();
    }

    private void addNextWeekPredicate() {
        predicateList.add(new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date tomorrow = cutTime(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                Date then = cutTime(calendar.getTime());
                Date tdate = cutTime(task.getDate());
                return (tdate.after(tomorrow) && tdate.before(then) );
            }

            public String toString() {
                return "Next 7 days";
            }
        });
    }

    private void addPastPredicate() {
        predicateList.add(new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                Calendar calendar = Calendar.getInstance();
                Date now = cutTime(calendar.getTime());
                Date tdate = cutTime(task.getDate());
                return (tdate.compareTo(now)<0);
            }

            public String toString() {
                return "Overdue";
            }
        });
    }

    private void addOtherPredicate() {
        predicateList.add(new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 8);
                Date then = cutTime(calendar.getTime());
                Date tdate = cutTime(task.getDate());
                return (tdate.compareTo(then)>=0);
            }

            public String toString() {
                return "Later in the year";
            }
        });
    }

    private void addTomorrowPredicate() {
        predicateList.add(new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date tomorrow = cutTime(calendar.getTime());
                Date tdate = cutTime(task.getDate());
                return (tdate.compareTo(tomorrow)==0);
            }

            public String toString() {
                return "Tomorrow";
            }
        });
    }

    private void addTodayPredicate() {
        predicateList.add(new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                Calendar calendar = Calendar.getInstance();
                Date now = cutTime(calendar.getTime());
                Date tdate = cutTime(task.getDate());
                return (tdate.compareTo(now)==0);
            }

            public String toString() {
                return "Today";
            }
        });
    }

    private Date cutTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public List<Predicate> getPredicateList() {
        return predicateList;
    }
}

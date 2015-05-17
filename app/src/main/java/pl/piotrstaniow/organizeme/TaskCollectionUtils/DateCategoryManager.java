package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import android.support.v4.util.ArrayMap;
import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.DatePredicates.*;
import pl.piotrstaniow.organizeme.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DateCategoryManager implements TaskCategoryManager {
    private List<Predicate<Task>> predicateList;
    private ArrayMap<Predicate<Task>, List<Task>> categorized;
    public DateCategoryManager()
    {
        predicateList = new ArrayList<>(6);
        addPredicates();
    }

    private void addPredicates() {
        predicateList.add(0, new TaskOverduePredicate());
        predicateList.add(1, new TaskYesterdayPredicate());
        predicateList.add(2, new TaskTodayPredicate());
        predicateList.add(3, new TaskTomorrowPredicate());
        predicateList.add(4, new TaskNextWeekPredicate());
        predicateList.add(5, new TaskFurtherPredicate());
    }

    @Override
    public Iterator getTasksCategorizedIterator() {
        categorized = new ArrayMap<>();
        TaskAggregator aggregator = TaskAggregator.getInstance();
        List<Task> filtered;
        for (Predicate<Task> predicate : predicateList)
        {
            filtered = aggregator.filter(predicate);
            categorized.put(predicate, filtered);
        }
        return categorized.entrySet().iterator();
    }
}

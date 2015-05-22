package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import android.support.v4.util.ArrayMap;
import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.CategoryPredicates.CategoryPredicateFactory;
import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskByCategoryManager implements TaskCategoryManager {
    private List<Predicate<Task>> predicateList;
    private ArrayMap<Predicate<Task>, List<Task>> categorized;
    private CategoryAggregator categoryAggregator;
    private CategoryPredicateFactory predicateFactory;

    public TaskByCategoryManager() {
        categoryAggregator = CategoryAggregator.getInstance();
        predicateFactory = CategoryPredicateFactory.getInstance();
        predicateList = new ArrayList<>();
        addPredicates();
    }

    private void addPredicates() {
        for (Category category : categoryAggregator.getAll()) {
            Predicate p = predicateFactory.getPredicateForCategory(category);
            predicateList.add(p);
        }
    }

    @Override
    public Iterator getTasksCategorizedIterator() {
        categorized = new ArrayMap<>();
        TaskAggregator aggregator = TaskAggregator.getInstance();
        List<Task> filtered;
        for (Predicate<Task> predicate : predicateList) {
            filtered = aggregator.filter(predicate);
            categorized.put(predicate, filtered);
        }
        return categorized.entrySet().iterator();
    }
}

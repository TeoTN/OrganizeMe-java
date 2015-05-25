package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import pl.piotrstaniow.organizeme.DatePredicates.*;

public class DateGroupProvider extends AbstractTaskGroupProvider {
    public DateGroupProvider() {
        super();
        addPredicates();
    }

    private void addPredicates() {
        predicateList.add(new TaskOverduePredicate());
        predicateList.add(new TaskYesterdayPredicate());
        predicateList.add(new TaskTodayPredicate());
        predicateList.add(new TaskTomorrowPredicate());
        predicateList.add(new TaskNextWeekPredicate());
        predicateList.add(new TaskFurtherPredicate());
    }
}

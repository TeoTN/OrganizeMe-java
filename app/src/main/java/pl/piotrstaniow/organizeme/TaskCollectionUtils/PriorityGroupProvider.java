package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import pl.piotrstaniow.organizeme.DatePredicates.PriorityPredicateFactory;

/**
 * Created by oszka on 29.05.15.
 */
public class PriorityGroupProvider extends AbstractTaskGroupProvider {
    public PriorityGroupProvider() {
        super();
        addPredicates();
    }

    private void addPredicates() {
        PriorityPredicateFactory priorityPredicateFactory = PriorityPredicateFactory.getInstance();
        for (int i = 1; i <= 4; ++i) {
            predicateList.add(priorityPredicateFactory.getPredicateForPriority("priority_" + i));
        }
        predicateList.add(priorityPredicateFactory.getPredicateWithoutPriority());
    }
}

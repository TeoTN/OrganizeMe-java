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

        predicateList.add(priorityPredicateFactory.getPredicateForPriority("High"));
        predicateList.add(priorityPredicateFactory.getPredicateForPriority("Normal"));
        predicateList.add(priorityPredicateFactory.getPredicateForPriority("Low"));

        predicateList.add(priorityPredicateFactory.getPredicateWithoutPriority());
    }
}

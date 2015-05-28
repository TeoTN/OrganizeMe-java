package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import pl.piotrstaniow.organizeme.DatePredicates.SearchPredicateFactory;
import pl.piotrstaniow.organizeme.DatePredicates.TaskYesterdayPredicate;

/**
 * Created by oszka on 28.05.15.
 */
public class QueryGroupProvider extends AbstractTaskGroupProvider {
    private String query;

    public QueryGroupProvider() {
        super();
        query = "";
        addPredicates();
    }
    public QueryGroupProvider(String query){
        super();
        this.query = query;
        addPredicates();
    }

    private void addPredicates() {
        SearchPredicateFactory searchPredicateFactory = SearchPredicateFactory.getInstance();
        predicateList.add(searchPredicateFactory.getPredicateForLabelQuery(query));
        predicateList.add(searchPredicateFactory.getPredicateForNameQuery(query));

    }
}

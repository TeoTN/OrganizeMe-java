package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.CategoryPredicates.CategoryPredicateFactory;
import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.CategoryAggregator;

import java.util.ArrayList;

public class CategoryGroupProvider extends AbstractTaskGroupProvider {
    private CategoryAggregator categoryAggregator;
    private CategoryPredicateFactory predicateFactory;

    public CategoryGroupProvider() {
        super();
        categoryAggregator = CategoryAggregator.getInstance();
        predicateFactory = CategoryPredicateFactory.getInstance();
        addPredicates();
    }

    public void refresh() {
        predicateList = new ArrayList<>();
        addPredicates();
    }

    private void addPredicates() {
        for (Category category : categoryAggregator.getAll()) {
            Predicate p = predicateFactory.getPredicateForCategory(category);
            predicateList.add(p);
        }
    }
}

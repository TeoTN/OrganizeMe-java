package pl.piotrstaniow.organizeme.CategoryPredicates;

import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.Task;

/**
 * Created by piotr on 22.05.15.
 */
public class CategoryPredicateFactory {
    private static CategoryPredicateFactory ourInstance = new CategoryPredicateFactory();

    private CategoryPredicateFactory() {
    }

    public static CategoryPredicateFactory getInstance() {
        return ourInstance;
    }

    public Predicate<Task> getPredicateForCategory(final Category category) {
        return new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                return task.getCategory().equals(category);
            }

            public String toString() {
                return category.toString();
            }
        };
    }
}

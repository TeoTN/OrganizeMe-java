package pl.piotrstaniow.organizeme.DatePredicates;

import com.android.internal.util.Predicate;

import java.util.List;

import pl.piotrstaniow.organizeme.Models.Label;
import pl.piotrstaniow.organizeme.Models.Task;

/**
 * Created by oszka on 28.05.15.
 */
public class SearchPredicateFactory {
    private static SearchPredicateFactory ourInstance = new SearchPredicateFactory();

    public static SearchPredicateFactory getInstance() {
        return ourInstance;
    }

    private SearchPredicateFactory() {
    }

    public Predicate<Task> getPredicateForNameQuery(final String searchQuery) {
        return new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                return task.getTaskDesc().toLowerCase().contains(searchQuery.toLowerCase());
            }

            public String toString() {
                return "Found by name";
            }
        };
    }

    public Predicate<Task> getPredicateForLabelQuery(final String searchQuery) {
        return new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                List<Label> labels = task.getLabels();
                for (Label l: labels){
                    if(l.getName().toLowerCase().contains(searchQuery.toLowerCase())){
                        return true;
                    }
                }
                return false;
            }

            public String toString() {
                return "Found by label";
            }
        };
    }
}

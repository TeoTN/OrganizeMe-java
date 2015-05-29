package pl.piotrstaniow.organizeme.DatePredicates;

import com.android.internal.util.Predicate;

import java.util.List;

import pl.piotrstaniow.organizeme.Models.Label;
import pl.piotrstaniow.organizeme.Models.Task;

/**
 * Created by oszka on 29.05.15.
 */
public class PriorityPredicateFactory {
    private static PriorityPredicateFactory ourInstance = new PriorityPredicateFactory();

    public static PriorityPredicateFactory getInstance() {
        return ourInstance;
    }

    private PriorityPredicateFactory() {
    }
    public Predicate<Task> getPredicateForPriority(final String priority) {
        return new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                List<Label> labels = task.getLabels();
                for(Label l: labels){
                    if(l.getName().equals(priority))
                        return true;
                }
                return false;
            }

            public String toString() {
                return priority;
            }
        };
    }
    public Predicate<Task> getPredicateWithoutPriority() {
        return new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                List<Label> labels = task.getLabels();
                boolean containsPriority = false;
                for(Label l: labels){
                    String name = l.getName();
                    if(name.equals("High") || name.equals("Normal") || name.equals("Low"))
                        containsPriority = true;
                }
                return !containsPriority;
            }

            public String toString() {
                return "Without priority";
            }
        };
    }
}

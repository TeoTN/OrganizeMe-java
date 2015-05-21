package pl.piotrstaniow.organizeme.CategoryPredicates;

import com.android.internal.util.Predicate;

import pl.piotrstaniow.organizeme.Models.Category;

/**
 * Created by oszka on 21.05.15.
 */
public class CategoryAllPredicate implements Predicate<Category> {
    @Override
    public boolean apply(Category category) {
        return  true;
    }

    public String toString(){
        return "All";
    }
}

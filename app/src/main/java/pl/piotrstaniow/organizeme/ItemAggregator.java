package pl.piotrstaniow.organizeme;

import com.android.internal.util.Predicate;

import java.util.List;

import pl.piotrstaniow.organizeme.Models.Category;

/**
 * Created by Slawomir on 2015-05-20.
 */
public interface ItemAggregator<ItemType> {
    void add(ItemType newTask);

    void remove(ItemType task);

    void update(ItemType createdCategory);

    int getSize();

    ItemType getItem(int i);

    List<ItemType> filter(Predicate<ItemType> predicate);
}

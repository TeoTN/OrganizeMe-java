package pl.piotrstaniow.organizeme;

import android.widget.ListAdapter;

/**
 * Created by Sławomir on 2015-05-20.
 */
public interface ItemListAdapter<ItemType> extends ListAdapter {
    int getCount();

    ItemType getItem(int id);

    void remove(ItemType item);

    void add(ItemType newTask);
}

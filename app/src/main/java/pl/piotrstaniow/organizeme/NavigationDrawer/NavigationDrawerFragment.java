package pl.piotrstaniow.organizeme.NavigationDrawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import pl.piotrstaniow.organizeme.R;

import java.util.ArrayList;

/**
 * Fragment that contains navigation drawer.
 * Use the {@link NavigationDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationDrawerFragment extends Fragment {
    private ArrayList<NavigationDrawerItem> summary_items, options_items, additional_items;
    private NavigationDrawerItem tasks_done_item, tasks_today_item, tasks_future_item;
    private NavigationDrawerItem pending_item, categories_item, priority_item;
    private ListView summary_list, options_list, additional_list;
    private NavigationDrawerAdapter summary_adapter, options_adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavigationDrawerFragment.
     */
    public static Fragment newInstance() {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        return fragment;
    }

    public NavigationDrawerFragment() {
        summary_items = get_summary_items();
        options_items = get_options_items();
        additional_items = get_additional_items();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summary_adapter = new NavigationDrawerAdapter(getActivity(), summary_items);
        options_adapter = new NavigationDrawerAdapter(getActivity(), options_items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        summary_list = (ListView) view.findViewById(R.id.summary_items);
        summary_list.setAdapter(summary_adapter);
        options_list = (ListView) view.findViewById(R.id.options_items);
        options_list.setAdapter(options_adapter);
        return view;
    }

    private ArrayList<NavigationDrawerItem> get_additional_items() {
        ArrayList<NavigationDrawerItem> items = new ArrayList();

        return items;
    }

    private ArrayList<NavigationDrawerItem> get_options_items() {
        ArrayList<NavigationDrawerItem> items = new ArrayList();
        pending_item = new NavigationDrawerItem(R.drawable.ic_pending, "Pending tasks", 0);
        categories_item = new NavigationDrawerItem(R.drawable.ic_categories, "Categories", 0);
        priority_item = new NavigationDrawerItem(R.drawable.ic_priority, "Ordered by priority", 0);
        items.add(pending_item);
        items.add(categories_item);
        items.add(priority_item);
        return items;
    }

    private ArrayList<NavigationDrawerItem> get_summary_items() {
        ArrayList<NavigationDrawerItem> items = new ArrayList();
        tasks_done_item = new NavigationDrawerItem(R.drawable.ic_done_all, "0 tasks done"); //TODO change 0 to real number
        tasks_today_item = new NavigationDrawerItem(R.drawable.ic_today, "0 tasks today");
        tasks_future_item = new NavigationDrawerItem(R.drawable.ic_future, "0 tasks in future");
        items.add(tasks_done_item);
        items.add(tasks_today_item);
        items.add(tasks_future_item);
        return items;
    }

    public void refreshTasksDone(long number) {
        tasks_done_item.text = number + " tasks done";
        if (summary_adapter != null)
            summary_adapter.notifyDataSetChanged();
    }
}

package pl.piotrstaniow.organizeme;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Piotr Staniów on 21.05.15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {
    private Context context;
    private String[] drawerOptions;

    public DrawerItemClickListener(TasksActivity context) {
        this.context = context;
        drawerOptions = context.getDrawerOptions();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        if (drawerOptions[position].equals("All tasks")) {
            fragment = new TaskListFragment();
        } else if (drawerOptions[position].equals("Categories")) {
            fragment = new CategoriesFragment();
        }

        FragmentManager fragmentManager = ((TasksActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        ((TasksActivity) context).setDrawerItemChecked(position, true);
        ((TasksActivity) context).setTitle(drawerOptions[position]);
        ((TasksActivity) context).closeDrawer();
    }
}

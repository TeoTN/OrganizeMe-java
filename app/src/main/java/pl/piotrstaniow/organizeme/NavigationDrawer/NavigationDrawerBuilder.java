package pl.piotrstaniow.organizeme.NavigationDrawer;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;
import pl.piotrstaniow.organizeme.R;
import pl.piotrstaniow.organizeme.TaskListFragment;

import java.util.ArrayList;

/**
 * Created by piotr on 11.09.15.
 */
public class NavigationDrawerBuilder {
    ActionBarActivity context;
    Fragment fragment;

    public NavigationDrawerBuilder(ActionBarActivity context) {
        this.context = context;
        loadFragment();
    }


    private void loadFragment() {
        fragment = NavigationDrawerFragment.newInstance();

        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.navigation_drawer, fragment)
                .commit();
    }

    public void refreshTasksDone() {
        LocalDbHelper.createInstance(context);
        LocalQueryManager.getInstance().openWritable();
        long done_count = LocalQueryManager.getInstance().countArchivedTasks();
        ((NavigationDrawerFragment) fragment).refreshTasksDone(done_count);
        LocalQueryManager.getInstance().close();
    }
}

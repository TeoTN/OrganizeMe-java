package pl.piotrstaniow.organizeme.NavigationDrawer;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import pl.piotrstaniow.organizeme.R;

/**
 * Created by piotr on 11.09.15.
 */
public class NavigationDrawerBuilder {
    Fragment fragment;
    ActionBarActivity context;

    private static boolean isLoaded = false;
    private static NavigationDrawerBuilder instance = new NavigationDrawerBuilder();

    public static NavigationDrawerBuilder getInstance(Context context) {
        instance.context = (ActionBarActivity) context;
        if (!isLoaded) {
            instance.loadFragment();
        }
        return instance;
    }

    private NavigationDrawerBuilder() {

    }


    private void loadFragment() {
        instance.fragment = NavigationDrawerFragment.newInstance();

        FragmentManager fragmentManager = instance.context.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.navigation_drawer, instance.fragment)
                .commit();
        isLoaded = true;
    }

    public void notifyValuesChanged() {
        ((NavigationDrawerFragment) instance.fragment).notifyValuesChanged();
    }
}

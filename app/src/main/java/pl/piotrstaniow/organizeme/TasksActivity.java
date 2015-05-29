package pl.piotrstaniow.organizeme;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;
import pl.piotrstaniow.organizeme.NavigationDrawer.DrawerItemClickListener;


public class TasksActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private String[] drawerOptions;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private FrameLayout contentFrame;
    private TextView drawerInfo;
    private ArrayAdapter drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent ishintent = new Intent(this, GPSPositionCheckingService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, ishintent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*2, pintent);

        setContentView(R.layout.activity_tasks);
        LocalDbHelper.createInstance(this);

        preloadContent();

        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerInfo = (TextView) findViewById(R.id.done_tasks);
        refreshArchivedTaskInfo();
        drawerAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerOptions);

        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener(this));

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }

    public void refreshArchivedTaskInfo() {
        LocalDbHelper.createInstance(this);
        LocalQueryManager.getInstance().openWritable();
        drawerInfo.setText(getResources().getText(R.string.done_tasks_title) + ": " + LocalQueryManager.getInstance().countArchivedTasks());
        LocalQueryManager.getInstance().close();
        if (drawerAdapter != null)
            drawerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void preloadContent() {
        contentFrame = (FrameLayout) findViewById(R.id.content_frame);
        Fragment fragment = TaskListFragment.newInstance(TaskListFragment.GROUP_BY_DATE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView( menu.findItem(R.id.action_search));


        searchView.setOnQueryTextListener(this);

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public String[] getDrawerOptions() {
        return drawerOptions;
    }

    public void setDrawerItemChecked(int position, boolean b) {
        drawerList.setItemChecked(position, b);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    public void prepareFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {


            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Fragment fragment = TaskListFragment.newInstance(TaskListFragment.GROUP_BY_QUERY,s);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("searchResult")
                .commit();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}

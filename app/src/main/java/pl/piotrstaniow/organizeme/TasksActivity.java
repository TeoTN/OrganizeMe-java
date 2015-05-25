package pl.piotrstaniow.organizeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;


public class TasksActivity extends ActionBarActivity {
    private String[] drawerOptions;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private FrameLayout contentFrame;
    private TextView drawerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
        }

        preloadContent();

        drawerOptions = getResources().getStringArray(R.array.drawer_options);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerInfo = (TextView) findViewById(R.id.done_tasks);
        refreshArchivedTaskInfo();

        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerOptions));
        drawerList.setOnItemClickListener(new DrawerItemClickListener(this));
    }

    private void refreshArchivedTaskInfo() {
        LocalDbHelper.createInstance(this);
        LocalQueryManager.getInstance().openWritable();
        drawerInfo.setText(getResources().getText(R.string.done_tasks_title) + ": " + LocalQueryManager.getInstance().countArchivedTasks());
        LocalQueryManager.getInstance().close();
    }

    private void preloadContent() {
        contentFrame = (FrameLayout) findViewById(R.id.content_frame);
        Fragment fragment = new TaskListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void createCategoriesActivity() {
        Intent intent = new Intent(this, CategoriesFragment.class);
        startActivity(intent);
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
}

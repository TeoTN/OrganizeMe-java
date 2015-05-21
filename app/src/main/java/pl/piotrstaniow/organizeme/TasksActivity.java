package pl.piotrstaniow.organizeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import pl.piotrstaniow.organizeme.TaskByDate.TaskListAdapter;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateCategoryManager;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskListAdapter;


public class TasksActivity extends ActionBarActivity
    implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final String[] drawerOptions ={"All tasks", "Today", "Next week", "Projects", "Labels", "Categories"};
    private TaskListAdapter taskListAdapter;
    private FloatingActionButton newTaskBtn;
    private FloatingActionsMenu floatingMenu;
    private ListView taskListView;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        LocalDbHelper.createInstance(this);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
        }

        floatingMenu = (FloatingActionsMenu) findViewById(R.id.floating_menu);

        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerOptions));
        drawerList.setOnItemClickListener(this);

        taskListView = (ListView) findViewById(R.id.todoList);
        taskListAdapter = new TaskListAdapter(this, new DateCategoryManager(), TaskAggregator.getInstance());

        newTaskBtn = (FloatingActionButton) findViewById(R.id.new_task_btn);
        newTaskBtn.setOnClickListener(this);

        floatingMenu = (FloatingActionsMenu) findViewById(R.id.floating_menu);

        taskListView.setAdapter(taskListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskListAdapter.notifyDataSetChanged();
        taskListView.deferNotifyDataSetChanged();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewTaskActivity(){
        Intent intent = new Intent(this, NewTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == newTaskBtn) {
            createNewTaskActivity();
            floatingMenu.collapse();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == drawerList) {
            if(drawerOptions[position].equals("Categories")) {
                createCategoriesActivity();
            }
        }
    }

    private void createCategoriesActivity() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }
}

package pl.piotrstaniow.organizeme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
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


public class TasksActivity extends ActionBarActivity
    implements NewTaskFragment.OnNewTaskCreatedListener, View.OnClickListener {
    private TaskListAdapter taskListAdapter;
    private final String[] drawerOptions ={"All tasks", "Today", "Next week", "Projects", "Labels"};
    private FloatingActionButton newTaskBtn;
    private FloatingActionsMenu floatingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ListView drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerOptions));

        ListView taskListView = (ListView) findViewById(R.id.todoList);
        taskListAdapter = new TaskListAdapter(this);

        newTaskBtn = (FloatingActionButton) findViewById(R.id.new_task_btn);
        newTaskBtn.setOnClickListener(this);

        floatingMenu = (FloatingActionsMenu) findViewById(R.id.floating_menu);

        taskListView.setAdapter(taskListAdapter);
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Task item = (Task) parent.getItemAtPosition(position);
                taskListAdapter.remove(item);
                return true;
            }
        });
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

    private void createNewTaskFragment() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        DialogFragment newFragment = new NewTaskFragment();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        ft.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();

        floatingMenu.setVisibility(View.INVISIBLE);
        floatingMenu.collapse();
    }


    @Override
    public void onNewTaskCreated(Task newTask) {
        taskListAdapter.add(newTask);
        taskListAdapter.notifyDataSetChanged();
        floatingMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == newTaskBtn) {
            createNewTaskFragment();
        }
    }
}

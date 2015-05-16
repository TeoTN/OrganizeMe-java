package pl.piotrstaniow.organizeme;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


public class TasksActivity extends ActionBarActivity
    implements NewTaskFragment.OnNewTaskCreatedListener, View.OnClickListener {
    private TaskListAdapter taskListAdapter;
    private final String[] drawerOptions ={"All tasks", "Today", "Next week", "Projects", "Labels"};
    private FloatingActionButton newTaskBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerOptions));

        ListView taskListView = (ListView) findViewById(R.id.todoList);
        taskListAdapter = new TaskListAdapter(this);

        newTaskBtn = (FloatingActionButton) findViewById(R.id.new_task_btn);
        newTaskBtn.setOnClickListener(this);


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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = new NewTaskFragment();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onNewTaskCreated(Task newTask) {
        taskListAdapter.add(newTask);
        taskListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == newTaskBtn) {
            createNewTaskFragment();
        }
    }
}

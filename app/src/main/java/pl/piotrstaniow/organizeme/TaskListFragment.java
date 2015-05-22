package pl.piotrstaniow.organizeme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.cocosw.bottomsheet.BottomSheet;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import pl.piotrstaniow.organizeme.DatabaseUtils.LocalDbHelper;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateCategoryManager;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskListAdapter;


public class TaskListFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemLongClickListener {
    private TaskListAdapter taskListAdapter;
    private FloatingActionButton newTaskBtn;
    private FloatingActionsMenu floatingMenu;
    private ListView taskListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        floatingMenu = (FloatingActionsMenu) view.findViewById(R.id.floating_menu);

        taskListView = (ListView) view.findViewById(R.id.todoList);
        taskListAdapter = new TaskListAdapter(getActivity(), new DateCategoryManager(), TaskAggregator.getInstance());

        newTaskBtn = (FloatingActionButton) view.findViewById(R.id.new_task_btn);
        newTaskBtn.setOnClickListener(this);

        floatingMenu = (FloatingActionsMenu) view.findViewById(R.id.floating_menu);

        taskListView.setAdapter(taskListAdapter);
        taskListView.setOnItemLongClickListener(this);
        return view;
    }

    public void onResume() {
        super.onResume();
        taskListAdapter.notifyDataSetChanged();
        taskListView.deferNotifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == newTaskBtn) {
            createNewTaskActivity();
            floatingMenu.collapse();
        }
    }

    private void createNewTaskActivity() {
        Intent intent = new Intent(getActivity(), NewTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        final TaskListAdapter taskListAdapter = (TaskListAdapter) adapterView.getAdapter();
        final Task task = taskListAdapter.getItem(position);
        final TaskAggregator ta = TaskAggregator.getInstance();

        new BottomSheet.Builder(getActivity(), R.style.BottomSheet_Dialog)
                .title("Actions")
                .grid() // <-- important part
                .sheet(R.menu.menu_task_action)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == R.id.task_edit) {
                            Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                            intent.putExtra("task",task.serialize());
                            startActivity(intent);
                            taskListAdapter.notifyDataSetChanged();
                        } else if (which == R.id.task_delete) {
                            ta.remove(task);
                            taskListAdapter.notifyDataSetChanged();

                        } else if (which == R.id.task_notif) {
                            pickNotif();
                        }
                    }
                }).show();
        return true;
    }

    private void pickNotif() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("picknotif");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = new NotificationFragment();
        newFragment.show(ft, "picknotif");
    }
}

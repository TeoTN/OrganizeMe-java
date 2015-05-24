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
import android.widget.ExpandableListView;
import com.cocosw.bottomsheet.BottomSheet;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.AbstractTaskGroupProvider;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.CategoryGroupProvider;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateGroupProvider;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskListAdapter;


public class TaskListFragment extends Fragment implements View.OnClickListener, ExpandableListView.OnChildClickListener {
    public static final int GROUP_BY_DATE = 13;
    public static final int GROUP_BY_CATEGORY = 14;
    private static final String GROUP_METHOD = "GroupingMethodParameter";
    private int groupingMethod;

    private AbstractTaskGroupProvider manager;
    private TaskListAdapter taskListAdapter;
    private FloatingActionButton newTaskBtn, newCatBtn;
    private FloatingActionsMenu floatingMenu;
    private ExpandableListView taskList;

    public TaskListFragment() {

    }

    public static TaskListFragment newInstance(int method) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt(GROUP_METHOD, method);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupingMethod = getArguments().getInt(GROUP_METHOD);
        }
        switch (groupingMethod) {
            case GROUP_BY_DATE:
                manager = new DateGroupProvider();
                break;
            case GROUP_BY_CATEGORY:
                manager = new CategoryGroupProvider();
                break;
            default:
                manager = new DateGroupProvider();
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        floatingMenu = (FloatingActionsMenu) view.findViewById(R.id.floating_menu);

        newTaskBtn = (FloatingActionButton) view.findViewById(R.id.new_task_btn);
        newTaskBtn.setOnClickListener(this);
        newCatBtn = (FloatingActionButton) view.findViewById(R.id.new_category_btn);
        newCatBtn.setOnClickListener(this);

        taskListAdapter = new TaskListAdapter(getActivity(), manager);

        taskList = (ExpandableListView) view.findViewById(R.id.task_list);
        taskList.setAdapter(taskListAdapter);
        taskList.setOnChildClickListener(this);
        taskList.setGroupIndicator(null);

        return view;
    }

    public void onResume() {
        super.onResume();
        taskListAdapter.refresh();
    }

    @Override
    public void onClick(View view) {
        if (view == newTaskBtn) {
            createNewTaskActivity();
            floatingMenu.collapse();
        } else if (view == newCatBtn) {
            createNewCategoryActivity();
            floatingMenu.collapse();
        }
    }

    private void createNewCategoryActivity() {
        Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
        startActivity(intent);
    }

    private void createNewTaskActivity() {
        Intent intent = new Intent(getActivity(), NewTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        final TaskListAdapter taskListAdapter = (TaskListAdapter) expandableListView.getExpandableListAdapter();
        final Task task = taskListAdapter.getChild(i, i1);
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
                            intent.putExtra("task", task.serialize());
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
        return false;
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

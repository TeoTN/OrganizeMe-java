package pl.piotrstaniow.organizeme.TaskByDate;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.R;
import pl.piotrstaniow.organizeme.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OrganizeMe
 * <p/>
 * Author: Piotr Staniów
 * Email: staniowp@gmail.com
 * Created: 12.05.15
 */
public class TaskListAdapter extends BaseAdapter {
    private Context ctx;
    private TaskAggregator aggregator;
    private DatePredicates predicates;
    private List<List<Task>> tasksByPredicate;

    public TaskListAdapter(Context ctx) {
        this.ctx = ctx;
        aggregator = TaskAggregator.getInstance();
        loadTasks();
    }

    private void loadTasks() {
        tasksByPredicate = new ArrayList<>();
        predicates = new DatePredicates();
        for (Predicate<Task> predicate : predicates.getPredicateList()) {
            tasksByPredicate.add(aggregator.filter(predicate));
        }
    }

    @Override
    public int getCount() {
        return aggregator.getSize();
    }

    @Override
    public Task getItem(int i) {
        int ds = 0;
        int p = 0;
        Task q = aggregator.getItem(0);
        for (List<Task> group : tasksByPredicate) {
            if (i >= ds + group.size()) {
                ds += group.size();
                ++p;
            }
            else {
                Task t = group.get(i - ds);
                if (t == group.get(0)) {
                    String groupName = predicates.getPredicateList().get(p).toString();
                    t.setPredicate(groupName);
                    t.setIsFirstInGroup(true);
                }

                return t;
            }
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View returnView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            returnView =  inflater.inflate(R.layout.task_layout, viewGroup, false);
        } else {
            returnView = view;
        }

        TextView text1 = (TextView) returnView.findViewById(R.id.text1);
        TextView text2 = (TextView) returnView.findViewById(R.id.text2);

        Task task = getItem(i);
        TextView separator = (TextView) returnView.findViewById(R.id.separator);
        if (task.isFirstInGroup()) {
            separator.setVisibility(View.VISIBLE);
            separator.setText(task.getPredicate());
        }
        else {
            separator.setVisibility(View.GONE);
        }

        text1.setText(task.getTaskDesc());
        text2.setText(task.getTaskDate());

        return returnView;
    }

    public void remove(Task item) {
        aggregator.removeTask(item);
        loadTasks();
        notifyDataSetChanged();
    }

    public void add(Task newTask) {
        aggregator.addTask(newTask);
        loadTasks();
        notifyDataSetChanged();
    }
}

package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.Predicate;

import pl.piotrstaniow.organizeme.Models.ItemAggregator;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;
import pl.piotrstaniow.organizeme.R;
import pl.piotrstaniow.organizeme.Models.Task;

import java.util.Iterator;
import java.util.List;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created: 12.05.15
 */
public class TaskListAdapter extends BaseAdapter implements ItemListAdapter<Task> {
    private Context ctx;
    private ItemAggregator<Task> aggregator;
    private TaskCategoryManager categoryMgr;

    public TaskListAdapter(Context ctx, TaskCategoryManager categoryMgr, TaskAggregator taskAggregator) {
        this.ctx = ctx;
        this.categoryMgr = categoryMgr;
        aggregator = taskAggregator;
    }

    @Override
    public int getCount() {
        return aggregator.getSize();
    }

    @Override
    public Task getItem(int id) {
        int ds = 0;
        int p = 0;
        Iterator groupIterator = categoryMgr.getTasksCategorizedIterator();
        while (groupIterator.hasNext()) {
            ArrayMap.Entry group = (ArrayMap.Entry) groupIterator.next();
            List<Task> tasks = (List<Task>) group.getValue();
            Predicate<Task> predicate = (Predicate<Task>) group.getKey();

            if (id >= ds + tasks.size()) {
                ds += tasks.size();
            }
            else {
                Task task = tasks.get(id - ds);
                if (task == tasks.get(0)) {
                    task.setIsFirstInGroup(true);
                    task.setPredicate(predicate.toString());
                }
                return task;
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
        ImageView label = (ImageView) returnView.findViewById(R.id.category);

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
        text2.setText(task.getDisplayDate());
        //TODO check if color is empty
        label.setBackgroundColor(Integer.parseInt(task.getCategory().getColor()));

        return returnView;
    }

    @Override
    public void remove(Task item) {
        aggregator.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void add(Task newTask) {
        aggregator.add(newTask);
        notifyDataSetChanged();
    }
}

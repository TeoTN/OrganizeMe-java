package pl.piotrstaniow.organizeme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * OrganizeMe
 * <p/>
 * Author: Piotr Staniów
 * Email: staniowp@gmail.com
 * Created: 12.05.15
 */
public class TaskListAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Task> taskList;

    public TaskListAdapter(Context ctx) {
        this.ctx = ctx;
        taskList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Task getItem(int i) {
        return taskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return taskList.get(i).getID();
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

        text1.setText(taskList.get(i).getTaskDesc());
        text2.setText(taskList.get(i).getTaskDate());

        return returnView;
    }

    public void remove(Task item) {
        taskList.remove(item);
        notifyDataSetChanged();
    }

    public void add(Task newTask) {
        taskList.add(newTask);
        notifyDataSetChanged();
    }
}

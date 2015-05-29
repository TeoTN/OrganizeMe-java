package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.R;

import java.util.List;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created: 12.05.15
 */
public class TaskListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private Context ctx;
    private AbstractTaskGroupProvider categoryMgr;

    public TaskListAdapter(Context ctx, AbstractTaskGroupProvider categoryMgr) {
        this.ctx = ctx;
        this.categoryMgr = categoryMgr;
    }

    @Override
    public int getGroupCount() {
        return categoryMgr.getGroupCount();
    }

    @Override
    public int getChildrenCount(int i) {
        return categoryMgr.getTaskCount(i);
    }

    @Override
    public List<Task> getGroup(int i) {
        return categoryMgr.getGroup(i);
    }

    @Override
    public Task getChild(int i, int i1) {
        return getGroup(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return getGroup(i).hashCode();
    }

    @Override
    public long getChildId(int i, int i1) {
        return getChild(i, i1).getID();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean expanded, View view, ViewGroup viewGroup) {
        String title = categoryMgr.getGroupName(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.category_layout, viewGroup, false);
        }
        TextView item = (TextView) view.findViewById(R.id.task_group_title);
        CardView card = (CardView) view.findViewById(R.id.category_card);
        ImageButton edBtn = (ImageButton) view.findViewById(R.id.category_edit);
        ImageButton rmBtn = (ImageButton) view.findViewById(R.id.category_delete);
        edBtn.setFocusable(false);
        rmBtn.setFocusable(false);
        edBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "EDIT", Toast.LENGTH_SHORT).show();
            }
        });
        rmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "REMOVE", Toast.LENGTH_SHORT).show();
            }
        });
        card.setRadius((float) 0.0);
        item.setText(title);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean expanded, View view, ViewGroup viewGroup) {
        Task task = getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.task_layout, viewGroup, false);
        }

        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        ImageView label = (ImageView) view.findViewById(R.id.category);

        text1.setText(task.getTaskDesc());
        text2.setText(task.getDisplayDate());
        //TODO check if color is empty
        label.setBackgroundColor(Integer.parseInt(task.getCategory().getColor()));

        CardView card = (CardView) view.findViewById(R.id.task_card);
        card.setRadius(0);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }
}

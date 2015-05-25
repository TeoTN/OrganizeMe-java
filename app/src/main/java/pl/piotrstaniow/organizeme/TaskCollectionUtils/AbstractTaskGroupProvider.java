package pl.piotrstaniow.organizeme.TaskCollectionUtils;

import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.Models.TaskAggregator;

import java.util.ArrayList;
import java.util.List;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created on 17.05.15.
 */
public abstract class AbstractTaskGroupProvider {
    protected List<Predicate<Task>> predicateList;

    public AbstractTaskGroupProvider() {
        predicateList = new ArrayList<>();
    }

    public String getGroupName(int position) {
        return predicateList.get(position).toString();
    }

    public int getGroupCount() {
        return predicateList.size();
    }

    public List<Task> getGroup(int position) {
        Predicate predicate = predicateList.get(position);
        return TaskAggregator.getInstance().filter(predicate);
    }

    public int getTaskCount(int position) {
        Predicate predicate = predicateList.get(position);
        return TaskAggregator.getInstance().filter(predicate).size();
    }
}

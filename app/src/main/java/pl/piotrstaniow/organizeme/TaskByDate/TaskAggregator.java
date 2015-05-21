package pl.piotrstaniow.organizeme.TaskByDate;

import com.android.internal.util.Predicate;
import pl.piotrstaniow.organizeme.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotr on 16.05.15.
 */
public class TaskAggregator {
    private static TaskAggregator instance = null;
    private List<Task> taskList;

    private TaskAggregator() {
        taskList = new ArrayList<>();
    }

    public static TaskAggregator getInstance() {
        if (instance == null) {
            synchronized(TaskAggregator.class) {
                if (instance == null) {
                    instance = new TaskAggregator();
                }
            }
        }
        return instance;
    }

    public void addTask(Task newTask) {
        taskList.add(newTask);
    }

    public void removeTask(Task task) {
        taskList.remove(task);
    }

    public int getSize() {
        return taskList.size();
    }

    public Task getItem(int i) {
        return taskList.get(i);
    }

    public List<Task> filter(Predicate<Task> predicate) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : taskList) {
            if (predicate.apply(task)) {
                filtered.add(task);
            }
        }
        return filtered;
    }
}

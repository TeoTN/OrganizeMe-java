package pl.piotrstaniow.organizeme.Models;

import com.android.internal.util.Predicate;

import pl.piotrstaniow.organizeme.DatabaseUtils.LocalQueryManager;
import pl.piotrstaniow.organizeme.ItemAggregator;

import java.util.ArrayList;
import java.util.List;

/**
 * OrganizeMe
 * Author: Piotr Staniów, Zuzanna Gniewaszewska, Sławomir Domagała
 * Email: staniowp@gmail.com oszka496@gmail.com slawomir.karol.domagala@gmail.com
 * Created on 16.05.15.
 */
public class TaskAggregator implements ItemAggregator<Task> {
    private static TaskAggregator instance = null;
    LocalQueryManager localQueryManager;
    private List<Task> taskList;

    private TaskAggregator() {
        taskList = new ArrayList<>();
        localQueryManager = LocalQueryManager.getInstance();
        localQueryManager.openWritable();
        taskList = localQueryManager.getAllTasks();
        localQueryManager.close();
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

    @Override
    public void add(Task newTask) {
        localQueryManager.openWritable();
        long id = localQueryManager.createTask(newTask);
        localQueryManager.close();

        newTask.setID(id);
        taskList.add(newTask);
    }

    @Override
    public void remove(Task task) {
        //localQueryManager.remove(task);
        taskList.remove(task);
    }

    @Override
    public int getSize() {
        return taskList.size();
    }

    @Override
    public Task getItem(int i) {
        return taskList.get(i);
    }

    @Override
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
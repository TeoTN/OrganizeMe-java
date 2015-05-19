package pl.piotrstaniow.organizeme.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.piotrstaniow.organizeme.Category;
import pl.piotrstaniow.organizeme.Task;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskUtils;

/**
 * Created by Zuzanna Gniewaszewska on 17.05.15.
 */
public class LocalQueryManager {
    private static LocalQueryManager instance = null;
    private SQLiteDatabase database;
    private LocalDbHelper dbHelper;

    private LocalQueryManager(){
        try {
            dbHelper = LocalDbHelper.getInstance();
        } catch (NullPointerException e){
            System.out.println(e);
        }
    }

    public static LocalQueryManager getInstance(){
        if (instance == null) {
            synchronized(LocalQueryManager.class) {
                if (instance == null) {
                    instance = new LocalQueryManager();
                }
            }
        }
        return instance;
    }

    public void openWritable() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createTask(Task task){
        ContentValues values = new ContentValues();
        values.put("task_name", task.getTaskDesc());
        if(task.isDateSet())
            values.put("deadline", TaskUtils.dateToString(task.getDate()));
        long newRowId;
        newRowId = database.insert(
                "task", null, values);
        return newRowId;
    }

    public void removeTask(Task task){
        long id = task.getID();
        database.delete("task","id="+id,null);
    }

	public long createCategory(Category category) {
		ContentValues values = new ContentValues();
		values.put("name", category.getName());
		values.put("color", category.getColor());
		long newRowId = database.insert("category", null, values);
		return newRowId;
	}

	public void removeCategory(Category category){
		long id = category.getID();
		database.delete("category","id="+id,null);
	}

    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        String[] columns = {"id", "task_name", "deadline"};
        Cursor cursor = database.query("task",columns,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            String taskDesc = cursor.getString(1);
            String taskDate = cursor.getString(2);
            Task task = new Task();
            if(taskDate != null)
                task.setDate(TaskUtils.stringToDate(taskDate));
            task.setTaskDesc(taskDesc);
            task.setID(id);
            taskList.add(task);

            cursor.moveToNext();
        }
        cursor.close();
        return taskList;
    }
}

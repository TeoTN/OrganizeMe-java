package pl.piotrstaniow.organizeme.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.piotrstaniow.organizeme.Task;

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

    public long createTask(String name, String date){
        ContentValues values = new ContentValues();
        values.put("task_name", name);
        values.put("deadline", date);
        long newRowId;
        newRowId = database.insert(
                "task", null, values);
        return newRowId;
    }

    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        String[] columns = {"task_name", "deadline"};
        Cursor cursor = database.query("task",columns,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String taskDesc = cursor.getString(0);
            String taskDate = cursor.getString(1);

            Task task = new Task();
           task.setTaskDesc(taskDesc);
        //    task.setTaskDate(taskDate);
            taskList.add(task);

            cursor.moveToNext();
        }
        cursor.close();
        return taskList;
    }
}

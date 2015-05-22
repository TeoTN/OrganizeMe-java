package pl.piotrstaniow.organizeme.DatabaseUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.List;

import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.TaskUtils;

import java.util.ArrayList;
import java.util.List;

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
    @Override
    public void finalize() throws Throwable {
        database.close();
        dbHelper.close();
        super.finalize();
    }
    public void close(){
        database.close();
        dbHelper.close();
    }

    public long createTask(Task task){
        ContentValues values = new ContentValues();
        values.put("task_name", task.getTaskDesc());
        if(task.isDateSet())
            values.put("deadline", TaskUtils.dateToString(task.getDate(), task.isTimeSet()));
        Category category = task.getCategory();
        if(category == null){
            String[] columns = {"name"};
            Cursor cursor = database.query("category",columns,"name='Unassigned'",null,null,null,null);
            category = new Category();
            category.setName("Unassinged");
            category.setColor("#607d8b");
            if(cursor.isAfterLast()){
                createCategory(category);
            }
        }

        values.put("category_name", category.getName());

        long newRowId;
        newRowId = database.insert(
                "task", null, values);
        return newRowId;
    }

    public void editTask(Task task){
        ContentValues values = new ContentValues();
        values.put("task_name", task.getTaskDesc());
        if(task.isDateSet())
            values.put("deadline", TaskUtils.dateToString(task.getDate(), task.isTimeSet()));
        database.update("task", values, "id="+task.getID(), null);
    }


    public void removeTask(Task task){
        long id = task.getID();
        database.delete("task", "id=" + id, null);
    }

	public void createCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("color", category.getColor());
        database.insert("category", null, values);
    }

    public void editCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("color", category.getColor());
        database.update("category", values, "name=\"" + category.getName() + "\"", null);
    }

	public void removeCategory(Category category){
        database.delete("task", "category_name=\"" + category.getName() + "\"", null);
		database.delete("category", "name=\"" + category.getName() + "\"", null);
	}

    public List<Task> getAllTasks(){
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables("'task' NATURAL JOIN 'category'");

        List<Task> taskList = new ArrayList<>();
        String[] columns = {"id", "task_name", "deadline", "category_name", "color"};
        Cursor cursor = sqb.query(database,columns,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            String taskDesc = cursor.getString(1);
            String taskDate = cursor.getString(2);
            String category = cursor.getString(3);
            String color = cursor.getString(4);
            Category cat = new Category();
            cat.setColor(color);
            cat.setName(category);
            Task task = new Task();
            task.setCategory(cat);
            boolean isTimeSet = false;

            if(taskDate != null) {
                if (taskDate.contains(" "))
                    isTimeSet = true;

                task.setDate(TaskUtils.stringToDate(taskDate), isTimeSet);
            }
            task.setTaskDesc(taskDesc);
            task.setID(id);
            taskList.add(task);

            cursor.moveToNext();
        }
        cursor.close();

        return taskList;
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String[] columns = {"name", "color"};
        Cursor cursor = database.query("category",columns,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(0);
            String color = cursor.getString(1);
            Category category = new Category();
            category.setName(name);
            category.setColor(color);
            categoryList.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return categoryList;
    }
}

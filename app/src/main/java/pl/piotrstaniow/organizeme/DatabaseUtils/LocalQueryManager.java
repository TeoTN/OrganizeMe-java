package pl.piotrstaniow.organizeme.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.piotrstaniow.organizeme.Models.Category;
import pl.piotrstaniow.organizeme.Models.Label;
import pl.piotrstaniow.organizeme.Models.NotificationItem;
import pl.piotrstaniow.organizeme.Models.Task;
import pl.piotrstaniow.organizeme.R;
import pl.piotrstaniow.organizeme.TaskCollectionUtils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Zuzanna Gniewaszewska on 17.05.15.
 */
public class LocalQueryManager {
    private static LocalQueryManager instance = null;
    private SQLiteDatabase database;
    private LocalDbHelper dbHelper;

    private LocalQueryManager() {
        try {
            dbHelper = LocalDbHelper.getInstance();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static LocalQueryManager getInstance() {
        if (instance == null) {
            synchronized (LocalQueryManager.class) {
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

    public void close() {
        database.close();
        dbHelper.close();
    }

    public long createTask(Task task) {
        long id = database.insert("task", null, getContentValues(task));
        task.setID(id);
        addLabelsOfTask(task);
        return id;
    }

    private ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put("task_name", task.getTaskDesc());
        if (task.isDateSet()) {
            values.put("deadline", DateTimeUtils.dateToString(task.getDate(), task.isTimeSet()));
        }
        values.put("category_name", getOrCreateCategory(task).getName());
        if (task.getLocation() != null) {
            values.put("location_latitude", task.getLocation().latitude);
            values.put("location_longitude", task.getLocation().longitude);
        }
        values.put("location_precision", task.getLocationPrecision());
        values.put("location_notify", task.isLocationNotify());
        return values;
    }

    private Category getOrCreateCategory(Task task) {
        Category category = task.getCategory();
        if (category == null) {
            Context ctx = LocalDbHelper.getInstance().getContext();
            String unassigned_name = ctx.getResources().getString(R.string.unassigned_category_name);
            int unassigned_color = ctx.getResources().getColor(R.color.unassigned_category_color);

            String[] columns = {"name"};
            Cursor cursor = database.query("category", columns, "name='" + unassigned_name + "'", null, null, null, null);

            category = new Category();
            category.setName(unassigned_name);
            category.setColor(String.valueOf(unassigned_color));
            if (cursor.getCount() == 0) {
                createCategory(category);
            }
        }
        return category;
    }

    public void editTask(Task task) {
        addLabelsOfTask(task);
        database.update("task", getContentValues(task), "id=" + task.getID(), null);
    }

    public void archiveTask(Task task, Date time) {
        ContentValues values = getContentValues(task);
        values.put("done", DateTimeUtils.dateToString(time, true));
        database.insert("archived_task", null, values);
        removeTask(task);
    }

    public void removeTask(Task task) {
        long id = task.getID();
        database.delete("task", "id=" + id, null);
    }

    public long countArchivedTasks() {
        return DatabaseUtils.queryNumEntries(database, "archived_task");
    }

    public void createLabel(String label) {
        ContentValues values = new ContentValues();
        values.put("name", label);
        database.insert("label", null, values);
    }

    public void addLabelsOfTask(Task task) {
        database.delete("task_label", "task_id=" + task.getID(), null);
        List<Label> alreadyInDB = new ArrayList<>();
        for (Label label : task.getLabels()) {
            if (!alreadyInDB.contains(label)) {
                alreadyInDB.add(label);
                ContentValues values = new ContentValues();
                values.put("task_id", task.getID());
                values.put("label_name", label.getName());
                database.insert("task_label", null, values);
            }

        }
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

    public void removeCategory(Category category) {
        database.delete("task", "category_name=\"" + category.getName() + "\"", null);
        database.delete("category", "name=\"" + category.getName() + "\"", null);
    }

    public Task getTaskById(long id) {
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables("task INNER JOIN category ON task.category_name = category.name");
        Task task = new Task();

        String[] columns = {"id", "task_name", "deadline", "location_latitude",
                "location_longitude", "location_precision", "location_notify", "category_name", "color"};
        Cursor cursor = sqb.query(database, columns, "id=" + id, null, null, null, null);
        cursor.moveToFirst();

        task.setID(id);
        task.setTaskDesc(cursor.getString(1));
        if (!cursor.isNull(3) && !cursor.isNull(4)) {
            task.setLocation(new LatLng(cursor.getDouble(3), cursor.getDouble(4)));
        }
        task.setLocationPrecision(cursor.getInt(5));
        task.setLocationNotify(cursor.getInt(6) == 1);

        String taskDate = cursor.getString(2);
        boolean isTimeSet = false;
        if (taskDate != null) {
            if (taskDate.contains(" "))
                isTimeSet = true;

            task.setDate(DateTimeUtils.stringToDate(taskDate), isTimeSet);
        }

        String category = cursor.getString(7);
        String color = cursor.getString(8);
        Category cat = new Category();
        cat.setColor(color);
        cat.setName(category);
        task.setCategory(cat);
        cursor.close();
        return task;
    }

    public List<Task> getAllTasks() {
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables("task INNER JOIN category ON task.category_name = category.name");

        List<Task> taskList = new ArrayList<>();
        String[] columns = {"id", "task_name", "deadline", "location_latitude",  "location_longitude",
                "location_precision", "location_notify", "category_name", "color"};
        Cursor cursor = sqb.query(database, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            String taskDesc = cursor.getString(1);
            String taskDate = cursor.getString(2);
            String category = cursor.getString(7);
            String color = cursor.getString(8);
            Category cat = new Category();
            cat.setColor(color);
            cat.setName(category);
            Task task = new Task();
            task.setCategory(cat);
            boolean isTimeSet = false;

            task.setID(id);
            task.setTaskDesc(cursor.getString(1));
            if (!cursor.isNull(3) && !cursor.isNull(4)) {
                task.setLocation(new LatLng(cursor.getDouble(3), cursor.getDouble(4)));
            }
            task.setLocationPrecision(cursor.getInt(5));
            task.setLocationNotify(cursor.getInt(6) == 1);

            if (taskDate != null) {
                if (taskDate.contains(" "))
                    isTimeSet = true;

                task.setDate(DateTimeUtils.stringToDate(taskDate), isTimeSet);
            }
            task.setTaskDesc(taskDesc);
            task.setID(id);

            List<Label> labels = getLabelsOfTask(task);
            task.setLabels(labels);

            taskList.add(task);

            cursor.moveToNext();
        }
        cursor.close();

        return taskList;
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String[] columns = {"name", "color"};
        Cursor cursor = database.query("category", columns, null, null, null, null, null);
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

    public List<Label> getAllLabels() {
        List<Label> labelList = new ArrayList<>();
        String[] columns = {"name"};
        Cursor cursor = database.query("label", columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            labelList.add(new Label(cursor.getString(0)));
            cursor.moveToNext();
        }
        cursor.close();
        return labelList;
    }

    public List<Label> getLabelsOfTask(Task task) {
        List<Label> labelList = new ArrayList<>();
        String[] columns = {"label_name"};
        Cursor cursor = database.query("task_label", columns, "task_id=" + task.getID(), null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            labelList.add(new Label(cursor.getString(0)));
            cursor.moveToNext();
        }
        cursor.close();
        return labelList;
    }

    public long createNotification(NotificationItem ni){
        ContentValues values = new ContentValues();
        values.put("task_id", ni.getTaskID());
        values.put("type", ni.getType());
        return database.insert("notification", null, values);
    }

    public void removeNotification(NotificationItem ni){
        database.delete("notification", "id=" + ni.getTaskID(), null);
    }

    public List<NotificationItem> getAllNotifications(){
        List<NotificationItem> notificationItems = new ArrayList<>();
        String[] columns = {"id", "task_id", "type"};
        NotificationItem ni;
        Cursor cursor = database.query("notifications",columns,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ni = new NotificationItem();
            ni.setNotifID(cursor.getLong(0));
            ni.setTaskID(cursor.getLong(1));
            ni.setType(cursor.getString(2));
            notificationItems.add(ni);
            cursor.moveToNext();
        }
        cursor.close();
        return notificationItems;
    }
}

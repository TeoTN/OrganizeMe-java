package pl.piotrstaniow.organizeme.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

import pl.piotrstaniow.organizeme.R;

/**
 * Created by Zuzanna Gniewaszewska on 17.05.15.
 */
public class LocalDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "OrganizeMeDB";
    private static LocalDbHelper instance = null;
    private Context context;

    public static LocalDbHelper createInstance(Context context){
        if (instance == null) {
            synchronized(LocalDbHelper.class) {
                if (instance == null) {
                    instance = new LocalDbHelper(context);
                }
            }
        }
        instance.context = context;
        return instance;
    }

    public static LocalDbHelper getInstance() throws NullPointerException{
        if (instance == null){
            throw new NullPointerException("Instance wasn't created");
        } else
            return instance;
    }

    private void createTables(SQLiteDatabase database){
        String createCategoryTable = "CREATE TABLE IF NOT EXISTS category (name TEXT UNIQUE, color TEXT)";
        String createTaskTable = "CREATE TABLE IF NOT EXISTS task (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "task_name TEXT, " +
                "deadline TEXT, " +
                "category_name TEXT, " +
                "FOREIGN KEY(category_name) REFERENCES category(name))";

        String createLabelTable = "CREATE TABLE IF NOT EXISTS label (name TEXT UNIQUE)";

        String createTaskLabelTable = "CREATE TABLE IF NOT EXISTS task_label (" +
                "task_id INTEGER, " +
                "label_name TEXT, " +
                "FOREIGN KEY(task_id) REFERENCES task(id), " +
                "FOREIGN KEY(label_name) REFERENCES label(name))";

        database.execSQL(createCategoryTable);
        database.execSQL(createTaskTable);
        database.execSQL(createLabelTable);
        database.execSQL(createTaskLabelTable);
        createArchivedTaskTable(database);

    }

    private void createMandatoryEntries(SQLiteDatabase database){
        String unassigned_name = context.getResources().getString(R.string.unassigned_category_name);
        int unassigned_color = context.getResources().getColor(R.color.unassigned_category_color);

        ContentValues values = new ContentValues();
        values.put("name", unassigned_name);
        values.put("color", unassigned_color);
        database.insert("category", null, values);

    }
    private void dropTables(SQLiteDatabase database){
        String[] tables = {"task","archived_task", "category", "label", "task_label"};
        for (String s : tables){
            database.execSQL("DROP TABLE IF EXISTS " + s);
        }
    }

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
        createMandatoryEntries(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        dropTables(sqLiteDatabase);
        createTables(sqLiteDatabase);
        createMandatoryEntries(sqLiteDatabase);
    }

    public Context getContext() {
            return context;
    }
    
    private void createArchivedTaskTable(SQLiteDatabase sqLiteDatabase) {
        String createArchivedTaskTable = "CREATE TABLE IF NOT EXISTS archived_task (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "task_name TEXT, " +
                "deadline TEXT, " +
                "category_name TEXT, " +
                "done TEXT, " +
                "FOREIGN KEY(category_name) REFERENCES category(name))";
        sqLiteDatabase.execSQL(createArchivedTaskTable);
    }

    private void createNotificationTable(SQLiteDatabase sqLiteDatabase){
        String createNotificationTable = "CREATE TABLE IF NOT EXISTS notification (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "task_id INTEGER, " +
                "relative_time TEXT"  +
                "FOREIGN KEY(task_id) REFERENCES task(id))";
    }
}

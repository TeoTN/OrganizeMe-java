package pl.piotrstaniow.organizeme.DatabaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zuzanna Gniewaszewska on 17.05.15.
 */
public class LocalDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
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
        return instance;
    }

    public static LocalDbHelper getInstance() throws NullPointerException{
        if (instance == null){
            throw new NullPointerException("Instance wasn't created");
        } else
            return instance;
    }

    private void CreateTables(SQLiteDatabase database){
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
    }

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CreateTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}

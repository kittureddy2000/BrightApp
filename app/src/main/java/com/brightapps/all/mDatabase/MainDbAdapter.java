package com.brightapps.all.mDatabase;

/**
 * Created by kyadamakanti on 12/25/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.brightapps.all.Util;

public class MainDbAdapter {


    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "KIT-MainDbAdapter";

    // DB info: it's name, and the table we are using (just one).
    static final String DATABASE_NAME = "MyToDo.db";
    static final String DATABASE_TABLE = "ToDo_Main";
    static final String DATABASE_CAT_TABLE = "Categories";

    // Track DB version if a new version of your app changes the format.
    static final int DATABASE_VERSION = 5;

    // DB Fields keys
    // Main table keys:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_REMINDER_DATE = "Reminder_Date";
    private static final String KEY_SNOOZE_AMOUNT = "Snooze_Amount";
    public static final String KEY_PRIORITY = "Priority";
    public static final String KEY_CATEGORY_NAME = "Category_Name";
    public static final String KEY_RECURRENCE = "Recurrence";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_TASK_COMPLTETION_STATUS = "Task_Complete_status";
    private static final String KEY_CREATION_DATE = "Creation_date";
    private static final String KEY_UPDATE_DATE = "Update_date";


    //Category Table
    private static final String  KEY_CAT_ROW_ID = "_id";
    private static final String  KEY_CAT_CATEGORY_NAME = "Category";
    private static final String KEY_CAT_CREATION_DATE = "Creation_date";
    private static final String KEY_CAT_UPDATE_DATE = "Update_date";
    private static final String KEY_CAT_DESCRIPTION = "Description";


    //Key String Arrays
    private static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_TITLE, KEY_DESCRIPTION,KEY_REMINDER_DATE,  KEY_SNOOZE_AMOUNT, KEY_PRIORITY, KEY_CATEGORY_NAME, KEY_RECURRENCE,KEY_LOCATION, KEY_TASK_COMPLTETION_STATUS,KEY_CREATION_DATE, KEY_UPDATE_DATE};
    private static final String[] ALL_CAT_KEYS = new String[]{KEY_CAT_ROW_ID,KEY_CAT_CATEGORY_NAME,KEY_CAT_CREATION_DATE,KEY_CAT_UPDATE_DATE};

    //Main TodoTable SQL
    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITLE + " text not null, "
                    + KEY_DESCRIPTION + " text, "
                    + KEY_REMINDER_DATE + " text, "
                    + KEY_SNOOZE_AMOUNT + " integer not null, "
                    + KEY_PRIORITY + " string not null, "
                    + KEY_CATEGORY_NAME + " string not null,"
                    + KEY_RECURRENCE + " integer not null, "
                    + KEY_LOCATION + " string, "
                    + KEY_TASK_COMPLTETION_STATUS + " integer not null, "
                    + KEY_CREATION_DATE + " text not null, "
                    + KEY_UPDATE_DATE + " text not null"
                    + ");";
     // Todocategory Table SQL
    private static final String DATABASE_CAT_CREATE_SQL =
            "create table " + DATABASE_CAT_TABLE
                    + " (" + KEY_CAT_ROW_ID + " integer primary key autoincrement, "
                    + KEY_CAT_CATEGORY_NAME + " text not null, "
                    + KEY_CAT_DESCRIPTION + " text , "
                    + KEY_CREATION_DATE + " text not null, "
                    + KEY_UPDATE_DATE + " text not null"
                    + ");";


    private DatabaseHelper myDBHelper;
    private SQLiteDatabase toDb;


    // Add a new set of values to the database for Todomain table.
    public long insertRow(String Title, String Description, String Reminder_Date, int Snooz_amount, String Priority, String Category_Name, int Recurrence,String Location, int Task_Completion) {
      //  Log.d(TAG,"insertRow insert row called");

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, Title);
        initialValues.put(KEY_DESCRIPTION, Description);
        initialValues.put(KEY_REMINDER_DATE, Reminder_Date);
        initialValues.put(KEY_SNOOZE_AMOUNT, Snooz_amount);
        initialValues.put(KEY_PRIORITY, Priority);
        initialValues.put(KEY_CATEGORY_NAME, Category_Name);
        initialValues.put(KEY_RECURRENCE,Recurrence);
        initialValues.put(KEY_LOCATION, Location);
        initialValues.put(KEY_TASK_COMPLTETION_STATUS, 0);//Task_Completion);
        initialValues.put(KEY_CREATION_DATE, Util.getCurrentTimeString());
        initialValues.put(KEY_UPDATE_DATE,Util.getCurrentTimeString());

        // Insert it into the database.
        return toDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public long insertCatRow(String Category_Name, String Category_Description) {
      //  Log.d(TAG,"insertCatRow Category insert row called");


        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CAT_CATEGORY_NAME, Category_Name);
        initialValues.put(KEY_CAT_DESCRIPTION, Category_Description);
        initialValues.put(KEY_CAT_CREATION_DATE, Util.getCurrentTimeString());
        initialValues.put(KEY_CAT_UPDATE_DATE, Util.getCurrentTimeString());

        // Insert it into the database.
        return toDb.insert(DATABASE_CAT_TABLE, null, initialValues);
    }


    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return toDb.delete(DATABASE_TABLE, where, null) != 0;
    }

    public boolean deleteCatRow(long rowId) {
        String where = KEY_CAT_ROW_ID + "=" + rowId;
        return toDb.delete(DATABASE_CAT_TABLE, where, null) != 0;
    }


    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
                Log.d(TAG,"deleteAll : Deleting row id : "+ rowId);
                //cancel reminder if any
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = KEY_TASK_COMPLTETION_STATUS + " = 0";
        Cursor c = toDb.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getAllCatRows() {
        String where = null;
        Cursor c = toDb.query(true, DATABASE_CAT_TABLE, ALL_CAT_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Log.d(TAG , "getRow : Row_ID: "+String.valueOf(rowId));
        Cursor c = toDb.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
/*            Log.d("Table Data Title : ", c.getString(c.getColumnIndex(MainDbAdapter.KEY_TITLE)));
            Log.d("Table Data Description : ", c.getString(c.getColumnIndex(MainDbAdapter.KEY_DESCRIPTION)));
            Log.d("Table Data Reminder Time : ", c.getString(c.getColumnIndex(MainDbAdapter.KEY_REMINDER_TIME)));
            Log.d("Table Data Category : ", c.getString(c.getColumnIndex(MainDbAdapter.KEY_CATEGORY_NAME)));
            Log.d("Table Data Priority : ", c.getString(c.getColumnIndex(MainDbAdapter.KEY_PRIORITY)));
            Log.d("Table Data Reccurence Hours : ", c.getString(c.getColumnIndex(MainDbAdapter.KEY_RECURRENCE)));
            Log.d("Table Data Location : ", c.getString(c.getColumnIndex(MainDbAdapter.KEY_LOCATION)));*/
            int taks_completion = c.getInt(c.getColumnIndex(MainDbAdapter.KEY_TASK_COMPLTETION_STATUS));

            // Log.d("Table Data Location : ",String.valueOf(taks_completion));
        }
        return c;
    }

    public Cursor getCatRow(long rowId) {
        String where = KEY_CAT_ROW_ID + "=" + rowId;
        Cursor c = toDb.query(true, DATABASE_CAT_TABLE, ALL_CAT_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }



    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String Title, String Description, String Reminder_Date, int Snooz_amount, String Priority, String Category_Name, int Recurrence,String Location,int Task_Completion) {
        String where = KEY_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TITLE, Title);
        newValues.put(KEY_DESCRIPTION, Description);
        newValues.put(KEY_REMINDER_DATE, Reminder_Date);
        newValues.put(KEY_SNOOZE_AMOUNT, Snooz_amount);
        newValues.put(KEY_PRIORITY, Priority);
        newValues.put(KEY_CATEGORY_NAME, Category_Name);
        newValues.put(KEY_RECURRENCE, Recurrence);
        newValues.put(KEY_LOCATION, Location);
        newValues.put(KEY_TASK_COMPLTETION_STATUS, Task_Completion);

        newValues.put(KEY_UPDATE_DATE, Util.getCurrentTimeString());
        // Insert it into the database.
        return toDb.update(DATABASE_TABLE, newValues, where, null) != 0;
    }
    // Change an existing row to be equal to new data.
    public boolean complete_task(long rowId, int Task_Completion) {
        String where = KEY_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TASK_COMPLTETION_STATUS, Task_Completion);

        // Insert it into the database.
        return toDb.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    public boolean updatCateRow(long rowId, String Category_Name, String Category_Description) {
        String where = KEY_CAT_ROW_ID + "=" + rowId;

        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CAT_CATEGORY_NAME, Category_Name);
        newValues.put(KEY_CAT_DESCRIPTION, Category_Description);
        newValues.put(KEY_CAT_UPDATE_DATE, Util.getCurrentTimeString());
        // Insert it into the database.
        return toDb.update(DATABASE_CAT_TABLE, newValues, where, null) != 0;
    }

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public MainDbAdapter(Context ctx) {
        myDBHelper = new DatabaseHelper(ctx);
        //Log.d(TAG,"MainDBAdapter constructor called");
    }

    // Open the database connection.
    public MainDbAdapter open() {
        toDb = myDBHelper.getWritableDatabase();
       // Log.d(TAG,"MainDBAdapter Open() called");
        return this;
    }

    // Close the database connection.
    public void close() {
        Log.d(TAG,"MainDBAdapter close() called");
        myDBHelper.close();
    }

    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db)
        {
            Log.d(TAG,"OnCreate: Database Helper On Create called" + DATABASE_CREATE_SQL);
            _db.execSQL(DATABASE_CREATE_SQL);
            _db.execSQL(DATABASE_CAT_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.d(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CAT_TABLE);
            // Recreate new database:
            onCreate(_db);
        }
    }
}

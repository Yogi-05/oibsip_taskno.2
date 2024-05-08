package com.oasis.todo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB";
    private static final String TABLE_USERS = "Users";
    private static final String COL_USER_ID = "ID";
    private static final String COL_USERNAME = "USERNAME";
    private static final String COL_PASSWORD = "PASSWORD";

    private static final String TABLE_TASKS = "Tasks";
    private static final String COL_TASK_ID = "ID";
    private static final String COL_TASK_USER_ID = "USER_ID";
    private static final String COL_TASK_NAME = "TASK_NAME";
    private static final String COL_TASK_COMPLETED = "IS_COMPLETED";

    private static final String COL_TASK_DATE_TIME = "TASK_DATE_TIME";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableQuery = "CREATE TABLE " + TABLE_USERS +
                " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUserTableQuery);

        String createTaskTableQuery = "CREATE TABLE " + TABLE_TASKS +
                " (" + COL_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK_USER_ID + " INTEGER, " +
                COL_TASK_NAME + " TEXT, " +
                COL_TASK_COMPLETED + " INTEGER DEFAULT 0, " +
                COL_TASK_DATE_TIME + " DATETIME, " +  // Define COL_TASK_DATE_TIME
                "FOREIGN KEY(" + COL_TASK_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
        db.execSQL(createTaskTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    public long addTask(String taskName, int year, int month, int day, int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK_USER_ID, 1);
        contentValues.put(COL_TASK_NAME, taskName);
        contentValues.put(COL_TASK_COMPLETED, 0); // Assuming the task is initially not completed

        // Create a string representation of the date and time
        String dateTime = String.format("%04d-%02d-%02d %02d:%02d:00", year, month, day, hour, minute);
        contentValues.put(COL_TASK_DATE_TIME, dateTime); // Assuming COL_TASK_DATE_TIME is the column for storing date and time

        long taskId = db.insert(TABLE_TASKS, null, contentValues);
        return taskId;
    }


    public boolean updateTaskStatus(long taskId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK_COMPLETED, isCompleted ? 1 : 0);
        int rowsAffected = db.update(TABLE_TASKS, contentValues, COL_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        return rowsAffected > 0;
    }

    public Cursor getAllTasksForUser(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COL_TASK_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    public boolean deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TASKS, COL_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        return rowsDeleted > 0;
    }
    //when the task is completed, the task is marked as completed
    public Cursor markTaskAsCompleted(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK_COMPLETED, 1);
        int rowsAffected = db.update(TABLE_TASKS, contentValues, COL_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COL_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
    }
    public Cursor markTaskAsNotCompleted(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TASK_COMPLETED, 0);
        int rowsAffected = db.update(TABLE_TASKS, contentValues, COL_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COL_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
    }
}

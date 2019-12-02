package com.example.finalproject.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.finalproject.Model.Event;
import com.example.finalproject.Model.Task;
import com.example.finalproject.Util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENT_TABLE = "CREATE TABLE " + Util.TABLE_NAME_EVENT + "("
                + Util.E_KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.E_KEY_NAME + " TEXT, "
                + Util.E_KEY_DESCRIPTION + " TEXT, "
                + Util.E_KEY_ENTRY_DATE+ " TEXT, "
                + Util.E_KEY_START_DATE + " TEXT, "
                + Util.E_KEY_END_DATE + " TEXT)";
        db.execSQL(CREATE_EVENT_TABLE);

        String CREATE_TASK_TABLE = "CREATE TABLE " + Util.TABLE_NAME_TASK + "("
                + Util.T_KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.T_KEY_NAME + " TEXT, "
                + Util.T_KEY_DESCRIPTION + " TEXT, "
                + Util.T_KEY_ENTRY_DATE+ " TEXT, "
                + Util.T_KEY_END_DATE + " TEXT, "
                + Util.T_KEY_STEPID + " INTEGER)";
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //String DROP_TABLE = String.valueOf(R.string.db_drop);
        db.execSQL("DROP TABLE "+Util.TABLE_NAME_EVENT);
        db.execSQL("DROP TABLE "+Util.TABLE_NAME_TASK);

        onCreate(db);
    }

    //add a contact
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.E_KEY_NAME, event.getName());
        values.put(Util.E_KEY_DESCRIPTION, event.getDescription());
        values.put(Util.E_KEY_ENTRY_DATE, event.getEntry_date());
        values.put(Util.E_KEY_START_DATE, event.getStart_date());
        values.put(Util.E_KEY_END_DATE, event.getEnd_date());

        db.insert(Util.TABLE_NAME_EVENT, null, values);
        db.close();
    }

    //get a event
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME_EVENT,
                new String[]{Util.E_KEY_ID, Util.E_KEY_NAME, Util.E_KEY_DESCRIPTION,
                        Util.E_KEY_ENTRY_DATE, Util.E_KEY_START_DATE, Util.E_KEY_END_DATE},
                Util.E_KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Event event = new Event(Integer.parseInt(cursor.getString(0)),
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getString(3),
                                    cursor.getString(4),
                                    cursor.getString(5));

            return event;
        }
        else {
            return null;
        }
    }

    //get all events on day
    public List<Event> getDayEvents(String startDate) {
        List<Event> eventList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_EVENT + " WHERE "
                + Util.E_KEY_START_DATE + " LIKE '" +startDate+ "%'";

        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));

                eventList.add(event);
            } while (cursor.moveToNext());
        }
        else {
            Log.d("Debug", "getDayEvents: No values found");
        }
        return eventList;
    }

    //get all events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String [] currentDateParts = date.split("-");

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_EVENT;

        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                String [] eventDateTimeParts = event.getStart_date().split(" ");
                String [] eventDateParts = eventDateTimeParts[0].split("-");

                if (Integer.parseInt(eventDateParts[0]) > Integer.parseInt(currentDateParts[0])) {
                    eventList.add(event);
                }
                else if (Integer.parseInt(eventDateParts[0]) == Integer.parseInt(currentDateParts[0])) {
                    if (Integer.parseInt(eventDateParts[1]) > Integer.parseInt(currentDateParts[1])) {
                        eventList.add(event);
                    }
                    else if (Integer.parseInt(eventDateParts[1]) == Integer.parseInt(currentDateParts[1])) {
                        if (Integer.parseInt(eventDateParts[2]) > Integer.parseInt(currentDateParts[2])) {
                            eventList.add(event);
                        }
                        else if (Integer.parseInt(eventDateParts[2]) == Integer.parseInt(currentDateParts[2])) {
                            eventList.add(event);
                        }
                    }
                }

            } while (cursor.moveToNext());
        }
        return eventList;
    }

    public List<Event> getEvents() {
        List<Event> eventList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_EVENT;

        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));

                eventList.add(event);

            } while (cursor.moveToNext());
        }
        return eventList;
    }

    //update a record
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.E_KEY_NAME, event.getName());
        values.put(Util.E_KEY_DESCRIPTION, event.getDescription());
        values.put(Util.E_KEY_ENTRY_DATE, event.getEntry_date());
        values.put(Util.E_KEY_START_DATE, event.getStart_date());
        values.put(Util.E_KEY_END_DATE, event.getEnd_date());

        return db.update(Util.TABLE_NAME_EVENT, values,
                Util.E_KEY_ID +" = ?", new String[]{String.valueOf(event.getId())});
        //db.close();
    }

    //delete a record
    public int deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int numRows = db.delete(Util.TABLE_NAME_EVENT, Util.E_KEY_ID +" = "+id, null);
        db.close();

        return numRows;
    }

    public int deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();

        int numRows = db.delete(Util.TABLE_NAME_EVENT, null, null);
        db.close();

        return numRows;
    }

    //count of database elements
    public int getEventCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_EVENT;

        Cursor cursor = db.rawQuery(selectAll, null);

        return cursor.getCount();
    }

    //add a contact
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.T_KEY_NAME, task.getName());
        values.put(Util.T_KEY_DESCRIPTION, task.getDescription());
        values.put(Util.T_KEY_ENTRY_DATE, task.getEntry_date());
        values.put(Util.T_KEY_END_DATE, task.getEnd_date());
        values.put(Util.T_KEY_STEPID, task.getStepid());

        db.insert(Util.TABLE_NAME_TASK, null, values);
        db.close();
    }

    //get a task
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME_TASK,
                new String[]{Util.T_KEY_ID, Util.T_KEY_NAME, Util.T_KEY_DESCRIPTION,
                        Util.T_KEY_ENTRY_DATE, Util.T_KEY_END_DATE, Util.T_KEY_STEPID},
                Util.T_KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Task task = new Task(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    Integer.parseInt(cursor.getString(5)));

            return task;
        }
        else {
            return null;
        }
    }

    public List<Task> getDayTasks(String endDate) {
        List<Task> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_TASK + " WHERE "
                + Util.T_KEY_END_DATE + " LIKE '" +endDate+ "%'";

        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Integer.parseInt(cursor.getString(5)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }
        else {
            Log.d("Debug", "getDayTasks: No values found");
        }
        return taskList;
    }

    //get all tasks
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String [] currentDateParts = date.split("-");

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_TASK;

        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Integer.parseInt(cursor.getString(5)));
                String [] taskDateTimeParts = task.getEnd_date().split(" ");
                String [] taskDateParts = taskDateTimeParts[0].split("-");

                if (Integer.parseInt(taskDateParts[0]) > Integer.parseInt(currentDateParts[0])) {
                    taskList.add(task);
                }
                else if (Integer.parseInt(taskDateParts[0]) == Integer.parseInt(currentDateParts[0])) {
                    if (Integer.parseInt(taskDateParts[1]) > Integer.parseInt(currentDateParts[1])) {
                        taskList.add(task);
                    }
                    else if (Integer.parseInt(taskDateParts[1]) == Integer.parseInt(currentDateParts[1])) {
                        if (Integer.parseInt(taskDateParts[2]) > Integer.parseInt(currentDateParts[2])) {
                            taskList.add(task);
                        }
                        else if (Integer.parseInt(taskDateParts[2]) == Integer.parseInt(currentDateParts[2])) {
                            taskList.add(task);
                        }
                    }
                }

            } while (cursor.moveToNext());
        }
        return taskList;
    }

    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_TASK;

        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Integer.parseInt(cursor.getString(5)));

               taskList.add(task);

            } while (cursor.moveToNext());
        }
        return taskList;
    }

    //update a record
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.T_KEY_NAME, task.getName());
        values.put(Util.T_KEY_DESCRIPTION, task.getDescription());
        values.put(Util.T_KEY_ENTRY_DATE, task.getEntry_date());
        values.put(Util.T_KEY_END_DATE, task.getEnd_date());
        values.put(Util.T_KEY_STEPID, task.getStepid());

        return db.update(Util.TABLE_NAME_TASK, values,
                Util.T_KEY_ID +" = ?", new String[]{String.valueOf(task.getId())});
        //db.close();
    }

    //delete a record
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME_TASK,  Util.T_KEY_ID +" = ?", new String[]{String.valueOf(id)});

        db.close();
    }
    public void deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME_TASK,  null, null);

        db.close();
    }

    //count of database elements
    public int getTaskCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_TASK;

        Cursor cursor = db.rawQuery(selectAll, null);

        return cursor.getCount();
    }
}

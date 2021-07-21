package com.todo.todolist.roomdb.todolist;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RoomToDoList.class}, version = 1, exportSchema = false)
abstract public class RoomToDoListHelper extends RoomDatabase {
    abstract public RoomToDoListDao roomToDoListDao();
    private static RoomToDoListHelper instance = null;

    public static synchronized RoomToDoListHelper getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomToDoListHelper.class, "ToDoList_Database").allowMainThreadQueries().build();
        }
        return instance;
    }

}

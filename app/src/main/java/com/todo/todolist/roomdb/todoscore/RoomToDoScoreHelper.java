package com.todo.todolist.roomdb.todoscore;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RoomToDoScore.class}, version = 1, exportSchema = false)
abstract public class RoomToDoScoreHelper extends RoomDatabase {
    abstract public RoomToDoScoreDao roomToDoScoreDao();
    private static RoomToDoScoreHelper instance = null;

    public static synchronized RoomToDoScoreHelper getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomToDoScoreHelper.class, "ToDoScore_Database").allowMainThreadQueries().build();
        }
        return instance;
    }
}
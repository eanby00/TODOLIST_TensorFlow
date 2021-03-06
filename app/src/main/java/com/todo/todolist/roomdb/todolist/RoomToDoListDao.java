package com.todo.todolist.roomdb.todolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RoomToDoListDao {
    @Query("select * from todo_list")
    List<RoomToDoList> getAll();

    @Query("select * from todo_list where date == :key and deleted == 0")
    List<RoomToDoList> getDate(String key);

    @Insert(onConflict = REPLACE)
    void insert(RoomToDoList todoList);

    @Delete
    void delete(RoomToDoList todoList);
}

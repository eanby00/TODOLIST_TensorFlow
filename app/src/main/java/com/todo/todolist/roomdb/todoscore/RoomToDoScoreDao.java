package com.todo.todolist.roomdb.todoscore;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface RoomToDoScoreDao {
    @Query("select * from todo_score")
    List<RoomToDoScore> getAll();

    @Query("select * from todo_score where date == :key")
    RoomToDoScore getDate(String key);

    @Insert(onConflict = REPLACE)
    void insert(RoomToDoScore todoScore);

    @Delete
    void delete(RoomToDoScore todoScore);
}

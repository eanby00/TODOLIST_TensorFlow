package com.todo.todolist.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("update todo_score set difficulty = :dif where date == :key")
    void updateDifficulty(String key, int dif);

    @Query("update todo_score set achievement = :ach where date == :key")
    void updateAchievement(String key, int ach);
}

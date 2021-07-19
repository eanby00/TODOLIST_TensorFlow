package com.todo.todolist.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_score")
public class RoomToDoScore {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int id;

    @ColumnInfo
    private String date = "";

    @ColumnInfo
    private int difficulty = 0;

    @ColumnInfo
    private int achievement = 0;

    public RoomToDoScore(String date, int difficulty, int achievement) {
        this.date = date;
        this.difficulty = difficulty;
        this.achievement = achievement;
    }

    public void setId(int id) { this.id = id; }

    public void setDate(String date) { this.date = date; }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void addDifficulty(int difficulty) { this.difficulty += difficulty; }

    public void setAchievement(int achievement) {
        this.achievement = achievement;
    }

    public void addAchievement(int achievement) { this.achievement += achievement; }

    public int getId() {return this.id; };

    public String getDate() { return this.date; }

    public int getDifficulty() {
        return this.difficulty;
    }

    public int getAchievement() {
        return this.achievement;
    }
}

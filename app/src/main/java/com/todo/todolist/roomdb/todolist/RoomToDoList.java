package com.todo.todolist.roomdb.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_list")
public class RoomToDoList {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int id;

    @ColumnInfo
    private String date = "";

    @ColumnInfo
    private String name = "";

    @ColumnInfo
    private int difficulty = 0;

    @ColumnInfo
    private Boolean done = false;

    @ColumnInfo
    private Boolean deleted = false;

    public RoomToDoList(String date, String name, int difficulty) {
        this.date = date;
        this.name = name;
        this.difficulty = difficulty;
    }
    public void setId(int id) { this.id = id; }

    public void setDate(String date) { this.date = date; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setDone() {
        this.done = !this.done;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setDeleted() {
        this.deleted = !this.deleted;
    }

    public int getId() {return this.id; };

    public String getDate() { return this.date; }

    public String getName() {
        return this.name;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public Boolean getDone() {
        return this.done;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }
}

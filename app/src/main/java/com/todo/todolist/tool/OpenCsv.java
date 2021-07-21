package com.todo.todolist.tool;

import android.util.Log;

import com.opencsv.CSVWriter;
import com.todo.todolist.roomdb.todoscore.RoomToDoScore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OpenCsv {
    public static void writeDataToCsv(String path, List<RoomToDoScore> scores) {
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(path));
            for (RoomToDoScore score : scores) {
                writer.writeNext(score.getItems());
            }
            writer.close();
        } catch (IOException e) {
            Log.d("error", "writeDataToCsv: \n"+e);
        }

    }
}

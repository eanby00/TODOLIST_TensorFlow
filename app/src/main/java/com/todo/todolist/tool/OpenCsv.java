package com.todo.todolist.tool;

import android.content.ContentValues;
import android.provider.MediaStore;
import android.util.Log;

import com.opencsv.CSVWriter;
import com.todo.todolist.roomdb.RoomToDoScore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OpenCsv {
    public static void writeDataToCsv(String path, List<RoomToDoScore> scores) {
        try{
//            Log.d("test", "writeDataToCsv: "+path);
            CSVWriter writer = new CSVWriter(new FileWriter(path));
            for (RoomToDoScore score : scores) {
//                Log.d("test", "writeDataToCsv_for: "+score.getDifficulty()+" "+score.getAchievement());
                writer.writeNext(score.getItems());
            }
            writer.close();
        } catch (IOException e) {

        }

    }
}

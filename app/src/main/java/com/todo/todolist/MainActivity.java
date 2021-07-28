package com.todo.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.todo.todolist.roomdb.todoscore.RoomToDoScore;
import com.todo.todolist.roomdb.todoscore.RoomToDoScoreHelper;
import com.todo.todolist.tool.OpenCsv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO LIST");

        // 달력 구성
        HashSet<Date> events = new HashSet<Date>();
        events.add(new Date());

        CustomCalendarView mainCal = (CustomCalendarView) findViewById(R.id.mainCal);
        mainCal.updateCalendar(events);

        mainCal.setEventHandler(new CustomCalendarView.EventHandler() {
            @Override
            public void onDayPress(String key) {
                Intent intent = new Intent(getApplicationContext(), ToDoDate.class);
                intent.putExtra("Date", key);
//                intent.putExtra("difEasy", dif_easy);
//                intent.putExtra("achLow", ach_low);
                RoomToDoScoreHelper scoreHelper = RoomToDoScoreHelper.getInstance(getApplicationContext());
                RoomToDoScore todo_score = scoreHelper.roomToDoScoreDao().getDate(key);
                if (todo_score == null) {
                    RoomToDoScore temp = new RoomToDoScore(key, 0, 0);
                    scoreHelper.roomToDoScoreDao().insert(temp);
                }
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            // 데이터 추출을 눌렀을 경우 현재 시간을 기준으로 csv 파일 생성
            // 난이도, 달성률 순으로 데이터가 이루어짐
            case R.id.extractData:
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                String time = dateFormat.format(date)+".csv";

                RoomToDoScoreHelper scoreHelper = RoomToDoScoreHelper.getInstance(getApplicationContext());
                File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), time);
                OpenCsv.writeDataToCsv(file.getPath(), scoreHelper.roomToDoScoreDao().getAll());

                return true;
            default:
                return false;
        }
    }
}
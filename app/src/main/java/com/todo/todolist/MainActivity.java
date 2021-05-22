package com.todo.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    TextView difEasy, difNormal, difHard, achLow, achNormal, achHigh;
    float dif_easy, dif_normal, dif_hard, ach_low, ach_normal, ach_high;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO LIST");

//        // 액션 바 제거
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

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
                startActivity(intent);
            }
        });

        // 계획 난이도 기준 설정

        difEasy = (TextView) findViewById(R.id.difEasy);
        difNormal = (TextView) findViewById(R.id.difNormal);
        difHard = (TextView) findViewById(R.id.difHard);

        dif_easy = 33;
        dif_normal = 67;
        dif_hard = 100;

        difEasy.setText(Float.toString(dif_easy));
        difNormal.setText(Float.toString(dif_normal));
        difHard.setText(Float.toString(dif_hard));


        // 계획 달성률 기준 설정

        achLow = (TextView) findViewById(R.id.achLow);
        achNormal = (TextView) findViewById(R.id.achNormal);
        achHigh = (TextView) findViewById(R.id.achHigh);

        ach_low = 33;
        ach_normal = 67;
        ach_high = 100;

        achLow.setText(Float.toString(ach_low));
        achNormal.setText(Float.toString(ach_normal));
        achHigh.setText(Float.toString(ach_high));
    }
}
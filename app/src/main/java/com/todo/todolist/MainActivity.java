package com.todo.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    TextView difEasy, difHard, achLow, achHigh;
    float dif_easy, dif_hard, ach_low, ach_high;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO LIST (정준성)");

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
        difHard = (TextView) findViewById(R.id.difHard);

        dif_easy = 50;
        dif_hard = 100;

        difEasy.setText(Float.toString(dif_easy));
        difHard.setText(Float.toString(dif_hard));


        // 계획 달성률 기준 설정

        achLow = (TextView) findViewById(R.id.achLow);
        achHigh = (TextView) findViewById(R.id.achHigh);

        ach_low = 50;
        ach_high = 100;

        achLow.setText(Float.toString(ach_low));
        achHigh.setText(Float.toString(ach_high));
    }
}
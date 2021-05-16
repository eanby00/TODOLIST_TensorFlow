package com.todo.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션 바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 달력 구성
        HashSet<Date> events = new HashSet<Date>();
        events.add(new Date());

        CustomCalendarView mainCal = (CustomCalendarView) findViewById(R.id.mainCal);
        mainCal.updateCalendar(events);

        mainCal.setEventHandler(new CustomCalendarView.EventHandler() {
            @Override
            public void onDayPress(Date date) {
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
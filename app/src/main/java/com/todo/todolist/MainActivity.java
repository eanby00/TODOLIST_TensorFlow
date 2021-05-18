package com.todo.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
                Toast.makeText(MainActivity.this, key, Toast.LENGTH_SHORT).show();
            }
        });

        // 계획 난이도 기준 설정

        difEasy = (TextView) findViewById(R.id.difEasy);
        difNormal = (TextView) findViewById(R.id.difNormal);
        difHard = (TextView) findViewById(R.id.difHard);

        dif_easy = 50;
        dif_normal = 120;
        dif_hard = 200;

        difEasy.setText(Float.toString(dif_easy));
        difNormal.setText(Float.toString(dif_normal));
        difHard.setText(Float.toString(dif_hard));

        LinearLayout.LayoutParams difEasyParams = (LinearLayout.LayoutParams) difEasy.getLayoutParams();
        difEasyParams.weight = dif_easy / dif_hard;
        Log.d("test", difEasyParams.weight+"");
        difEasy.setLayoutParams(difEasyParams);

        LinearLayout.LayoutParams difNormalParams = (LinearLayout.LayoutParams) difNormal.getLayoutParams();
        difNormalParams.weight = (dif_normal - dif_easy) / dif_hard;
        Log.d("test", difNormalParams.weight+"");
        difNormal.setLayoutParams(difNormalParams);

        LinearLayout.LayoutParams difHardParams = (LinearLayout.LayoutParams) difHard.getLayoutParams();
        difHardParams.weight = (dif_hard - dif_normal) / dif_hard;
        Log.d("test", difHardParams.weight+"");
        difHard.setLayoutParams(difHardParams);


        // 계획 달성률 기준 설정

        achLow = (TextView) findViewById(R.id.achLow);
        achNormal = (TextView) findViewById(R.id.achNormal);
        achHigh = (TextView) findViewById(R.id.achHigh);

        ach_low = 10;
        ach_normal = 120;
        ach_high = 180;

        achLow.setText(Float.toString(ach_low));
        achNormal.setText(Float.toString(ach_normal));
        achHigh.setText(Float.toString(ach_high));

        LinearLayout.LayoutParams achLowParams = (LinearLayout.LayoutParams) achLow.getLayoutParams();
        achLowParams.weight = ach_low / ach_high;
        Log.d("test", (achLowParams.weight == 0.055555556f)+"");
        achLow.setLayoutParams(achLowParams);

        LinearLayout.LayoutParams achNormalParams = (LinearLayout.LayoutParams) achNormal.getLayoutParams();
        achNormalParams.weight = (ach_normal - ach_low) / ach_high;
        Log.d("test", achNormalParams.weight+"");
        achNormal.setLayoutParams(achNormalParams);

        LinearLayout.LayoutParams achHighParams = (LinearLayout.LayoutParams) achHigh.getLayoutParams();
        achHighParams.weight = (ach_high - ach_normal) / ach_high;
        Log.d("test", achHighParams.weight+"");
        achHigh.setLayoutParams(achHighParams);
    }
}
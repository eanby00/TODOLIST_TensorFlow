package com.todo.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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

            case R.id.selectDate:
                MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
                MaterialDatePicker materialDatePicker = materialDateBuilder.build();
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        String key = materialDatePicker.getHeaderText().replace("년 ", "_").replace("월 ", "_").replace("일", "");

                        Intent intent = new Intent(getApplicationContext(), ToDoDate.class);
                        intent.putExtra("Date", key);
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


            default:
                return false;
        }
    }
}
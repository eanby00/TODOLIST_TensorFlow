package com.todo.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.todo.todolist.roomdb.RoomToDoScore;
import com.todo.todolist.roomdb.RoomToDoScoreHelper;
import com.todo.todolist.tool.OpenCsv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    TextView difEasy, achLow;
    Double dif_easy = 50.0;
    Double ach_low = 50.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TODO LIST (정준성)");

        setDifAch();

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
                intent.putExtra("difEasy", dif_easy);
                intent.putExtra("achLow", ach_low);
                RoomToDoScoreHelper scoreHelper = RoomToDoScoreHelper.getInstance(getApplicationContext());
                RoomToDoScore todo_score = scoreHelper.roomToDoScoreDao().getDate(key);
                if (todo_score == null) {
                    RoomToDoScore temp = new RoomToDoScore(key, 0, 0);
                    scoreHelper.roomToDoScoreDao().insert(temp);
                }
                startActivity(intent);
            }
        });

        RoomToDoScoreHelper scoreHelper = RoomToDoScoreHelper.getInstance(getApplicationContext());
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.txt");
        OpenCsv.writeDataToCsv(file.getPath(), scoreHelper.roomToDoScoreDao().getAll());
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        setDifAch();
    }

    public void setDifAch() {
        // 계획 난이도 기준 설정
        // TODO LIST의 난이도와 달성률 로드
        Vector <Double> difArray = new Vector<Double>();
        Vector <Double> achArray = new Vector<Double>();
        try{
            FileInputStream inFsNum = openFileInput("data.txt");
            byte[] txtNum = new byte[inFsNum.available()];
            inFsNum.read(txtNum);
            inFsNum.close();
            String str = new String(txtNum);
            Vector<String> tempNum = new Vector<String>(Arrays.asList(str.split("\n")));
            for(String row : tempNum) {
                if (row != null || row != "") {
                    String[] rows = row.split(",");
                    if (rows.length == 3 && Double.parseDouble(rows[1]) > 0) {
                        difArray.add(Double.parseDouble(rows[1]));
                        achArray.add(Double.parseDouble(rows[2])/ Double.parseDouble(rows[1])* 100);
                    }
                }
            }
            Double sumDif = 0.0;
            for (Double tempDif : difArray) {
                sumDif += tempDif;
            }
            dif_easy = Double.valueOf(String.format("%.2f", sumDif / difArray.size()));

            Double sumAch = 0.0;
            for (Double tempAch : achArray) {
                sumAch += tempAch;
            }
            ach_low = Double.valueOf(String.format("%.2f", sumAch / achArray.size()));

        } catch (IOException e) {
            try {
                // 최초 실행시 파일 초기화
                FileOutputStream outFs = openFileOutput("data.txt", Context.MODE_PRIVATE);
                String result = "";
                outFs.write(result.getBytes());
                outFs.close();
            } catch(IOException e2) {

            }

        }

        difEasy = (TextView) findViewById(R.id.difEasy);
        difEasy.setText(Double.toString(dif_easy));


        // 계획 달성률 기준 설정

        achLow = (TextView) findViewById(R.id.achLow);
        achLow.setText(Double.toString(ach_low));
    }
}
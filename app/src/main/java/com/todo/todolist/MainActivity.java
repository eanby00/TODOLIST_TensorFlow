package com.todo.todolist;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.todo.todolist.frame.ToDoDate;
import com.todo.todolist.roomdb.todoscore.RoomToDoScore;
import com.todo.todolist.roomdb.todoscore.RoomToDoScoreHelper;
import com.todo.todolist.tool.OpenCsv;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    ToDoDate todoDate;
    FragmentTransaction transaction;
    CoordinatorLayout snack_main;

    MaterialToolbar materialToolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snack_main = findViewById(R.id.snack_main);

        materialToolbar = findViewById(R.id.toolBar);
        setItem();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
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

                        Snackbar.make(snack_main, "파일이 생성되었습니다.", Snackbar.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int date = calendar.get((Calendar.DATE));
        String key = year+"_"+month+"_"+date;

        changeFragment(key);
    }


    public void setItem() {
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.selectDate:
                        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker materialDatePicker = materialDateBuilder.build();
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                        materialDatePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snack_main = findViewById(R.id.snack_main);
                                Snackbar.make(snack_main, "취소되었습니다.", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                String key = materialDatePicker.getHeaderText().replace("년 ", "_").replace("월 ", "_").replace("일", "");

                                changeFragment(key);
                            }
                        });

                    default:
                        return false;
                }
            }
        });
    }

    public void changeFragment(String key) {
        String[] dateInfo = key.split("_");
        materialToolbar.setTitle(dateInfo[0]+"년 "+dateInfo[1]+"월 "+dateInfo[2]+"일의 ToDo");

        RoomToDoScoreHelper scoreHelper = RoomToDoScoreHelper.getInstance(getApplicationContext());
        RoomToDoScore todo_score = scoreHelper.roomToDoScoreDao().getDate(key);
        if (todo_score == null) {
            RoomToDoScore temp = new RoomToDoScore(key, 0, 0);
            scoreHelper.roomToDoScoreDao().insert(temp);
        }

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        todoDate = new ToDoDate();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", key);
        todoDate.setArguments(bundle);
        transaction.replace(R.id.frame, todoDate, "main_data").commit();
    }
}
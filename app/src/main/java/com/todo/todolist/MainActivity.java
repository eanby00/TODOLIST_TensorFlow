package com.todo.todolist;

import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
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
    FragmentManager fragment_manager;
    ToDoDate todoDate;
    FragmentTransaction transaction;
    CoordinatorLayout snack_date;

    MaterialToolbar material_toolbar;
    DrawerLayout drawer_layout;
    ActionBarDrawerToggle toggle;
    NavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        material_toolbar = findViewById(R.id.tool_bar);
        snack_date = findViewById(R.id.snack_date);

        setItem();

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        navigation_view = (NavigationView) findViewById(R.id.nav);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer_layout.closeDrawers();
                switch (item.getItemId()) {

                    // 데이터 추출을 눌렀을 경우 현재 시간을 기준으로 csv 파일 생성
                    // 난이도, 달성률 순으로 데이터가 이루어짐
                    case R.id.extractData:
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat date_format = new SimpleDateFormat("yyyyMMddhhmm");
                        String time = date_format.format(date)+".csv";

                        RoomToDoScoreHelper score_helper = RoomToDoScoreHelper.getInstance(getApplicationContext());
                        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), time);
                        OpenCsv.writeDataToCsv(file.getPath(), score_helper.roomToDoScoreDao().getAll());

                        Snackbar.make(snack_date, "파일이 생성되었습니다.", Snackbar.LENGTH_SHORT).show();
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
        material_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });

        material_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.selectDate:
                        MaterialDatePicker.Builder material_date_builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker material_date_picker = material_date_builder.build();
                        material_date_picker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                        material_date_picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snack_date = findViewById(R.id.snack_date);
                                Snackbar.make(snack_date, "취소되었습니다.", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        material_date_picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                String key = material_date_picker.getHeaderText().replace("년 ", "_").replace("월 ", "_").replace("일", "");

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
        material_toolbar.setTitle(dateInfo[0]+"년 "+dateInfo[1]+"월 "+dateInfo[2]+"일의 ToDo");

        RoomToDoScoreHelper score_helper = RoomToDoScoreHelper.getInstance(getApplicationContext());
        RoomToDoScore todo_score = score_helper.roomToDoScoreDao().getDate(key);
        if (todo_score == null) {
            RoomToDoScore temp = new RoomToDoScore(key, 0, 0);
            score_helper.roomToDoScoreDao().insert(temp);
        }

        fragment_manager = getSupportFragmentManager();
        transaction = fragment_manager.beginTransaction();

        todoDate = new ToDoDate();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", key);
        todoDate.setArguments(bundle);
        transaction.replace(R.id.frame, todoDate, "main_data").commit();
    }
}

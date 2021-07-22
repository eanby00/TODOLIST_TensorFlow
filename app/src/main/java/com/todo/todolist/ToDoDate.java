package com.todo.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.todo.todolist.recyclerview.Adapter;
import com.todo.todolist.roomdb.todolist.RoomToDoList;
import com.todo.todolist.roomdb.todolist.RoomToDoListHelper;
import com.todo.todolist.roomdb.todoscore.RoomToDoScore;
import com.todo.todolist.roomdb.todoscore.RoomToDoScoreHelper;

import java.util.List;

public class ToDoDate extends AppCompatActivity {
    View dialogView;
    String key;

    RoomToDoListHelper listHelper = null;
    RoomToDoScoreHelper scoreHelper = null;
    List<RoomToDoList> todo_items;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_date);

        // intent로 받은 정보를 가지고 초기 화면 설정
        Intent intent = getIntent();
        key = intent.getStringExtra("Date");

        String[] dateInfo = key.split("_");
        setTitle(dateInfo[0]+"년 "+dateInfo[1]+"월 "+dateInfo[2]+"일의 ToDo");

        listHelper = RoomToDoListHelper.getInstance(getApplicationContext());
        scoreHelper = RoomToDoScoreHelper.getInstance(getApplicationContext());

        // 해당 날짜의 할 일 목록을 데이터베이스로부터 불러옴
        List<RoomToDoList> todo_list = listHelper.roomToDoListDao().getDate(key);
        todo_items = todo_list;

        // 할 일을 recycler view를 이용해서 시각화
        adapter = new Adapter(todo_items, listHelper, scoreHelper, key);
        RecyclerView recyclerView = findViewById(R.id.item_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public void refresh() {
        try {
            Intent intent = getIntent();
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // 옵션 메뉴 관련
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.todo_menu, menu);
        return true;
    }

    // 옵션 메뉴 관련
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            // 추가 버튼이 클릭되었을 때 화면에 출력되는 대화상자 관련 코드
            case R.id.addNewItem:
                dialogView = (View) View.inflate(ToDoDate.this, R.layout.tododialog, null);
//                AlertDialog.Builder dlg = new AlertDialog.Builder(ToDoDate.this);
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ToDoDate.this);
                materialAlertDialogBuilder.setTitle("ToDo 추가");
                materialAlertDialogBuilder.setView(dialogView);

                materialAlertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                // 확인 버튼을 눌렀을 대 기존의 데이터와 추가한 데이터를 함께 저장
                materialAlertDialogBuilder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dlgTitle = (EditText) dialogView.findViewById(R.id.todoName);
                        EditText dlgDif = (EditText) dialogView.findViewById(R.id.todoDif);
                        EditText dlgMemo = (EditText) dialogView.findViewById(R.id.todoMemo);

                        if (dlgTitle.getText() == null || dlgDif.getText() == null) {
                            Toast.makeText(getApplicationContext(), "필수 항목을 모두 입력하시오", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "완료되었습니다", Toast.LENGTH_SHORT).show();

                            Log.d("test", "onClick: \n"+dlgMemo.getText().toString());

                            // ToDo List에 새로운 데이터 추가
                            RoomToDoList roomToDoList = new RoomToDoList(key, dlgTitle.getText().toString(), Integer.parseInt(dlgDif.getText().toString()), dlgMemo.getText().toString());
                            listHelper.roomToDoListDao().insert(roomToDoList);

                            // 난이도 변경
                            RoomToDoScore roomToDoScore = scoreHelper.roomToDoScoreDao().getDate(key);
                            roomToDoScore.addDifficulty(Integer.parseInt(dlgDif.getText().toString()));
                            scoreHelper.roomToDoScoreDao().insert(roomToDoScore);

                            refresh();
                        }

                    }
                });
                materialAlertDialogBuilder.show();

                return true;

            default:
                return false;
        }
    }
}
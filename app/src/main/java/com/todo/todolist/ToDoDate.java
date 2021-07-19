package com.todo.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.todo.todolist.recyclerview.Adapter;
import com.todo.todolist.roomdb.RoomToDoList;
import com.todo.todolist.roomdb.RoomToDoListHelper;
import com.todo.todolist.roomdb.RoomToDoScore;
import com.todo.todolist.roomdb.RoomToDoScoreHelper;

import java.util.List;

public class ToDoDate extends AppCompatActivity {
    View dialogView;
    String key;
//    double difficulty;
//    double achievement;
    Double dif;
    Double ach;
//    Boolean flag = true;
//    Vector<String[]> datas = new Vector<String[]>();
//    Vector<String[]> numData = new Vector<String[]>();

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
        dif = intent.getDoubleExtra("difEasy", 0.0);
        ach = intent.getDoubleExtra("achLow", 0.0);

        String[] dateInfo = key.split("_");
        setTitle(dateInfo[0]+"년 "+dateInfo[1]+"월 "+dateInfo[2]+"일의 ToDo");

        listHelper = RoomToDoListHelper.getInstance(getApplicationContext());
        scoreHelper = RoomToDoScoreHelper.getInstance(getApplicationContext());

        // 하단 기준 설정
        TextView difEasy = (TextView) findViewById(R.id.difEasyItem);
        TextView achLow = (TextView) findViewById(R.id.achLowItem);
        difEasy.setText(String.valueOf(dif));
        achLow.setText(String.valueOf(ach));

        // 해당 날짜의 할 일 목록을 데이터베이스로부터 불러옴
        List<RoomToDoList> todo_list = listHelper.roomToDoListDao().getDate(key);
        todo_items = todo_list;

        // 할 일을 recycler view를 이용해서 시각화
        adapter = new Adapter(todo_items, listHelper, scoreHelper, key);
        RecyclerView recyclerView = findViewById(R.id.item_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        // 기준 하단에 난이도와 달성률을 시각적으로 표현
//        TextView difDate = (TextView) findViewById(R.id.difDate);
//        TextView achDate = (TextView) findViewById(R.id.achDate);
//
//        difDate.setText(String.format("%.2f", difficulty));
//        achDate.setText(String.format("%.2f", achievement / difficulty * 100));
//
//        difDate.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.parseFloat(String.valueOf(difficulty / dif))));
//        achDate.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.parseFloat(String.valueOf((achievement / difficulty * 100) / ach))));
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

    // 난이도나 달성률이 변화하면 그것을 numData 벡터에 저장
//    public void setChangeNum() {
//        boolean numFlag = true;
//        for ( String[] temp : numData) {
//            if (temp.length == 3) {
//                if (temp[0].equals(key)) {
//                    temp[1] = String.valueOf(difficulty);
//                    temp[2] = String.valueOf(achievement);
//                    numFlag = false;
//                }
//            }
//        }
//        if (numFlag) {
//            numData.add(new String[]{key, String.valueOf(difficulty), String.valueOf(achievement)});
//            numFlag = false;
//        }
//
//    }

    // 날짜별 TODO list와 난이도, 달성률 로드, 처음 실행시 초기화 기능 포함
//    public void load() {
//        difficulty = 0;
//        achievement = 0;
//        datas = new Vector<String[]>();
//        numData = new Vector<String[]>();
//
//        // 해당 날짜의 TODO LIST 로드
//        try {
//            FileInputStream inFs = openFileInput(key+".txt");
//            byte[] txt = new byte[inFs.available()];
//            inFs.read(txt);
//            inFs.close();
//            String str = new String(txt);
//            Vector<String> temp = new Vector<String>(Arrays.asList(str.split("\n")));
//            for(String row : temp) {
//                if (row != "" || row != null) {
//                    // 데이터: 이름, 난이도, done여부, 제거 여부
//                    String[] rows = row.split(",");
//                    datas.add(rows);
//                }
//            }
//
//        } catch (IOException e) {
//            try {
//                // 최초 실행시 파일 초기화
//                FileOutputStream outFs = openFileOutput(key+".txt", Context.MODE_PRIVATE);
//                String result = "";
//                outFs.write(result.getBytes());
//                outFs.close();
//            } catch(IOException e2) {
//
//            }
//        }
//
//        // 해당 날짜의 TODO LIST의 난이도와 달성률 로드
//        try{
//            FileInputStream inFsNum = openFileInput("data.txt");
//            byte[] txtNum = new byte[inFsNum.available()];
//            inFsNum.read(txtNum);
//            inFsNum.close();
//            String str = new String(txtNum);
//            Vector<String> tempNum = new Vector<String>(Arrays.asList(str.split("\n")));
//            for(String row : tempNum) {
//                if (row != null || row != "") {
//                    // 데이터: 이름, 난이도, 달성율
//                    String[] rows = row.split(",");
//                    if (rows.length == 3) {
//                        if (rows[0].equals(key)) {
//                            difficulty = Double.parseDouble(rows[1]);
//                            achievement = Double.parseDouble(rows[2]);
//                            flag = false;
//                        }
//                        numData.add(rows);
//                    }
//                }
//            }
//            if (flag) {
//                numData.add(new String[]{key,String.valueOf(0.0), String.valueOf(0.0) });
//                flag = false;
//            }
//
//        } catch (IOException e) {
//            try {
//                // 최초 실행시 파일 초기화
//                FileOutputStream outFs = openFileOutput("data.txt", Context.MODE_PRIVATE);
//                String result = "";
//                outFs.write(result.getBytes());
//                outFs.close();
//            } catch(IOException e2) {
//
//            }
//
//        }
//    }

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
                AlertDialog.Builder dlg = new AlertDialog.Builder(ToDoDate.this);
                dlg.setTitle("할 일 새로 추가");
                dlg.setView(dialogView);

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                // 확인 버튼을 눌렀을 대 기존의 데이터와 추가한 데이터를 함께 저장
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dlgTitle = (EditText) dialogView.findViewById(R.id.todoName);
                        EditText dlgDif = (EditText) dialogView.findViewById(R.id.todoDif);

                        Toast.makeText(getApplicationContext(), "생성되었습니다.", Toast.LENGTH_SHORT).show();

                        // ToDo List에 새로운 데이터 추가
                        RoomToDoList roomToDoList = new RoomToDoList(key, dlgTitle.getText().toString(), Integer.parseInt(dlgDif.getText().toString()));
                        listHelper.roomToDoListDao().insert(roomToDoList);

                        // 난이도 변경
                        RoomToDoScore roomToDoScore = scoreHelper.roomToDoScoreDao().getDate(key);
                        roomToDoScore.addDifficulty(Integer.parseInt(dlgDif.getText().toString()));
                        scoreHelper.roomToDoScoreDao().insert(roomToDoScore);

                        refresh();

//                        datas.add(new String[]{dlgTitle.getText().toString(), dlgDif.getText().toString(), "false", "false"});
//                        difficulty += Double.parseDouble(dlgDif.getText().toString());
//                        setChangeNum();
//                        save();
                    }
                });
                dlg.show();
                return true;

            default:
                return false;
        }
    }
}
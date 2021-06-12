package com.todo.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class ToDoDate extends AppCompatActivity {
    View dialogView;
    String key;
    Vector<String[]> datas = new Vector<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_date);

        Intent intent = getIntent();
        key = intent.getStringExtra("Date");
        String[] dateInfo = key.split("_");
        setTitle(dateInfo[0]+"년 "+dateInfo[1]+"월 "+dateInfo[2]+"일의 ToDO");

        LinearLayout todoitems = (LinearLayout) findViewById(R.id.todoItems);
        datas = readFile(key);
        ToDoItem[] items = new ToDoItem[datas.size()];

        TextView[] nameitems = new TextView[datas.size()];
        TextView[] difitems = new TextView[datas.size()];
        ImageButton[] doneitems = new ImageButton[datas.size()];
        ImageButton[] deleteitems = new ImageButton[datas.size()];

        for (int i = 0; i < datas.size(); i++) {
            items[i] = new ToDoItem(this);
        }

        for (int i = 0; i < items.length; i++) {
            if (!Boolean.parseBoolean(datas.get(i)[3])) {
                items[i] = new ToDoItem(this);
                items[i].setTitle(datas.get(i)[0]);
                items[i].setDif(datas.get(i)[1]);
                items[i].setDone(datas.get(i)[2]);

                doneitems[i] = (ImageButton) items[i].findViewById(R.id.btnDone);
                deleteitems[i] = (ImageButton) items[i].findViewById(R.id.btnDelete);

                final int index = i;
                doneitems[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datas.get(index)[2] = String.valueOf(!Boolean.parseBoolean(datas.get(index)[2]));
                        save();
                    }
                });

                deleteitems[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datas.get(index)[3] = String.valueOf(!Boolean.parseBoolean(datas.get(index)[3]));
                        save();
                    }
                });

                todoitems.addView(items[i]);
            }
        }
    }

    Vector<String[]> readFile(String key) {
        Vector<String[]> data = new Vector<String[]>();
        FileInputStream inFs;

        try {
            inFs = openFileInput(key+".txt");
            byte[] txt = new byte[inFs.available()];
            inFs.read(txt);
            inFs.close();
            String str = new String(txt);
            Vector<String> temp = new Vector<String>(Arrays.asList(str.split("\n")));
            for(String row : temp) {
                if (row != null) {
                    // 이름, 난이도, done여부, 제거 여부
                    String[] rows = row.split(",");
                    data.add(rows);
                }

            }
        } catch (IOException e) {

        }

        return data;
    }

    public void save() {
        try {
            FileOutputStream outFs = openFileOutput(key+".txt", Context.MODE_PRIVATE);
            String str = "";
            if (datas != null) {
                for(String[] row : datas) {
                    str += row[0]+","+ row[1] +","+ row[2] +","+ row[3] +"\n";
                }
            }
            outFs.write(str.getBytes());
            outFs.close();
            refresh();

        } catch (IOException e) {

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.todo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
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

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dlgTitle = (EditText) dialogView.findViewById(R.id.todoName);
                        EditText dlgDif = (EditText) dialogView.findViewById(R.id.todoDif);

                        Toast.makeText(getApplicationContext(), "생성되었습니다.", Toast.LENGTH_SHORT).show();
                        try {
                            FileOutputStream outFs = openFileOutput(key+".txt", Context.MODE_PRIVATE);
                            String str = "";
                            if (datas != null) {
                                for(String[] row : datas) {
                                    str += row[0]+","+ row[1] +","+ row[2] +","+ row[3] +"\n";
                                }
                            }
                            str += dlgTitle.getText().toString()+","+ dlgDif.getText().toString() +", false,false\n";
                            outFs.write(str.getBytes());
                            outFs.close();


                        } catch (IOException e) {

                        }

                        refresh();
                    }
                });
                dlg.show();
                return true;

            default:
                return false;
        }
    }
}
package com.todo.todolist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ToDoItem extends LinearLayout {

    TextView name, dif;
    ImageButton btnDone, btnDelete;

    public ToDoItem(Context context) {
        super(context);
        init(context, null);
    }

    public ToDoItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ToDoItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.todoitem, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        name = (TextView) findViewById(R.id.name);
        dif = (TextView) findViewById(R.id.dif);
        btnDone = (ImageButton) findViewById(R.id.btnDone);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);

        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", name.getText().toString());
            }
        });
    }
}

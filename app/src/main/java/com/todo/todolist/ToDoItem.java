//package com.todo.todolist;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//
//public class ToDoItem extends LinearLayout {
//
//    Context c;
//    Boolean TF;
//
//    TextView name = (TextView) findViewById(R.id.name);
//    TextView dif = (TextView) findViewById(R.id.dif);
//
//    public ToDoItem(Context context) {
//        super(context);
//        c = context;
//        init(context, null);
//    }
//    public ToDoItem(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    public ToDoItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context, attrs);
//    }
//
//    private void init(Context context, AttributeSet attr) {
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutInflater.inflate(R.layout.todoitem, this);
//    }
//
//
//    public void setTitle(String title) {
//        name = (TextView) findViewById(R.id.name);
//        name.setText(title);
//    }
//
//    public void setDif(String d) {
//        dif = (TextView) findViewById(R.id.dif);
//        dif.setText(d);
//    }
//
//    public void setDone(String b) {
//        TF = Boolean.parseBoolean(b);
//        if (TF) {
//            name.setTextColor(Color.GREEN);
//        } else {
//            name.setTextColor(Color.BLACK);
//        }
//    }
//}

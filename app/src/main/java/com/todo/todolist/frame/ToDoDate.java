package com.todo.todolist.frame;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.todo.todolist.R;
import com.todo.todolist.recyclerview.Adapter;
import com.todo.todolist.roomdb.todolist.RoomToDoList;
import com.todo.todolist.roomdb.todolist.RoomToDoListHelper;
import com.todo.todolist.roomdb.todoscore.RoomToDoScoreHelper;

import java.util.List;

public class ToDoDate extends Fragment {
    View dialogView;
    String key;
    CoordinatorLayout snack_date;

    RoomToDoListHelper listHelper = null;
    RoomToDoScoreHelper scoreHelper = null;
    List<RoomToDoList> todo_items;
    Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup frameView = (ViewGroup) inflater.inflate(R.layout.todo_date, container, false);

        snack_date = frameView.findViewById(R.id.snack_date);

        // Bundle로 받은 정보를 가지고 초기 화면 설정
        Bundle bundle = getArguments();
        key = bundle.getString("key");

//        String[] dateInfo = key.split("_");
//        setTitle(dateInfo[0]+"년 "+dateInfo[1]+"월 "+dateInfo[2]+"일의 ToDo");

        listHelper = RoomToDoListHelper.getInstance(container.getContext());
        scoreHelper = RoomToDoScoreHelper.getInstance(container.getContext());

        // 해당 날짜의 할 일 목록을 데이터베이스로부터 불러옴
        List<RoomToDoList> todo_list = listHelper.roomToDoListDao().getDate(key);
        todo_items = todo_list;

        // 할 일을 recycler view를 이용해서 시각화
        adapter = new Adapter(todo_items, listHelper, scoreHelper, key);
        RecyclerView recyclerView = frameView.findViewById(R.id.item_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(adapter);
        return frameView;
    }

}

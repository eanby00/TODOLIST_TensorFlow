package com.todo.todolist.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todo.todolist.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter {
//    ArrayList<String> list;

    Adapter(ArrayList<String> list) {
//        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.todo_item_recycler, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        holder.tv.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
//        return list.size();
        return 0;
    }
}

class Holder extends RecyclerView.ViewHolder {
//    TextView tv;

    public Holder(@NonNull View itemView) {
        super(itemView);
//        tv = itemView.findViewById(R.id.text);
    }
}
package com.todo.todolist.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todo.todolist.R;
import com.todo.todolist.roomdb.RoomToDoList;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Holder> {
    List<RoomToDoList> todo_list;

    public Adapter(List<RoomToDoList> todo_list) {
        this.todo_list = todo_list;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.todo_item_recycler, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.item_name.setText(todo_list.get(position).getName());
        holder.item_difficulty.setText(String.valueOf(todo_list.get(position).getDifficulty()));
    }

    @Override
    public int getItemCount() {
        return todo_list.size();
    }
}

class Holder extends RecyclerView.ViewHolder {
    TextView item_name;
    TextView item_difficulty;
    ImageButton item_btnDone;
    ImageButton item_btnDelete;

    public Holder(@NonNull View itemView) {
        super(itemView);
        item_name = itemView.findViewById(R.id.item_name);
        item_difficulty = itemView.findViewById(R.id.item_difficulty);
        item_btnDone = itemView.findViewById(R.id.item_btnDone);
        item_btnDelete = itemView.findViewById(R.id.item_btnDelete);

        item_btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        item_btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
package com.todo.todolist.recyclerview;

import android.content.Context;
import android.graphics.Color;
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
import com.todo.todolist.roomdb.RoomToDoListHelper;
import com.todo.todolist.roomdb.RoomToDoScoreHelper;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    RoomToDoListHelper listHelper = null;
    RoomToDoScoreHelper scoreHelper = null;
    List<RoomToDoList> todo_list;
    String key;

    public Adapter(List<RoomToDoList> todo_list, RoomToDoListHelper listHelper, RoomToDoScoreHelper scoreHelper) {
        this.todo_list = todo_list;
        this.listHelper = listHelper;
        this.scoreHelper = scoreHelper;

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
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        todo_list.get(pos).setDone();

                        // 현재 Done 값을 체크해 true라면 난이도만큼 달성율 상승
                        // false라면 난이도만틈 달성률 감소
                        if (todo_list.get(pos).getDone()) {
                            item_name.setTextColor(Color.GREEN);
                        } else {
                            item_name.setTextColor(Color.BLACK);
                        }


                        listHelper.roomToDoListDao().insert(todo_list.get(pos));

                        notifyDataSetChanged();

                    }

                }
            });

            item_btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("test", "onClick: test");
                }
            });
        }
    }
}



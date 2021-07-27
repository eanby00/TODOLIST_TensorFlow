package com.todo.todolist.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.todo.todolist.R;
import com.todo.todolist.roomdb.todolist.RoomToDoList;
import com.todo.todolist.roomdb.todolist.RoomToDoListHelper;
import com.todo.todolist.roomdb.todoscore.RoomToDoScore;
import com.todo.todolist.roomdb.todoscore.RoomToDoScoreHelper;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    RoomToDoListHelper listHelper = null;
    RoomToDoScoreHelper scoreHelper = null;
    List<RoomToDoList> todo_list;
    RoomToDoScore todo_score;
    String key;
    Context context;

    public Adapter(List<RoomToDoList> todo_list, RoomToDoListHelper listHelper, RoomToDoScoreHelper scoreHelper, String key) {
        this.todo_list = todo_list;
        this.listHelper = listHelper;
        this.scoreHelper = scoreHelper;
        this.key = key;
        todo_score = scoreHelper.roomToDoScoreDao().getDate(key);

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // list의 데이터를 기반으로 내부 요소 생성
        holder.card.setChecked(todo_list.get(position).getDone());
        holder.item_name.setText(todo_list.get(position).getName());
        holder.item_difficulty.setText(String.valueOf(todo_list.get(position).getDifficulty()));
        holder.item_memo.setText(todo_list.get(position).getMemo());
    }

    @Override
    public int getItemCount() {
        return todo_list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView item_name;
        TextView item_difficulty;
        TextView item_memo;
        MaterialButton item_btnDone;
        MaterialButton item_btnDelete;
        MaterialCardView card;

        public Holder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_difficulty = itemView.findViewById(R.id.item_difficulty);
            item_memo = itemView.findViewById(R.id.item_memo);
            item_btnDone = itemView.findViewById(R.id.item_btnDone);
            item_btnDelete = itemView.findViewById(R.id.item_btnDelete);
            card = itemView.findViewById(R.id.card);

            item_btnDone.setOnClickListener(new View.OnClickListener() {
                // 완료 버튼이 클릭되었을 경우 글자색과 달성율 변경
                // 변경된 값 데이터베이스에 반영
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        todo_list.get(pos).setDone();
                        int dif = todo_list.get(pos).getDifficulty();

                        if (todo_list.get(pos).getDone()) {
                            card.setChecked(!card.isChecked());
                            todo_score.addAchievement(dif);

                        } else {
                            card.setChecked(!card.isChecked());
                            todo_score.addAchievement(-dif);
                        }

                        listHelper.roomToDoListDao().insert(todo_list.get(pos));
                        scoreHelper.roomToDoScoreDao().insert(todo_score);

                        notifyDataSetChanged();

                    }

                }
            });

            item_btnDelete.setOnClickListener(new View.OnClickListener() {
                // 삭제 버튼이 클릭되었을 경우 난이도와 달성율 변경 후 삭제
                // 변경된 값 데이터베이스에 반영
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                    materialAlertDialogBuilder.setTitle("삭제하시겠습니까?");
                    materialAlertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    materialAlertDialogBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                todo_list.get(pos).setDeleted();
                                int dif = todo_list.get(pos).getDifficulty();
                                todo_score.addDifficulty(-dif);
                                if (todo_list.get(pos).getDone()) {
                                    todo_score.addAchievement(-dif);
                                }


                                listHelper.roomToDoListDao().insert(todo_list.get(pos));
                                scoreHelper.roomToDoScoreDao().insert(todo_score);
                                todo_list = listHelper.roomToDoListDao().getDate(key);
                                notifyDataSetChanged();
                            }
                        }
                    });

                    materialAlertDialogBuilder.show();
                }
            });
        }
    }
}



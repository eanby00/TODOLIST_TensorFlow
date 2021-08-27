package com.todo.todolist.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.snackbar.Snackbar;
import com.todo.todolist.R;
import com.todo.todolist.frame.ToDoDate;
import com.todo.todolist.roomdb.todolist.RoomToDoList;
import com.todo.todolist.roomdb.todolist.RoomToDoListHelper;
import com.todo.todolist.roomdb.todoscore.RoomToDoScore;
import com.todo.todolist.roomdb.todoscore.RoomToDoScoreHelper;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    RoomToDoListHelper list_helper = null;
    RoomToDoScoreHelper score_helper = null;
    List<RoomToDoList> todo_list;
    RoomToDoScore todo_score;
    String key;
    Context context;
    ToDoDate todo_date;

    public Adapter(List<RoomToDoList> todo_list, RoomToDoListHelper list_helper, RoomToDoScoreHelper score_helper, String key, ToDoDate todo_date) {
        this.todo_list = todo_list;
        this.list_helper = list_helper;
        this.score_helper = score_helper;
        this.key = key;
        todo_score = score_helper.roomToDoScoreDao().getDate(key);
        this.todo_date = todo_date;

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
        // 내부 요소 생성
        holder.card.setChecked(todo_list.get(position).getDone());
        holder.item_name.setText(todo_list.get(position).getName());
        holder.item_difficulty.setText(String.valueOf(todo_list.get(position).getDifficulty()));
        if (todo_list.get(position).getMemo().equals("")) {
            holder.item_memo.setVisibility(View.GONE);
            holder.item_divider.setVisibility(View.GONE);
        } else {
            holder.item_memo.setText(todo_list.get(position).getMemo());
        }
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
        MaterialDivider item_divider;

        public Holder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_difficulty = itemView.findViewById(R.id.item_difficulty);
            item_memo = itemView.findViewById(R.id.item_memo);
            item_btnDone = itemView.findViewById(R.id.item_btnDone);
            item_btnDelete = itemView.findViewById(R.id.item_btnDelete);
            card = itemView.findViewById(R.id.card);
            item_divider = itemView.findViewById(R.id.item_divider);

            item_btnDone.setOnClickListener(new View.OnClickListener() {
                // 완료 버튼 이벤트
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        todo_list.get(pos).setDone();
                        int dif = todo_list.get(pos).getDifficulty();

                        // done 여부에 따라 체크 여부와 달성율 변경
                        if (todo_list.get(pos).getDone()) {
                            card.setChecked(!card.isChecked());
                            todo_score.addAchievement(dif);
                        } else {
                            card.setChecked(!card.isChecked());
                            todo_score.addAchievement(-dif);
                        }

                        // 데이터 베이스에 반영
                        list_helper.roomToDoListDao().insert(todo_list.get(pos));
                        score_helper.roomToDoScoreDao().insert(todo_score);

                        notifyDataSetChanged();
                    }

                }
            });

            item_btnDelete.setOnClickListener(new View.OnClickListener() {
                // 삭제 버튼 이벤트
                // 변경된 값 데이터베이스에 반영
                @Override
                public void onClick(View v) {
                    // 삭제 관련 다이어로그 설정
                    MaterialAlertDialogBuilder material_alert_dialog_builder = new MaterialAlertDialogBuilder(context);
                    material_alert_dialog_builder.setTitle("삭제하시겠습니까?");

                    // 취소 관련 이벤트
                    material_alert_dialog_builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(todo_date.getActivity().findViewById(R.id.snack_date), "취소되었습니다.", Snackbar.LENGTH_SHORT).show();
                        }
                    });

                    // 확인(삭제) 관련 이벤트
                    material_alert_dialog_builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(todo_date.getActivity().findViewById(R.id.snack_date), "삭제되었습니다.", Snackbar.LENGTH_SHORT).show();

                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                todo_list.get(pos).setDeleted();
                                int dif = todo_list.get(pos).getDifficulty();
                                todo_score.addDifficulty(-dif);

                                // 삭제될 아이템이 done이라면 달성율 감소
                                if (todo_list.get(pos).getDone()) {
                                    todo_score.addAchievement(-dif);
                                }

                                // 데이터베이스에 반영
                                list_helper.roomToDoListDao().insert(todo_list.get(pos));
                                score_helper.roomToDoScoreDao().insert(todo_score);
                                todo_list = list_helper.roomToDoListDao().getDate(key);
                                notifyDataSetChanged();
                            }
                        }
                    });

                    material_alert_dialog_builder.show();
                }
            });
        }
    }
}



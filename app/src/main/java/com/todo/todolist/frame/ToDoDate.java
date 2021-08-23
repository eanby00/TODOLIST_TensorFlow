package com.todo.todolist.frame;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.todo.todolist.MainActivity;
import com.todo.todolist.R;
import com.todo.todolist.recyclerview.Adapter;
import com.todo.todolist.roomdb.todolist.RoomToDoList;
import com.todo.todolist.roomdb.todolist.RoomToDoListHelper;
import com.todo.todolist.roomdb.todoscore.RoomToDoScore;
import com.todo.todolist.roomdb.todoscore.RoomToDoScoreHelper;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ToDoDate extends Fragment {
    View dialogView;
    String key;
    CoordinatorLayout snack_date;

    RoomToDoListHelper listHelper = null;
    RoomToDoScoreHelper scoreHelper = null;
    List<RoomToDoList> todo_items;
    Adapter adapter;
    FloatingActionButton addNewItem;

    ViewGroup frameView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frameView = (ViewGroup) inflater.inflate(R.layout.todo_date, container, false);

        snack_date = frameView.findViewById(R.id.snack_date);
        addNewItem = frameView.findViewById(R.id.addNewItem);

        // Bundle로 받은 정보를 가지고 초기 화면 설정
        Bundle bundle = getArguments();
        key = bundle.getString("key");

        listHelper = RoomToDoListHelper.getInstance(container.getContext());
        scoreHelper = RoomToDoScoreHelper.getInstance(container.getContext());

        predict(scoreHelper.roomToDoScoreDao().getDate(key));

        // 해당 날짜의 할 일 목록을 데이터베이스로부터 불러옴
        List<RoomToDoList> todo_list = listHelper.roomToDoListDao().getDate(key);
        todo_items = todo_list;

        // 할 일을 recycler view를 이용해서 시각화
        adapter = new Adapter(todo_items, listHelper, scoreHelper, key, this);
        RecyclerView recyclerView = frameView.findViewById(R.id.item_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(adapter);

        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = View.inflate(container.getContext(), R.layout.tododialog, null);
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(container.getContext());
                materialAlertDialogBuilder.setTitle("ToDo 추가");
                materialAlertDialogBuilder.setView(dialogView);

                materialAlertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(snack_date, "취소되었습니다.", Snackbar.LENGTH_SHORT).show();
                    }
                });

                // 확인 버튼을 눌렀을 대 기존의 데이터와 추가한 데이터를 함께 저장
                materialAlertDialogBuilder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dlgTitle = dialogView.findViewById(R.id.todoName);
                        EditText dlgDif = dialogView.findViewById(R.id.todoDif);
                        EditText dlgMemo = dialogView.findViewById(R.id.todoMemo);

                        if (dlgTitle.getText().toString().equals("") || dlgDif.getText().toString().equals("")) {
                            Snackbar.make(snack_date, "필수 항목을 모두 입력하시오.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(snack_date, "완료되었습니다.", Snackbar.LENGTH_SHORT).show();

                            // ToDo List에 새로운 데이터 추가
                            RoomToDoList roomToDoList = new RoomToDoList(key, dlgTitle.getText().toString(), Integer.parseInt(dlgDif.getText().toString()), dlgMemo.getText().toString());
                            listHelper.roomToDoListDao().insert(roomToDoList);

                            // 난이도 변경
                            RoomToDoScore roomToDoScore = scoreHelper.roomToDoScoreDao().getDate(key);
                            roomToDoScore.addDifficulty(Integer.parseInt(dlgDif.getText().toString()));
                            scoreHelper.roomToDoScoreDao().insert(roomToDoScore);

                            refresh();
                        }

                    }
                });
                materialAlertDialogBuilder.show();
            }
        });

        return frameView;
    }

    public void refresh() {
        Fragment fragment = null;
        fragment = getFragmentManager().findFragmentByTag("main_data");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    private Interpreter getTfliteInterpreter(String modelPath) {
        try{
            return new Interpreter(loadModelFile((MainActivity) getActivity(), modelPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public void predict(RoomToDoScore date) {
        Log.d("test", "predict: "+date.getDifficulty()+" / "+date.getAchievement());
        // 난이도 변경 ---------------------------------------------------------------------------
        Log.d("test", "predict_getDifficulty: "+date.getDifficulty());
        float[][] inputs_difficulty = new float[][]{{date.getDifficulty()}};

        float[][] resultLabel_difficulty = new float[1][2];
        Map<Integer, Object> outputs_difficulty = new HashMap();
        outputs_difficulty.put(0, resultLabel_difficulty);

        Interpreter tflite_difficulty = getTfliteInterpreter("converted_model_difficulty.tflite");
        tflite_difficulty.runForMultipleInputsOutputs(inputs_difficulty, outputs_difficulty);

        float[][] output_difficulty = (float[][]) outputs_difficulty.get(0);
        TextView text_difficulty = frameView.findViewById(R.id.text_difficulty);

        Log.d("test", "predict_difficulty: "+output_difficulty[0][0]+" / "+output_difficulty[0][1]);
        if (output_difficulty[0][0] > output_difficulty[0][1]) {
            text_difficulty.setText("당일 일정의 난이도는 "+Math.round(output_difficulty[0][0]*100)+"%의 확률로 평소보다 높습니다");
        } else if(output_difficulty[0][0] < output_difficulty[0][1]) {
            text_difficulty.setText("당일 일정의 난이도는 "+Math.round(output_difficulty[0][1]*100)+"%의 확률로 평소보다 낮습니다");
        } else {
            text_difficulty.setText("당일 일정의 난이도는 평소와 유사합니다.");
        }

        // 달성율 변경 ---------------------------------------------------------------------------
        Log.d("test", "predict_getAchievement: "+date.getAchievement());
        float[][] inputs_achievement = new float[][]{{date.getAchievement()}};

        float[][] resultLabel_achievement = new float[1][2];
        Map<Integer, Object> outputs_achievement = new HashMap();
        outputs_achievement.put(0, resultLabel_achievement);

        Interpreter tflite_achievement = getTfliteInterpreter("converted_model_achievement.tflite");
        tflite_achievement.runForMultipleInputsOutputs(inputs_achievement, outputs_achievement);

        float[][] output_achievement = (float[][]) outputs_achievement.get(0);
        TextView text_achievement = frameView.findViewById(R.id.text_achievement);

        Log.d("test", "predict_achievement: "+output_achievement[0][0]+" / "+output_achievement[0][1]+"\n");
        if (output_achievement[0][0] > output_achievement[0][1]) {
            text_achievement.setText("당일 일정의 달성율은 "+Math.round(output_achievement[0][0]*100)+"%의 확률로 평소보다 높습니다");
        } else if(output_achievement[0][0] < output_achievement[0][1]) {
            text_achievement.setText("당일 일정의 달성율은 "+Math.round(output_achievement[0][1]*100)+"%의 확률로 평소보다 낮습니다");
        } else {
            text_achievement.setText("당일 일정의 달성율은 평소와 유사합니다.");
        }
    }

}


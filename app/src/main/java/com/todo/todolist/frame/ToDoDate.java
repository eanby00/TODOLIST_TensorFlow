package com.todo.todolist.frame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.math.MathUtils;
import com.google.android.material.navigation.NavigationView;
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

public class ToDoDate extends Fragment {
    View dialog_view;
    String key;
    CoordinatorLayout snack_date;

    RoomToDoListHelper list_helper = null;
    RoomToDoScoreHelper score_helper = null;
    List<RoomToDoList> todo_items;
    Adapter adapter;
    FloatingActionButton add_new_item;
    BottomAppBar bottom_appbar;
    ViewGroup frame_view;

    NavigationView navigation_view;
    FrameLayout scrim;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frame_view = (ViewGroup) inflater.inflate(R.layout.todo_date, container, false);

        // xml ????????? ??????
        snack_date = frame_view.findViewById(R.id.snack_date);
        add_new_item = frame_view.findViewById(R.id.add_new_item);
        bottom_appbar = frame_view.findViewById(R.id.bottom_appbar);
        navigation_view = (NavigationView) frame_view.findViewById(R.id.nav_frame);
        scrim = (FrameLayout) frame_view.findViewById(R.id.scrim);

        // ?????? ?????? ??????????????? ?????? ?????? -----------------------------------------------------------------------------------------------
        BottomSheetBehavior bottom_sheet_behavior = BottomSheetBehavior.from(navigation_view);
        scrim.setVisibility(View.GONE);
        bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    default:
                        scrim.setVisibility(View.GONE);
                        bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        return false;
                }
            }
        });

        scrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrim.setVisibility(View.GONE);
                bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        bottom_sheet_behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float base_alpha = ResourcesCompat.getFloat(getResources(), R.dimen.material_emphasis_medium);

                float offset = (slideOffset - (-1f)) / (1f - (-1f)) * (1f - 0f) + 0f;
                int alpha = (int) MathUtils.lerp(0f, 255f, offset * base_alpha);
                int color = Color.argb(alpha, 0, 0, 0);
                scrim.setBackgroundColor(color);
            }
        });

        // ---------------------------------------------------------------------------------------------------------------

        // Bundle??? ?????? ????????? ?????? ??????
        Bundle bundle = getArguments();
        key = bundle.getString("key");

        // ????????? ????????? ??????
        list_helper = RoomToDoListHelper.getInstance(container.getContext());
        score_helper = RoomToDoScoreHelper.getInstance(container.getContext());

        // ?????? ????????? ??? ??? ?????? ??????
        List<RoomToDoList> todo_list = list_helper.roomToDoListDao().getDate(key);
        todo_items = todo_list;

        // ??? ?????? recycler view??? ???????????? ?????????
        adapter = new Adapter(todo_items, list_helper, score_helper, key, this);
        RecyclerView recycler_view = frame_view.findViewById(R.id.item_recycler);

        // ?????????????????? ??????
        recycler_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recycler_view.setAdapter(adapter);

        // fab ?????? ??????
        add_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ???????????? ??????????????? ?????? & ??????
                dialog_view = View.inflate(container.getContext(), R.layout.todo_dialog, null);
                MaterialAlertDialogBuilder material_alert_dialog_builder = new MaterialAlertDialogBuilder(container.getContext());
                material_alert_dialog_builder.setTitle("ToDo ??????");
                material_alert_dialog_builder.setView(dialog_view);

                // ?????? ?????? ?????????
                material_alert_dialog_builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(snack_date, "?????????????????????.", Snackbar.LENGTH_SHORT).show();
                    }
                });

                // ?????? ?????? ?????????
                material_alert_dialog_builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ??????????????? xml??? ??????
                        EditText dlg_title = dialog_view.findViewById(R.id.dlg_title);
                        EditText dlg_difficulty = dialog_view.findViewById(R.id.dlg_difficulty);
                        EditText dlg_memo = dialog_view.findViewById(R.id.dlg_memo);

                        // ?????? ????????? ?????????
                        if (dlg_title.getText().toString().equals("") || dlg_difficulty.getText().toString().equals("")) {
                            Snackbar.make(snack_date, "?????? ????????? ?????? ???????????????.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(snack_date, "?????????????????????.", Snackbar.LENGTH_SHORT).show();

                            // ToDo List??? ????????? ????????? ??????
                            RoomToDoList room_todo_list = new RoomToDoList(key, dlg_title.getText().toString(), Integer.parseInt(dlg_difficulty.getText().toString()), dlg_memo.getText().toString());
                            list_helper.roomToDoListDao().insert(room_todo_list);

                            // ????????? ??????
                            RoomToDoScore room_todo_score = score_helper.roomToDoScoreDao().getDate(key);
                            room_todo_score.addDifficulty(Integer.parseInt(dlg_difficulty.getText().toString()));
                            score_helper.roomToDoScoreDao().insert(room_todo_score);

                            refresh();
                        }

                    }
                });
                material_alert_dialog_builder.show();
            }
        });

        // ?????? ?????? ?????? ??????
        bottom_appbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // ?????? ?????? ?????? ?????????
                    case R.id.more:
                        // ?????? ?????? ??????
                        if (bottom_sheet_behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                            predict(score_helper.roomToDoScoreDao().getDate(key));
                            scrim.setVisibility(View.VISIBLE);
                            bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            scrim.setVisibility(View.GONE);
                            bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        return frame_view;
    }

    // ??????????????? ????????????
    public void refresh() {
        Fragment fragment = null;
        fragment = getFragmentManager().findFragmentByTag("main_data");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    // ?????? ???????????? -----------------------------------------------------------------------------------------
    private Interpreter getTfliteInterpreter(String model_path) {
        try{
            return new Interpreter(loadModelFile((MainActivity) getActivity(), model_path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MappedByteBuffer loadModelFile(Activity activity, String model_path) throws IOException {
        AssetFileDescriptor file_descriptor = activity.getAssets().openFd(model_path);
        FileInputStream input_stream = new FileInputStream(file_descriptor.getFileDescriptor());
        FileChannel file_channel = input_stream.getChannel();
        long start_offset = file_descriptor.getStartOffset();
        long declared_length = file_descriptor.getDeclaredLength();
        return file_channel.map(FileChannel.MapMode.READ_ONLY, start_offset, declared_length);
    }
    // ------------------------------------------------------------------------------------------------------


    // ?????? ?????? ??????
    @SuppressLint("RestrictedApi")
    public void predict(RoomToDoScore date) {
        // ????????? ?????? ---------------------------------------------------------------------------

        // input ?????? ??????
        float[][] inputs_difficulty = new float[][]{{date.getDifficulty()}};

        // output ?????? ??????
        float[][] resultLabel_difficulty = new float[1][2];
        Map<Integer, Object> outputs_difficulty = new HashMap();
        outputs_difficulty.put(0, resultLabel_difficulty);

        // ?????? ???????????? ?????? ??????
        Interpreter tflite_difficulty = getTfliteInterpreter("converted_model_difficulty.tflite");
        tflite_difficulty.runForMultipleInputsOutputs(inputs_difficulty, outputs_difficulty);

        // ?????? ??? ????????????
        float[][] output_difficulty = (float[][]) outputs_difficulty.get(0);
        NavigationMenuItemView text_difficulty = frame_view.findViewById(R.id.info_difficulty);

        // ????????? ?????? ??????
        if (output_difficulty[0][0] > output_difficulty[0][1]) {
            text_difficulty.setTitle("?????? ????????? ???????????? "+Math.round(output_difficulty[0][0]*100)+"%??? ????????? ???????????? ????????????");
        } else if(output_difficulty[0][0] < output_difficulty[0][1]) {
            text_difficulty.setTitle("?????? ????????? ???????????? "+Math.round(output_difficulty[0][1]*100)+"%??? ????????? ???????????? ????????????");
        } else {
            text_difficulty.setTitle("?????? ????????? ???????????? ????????? ???????????????.");
        }

        // ????????? ?????? ---------------------------------------------------------------------------
        // input ?????? ??????
        float[][] inputs_achievement = new float[][]{{date.getAchievement()}};

        // output ?????? ??????
        float[][] resultLabel_achievement = new float[1][2];
        Map<Integer, Object> outputs_achievement = new HashMap();
        outputs_achievement.put(0, resultLabel_achievement);

        // ?????? ???????????? ?????? ??????
        Interpreter tflite_achievement = getTfliteInterpreter("converted_model_achievement.tflite");
        tflite_achievement.runForMultipleInputsOutputs(inputs_achievement, outputs_achievement);

        // ?????? ??? ????????????
        float[][] output_achievement = (float[][]) outputs_achievement.get(0);
        NavigationMenuItemView text_achievement = frame_view.findViewById(R.id.info_achievement);

        // ????????? ?????? ??????
        if (output_achievement[0][0] > output_achievement[0][1]) {
            text_achievement.setTitle("?????? ????????? ???????????? "+Math.round(output_achievement[0][0]*100)+"%??? ????????? ???????????? ????????????");
        } else if(output_achievement[0][0] < output_achievement[0][1]) {
            text_achievement.setTitle("?????? ????????? ???????????? "+Math.round(output_achievement[0][1]*100)+"%??? ????????? ???????????? ????????????");
        } else {
            text_achievement.setTitle("?????? ????????? ???????????? ????????? ???????????????.");
        }
    }

}


package com.android.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewAdapter viewAdapter;
    private List<TaskItem> taskList;

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String STATE = "state";
    @Override
    protected void onResume (){
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("Username","go set your info in setting !!");
        TextView usernameView = findViewById(R.id.Username_main);
        usernameView.setText(username);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppDB db = Room.databaseBuilder(getApplicationContext(),
                AppDB.class, AddTask.TASK).allowMainThreadQueries().build();

        TaskDao taskDao = db.taskDao();
        taskList = taskDao.findAll();
        RecyclerView taskRecycleView = findViewById(R.id.list);
        viewAdapter = new ViewAdapter(taskList, new ViewAdapter.OnTaskItemClickListener() {
            @Override
            public void onTaskClicked(int position) {
                Intent detailsPage = new Intent(getApplicationContext(), TaskDetailPage.class);
                detailsPage.putExtra(TITLE,taskList.get(position).getTitle());
                detailsPage.putExtra(BODY,taskList.get(position).getBody());
                detailsPage.putExtra(STATE,taskList.get(position).getState());
                startActivity(detailsPage);

            }
        });
        ImageButton menuBtn = findViewById(R.id.imageButton);
        menuBtn.setOnClickListener(v -> {
            Intent menuIntent = new Intent(MainActivity.this,SettingPage.class);
            startActivity(menuIntent);
        });
        Button allTaskBtn = MainActivity.this.findViewById(R.id.allTaskBtn);
        allTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AllTask.class);
                MainActivity.this.startActivity(intent);
            }
        });
        Button addTaskBtn = MainActivity.this.findViewById(R.id.addBtn);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddTask.class);
                MainActivity.this.startActivity(intent);
            }
        });



//        taskList = new ArrayList<>();
//        taskList.add(new TaskItem("Task A","Body A","New"));
//        taskList.add(new TaskItem("Task B","Body B"," In Progres"));
//        taskList.add(new TaskItem("Task C","Body C","Assign"));
//        taskList.add(new TaskItem("Task D","Body D","Completed"));
//        taskList.add(new TaskItem("Task E","Body E","New"));
//        taskList.add(new TaskItem("Task F","Body F"," In Progres"));
//        taskList.add(new TaskItem("Task I","Body I","New"));
//        taskList.add(new TaskItem("Task J","Body J","Assign"));
//        taskList.add(new TaskItem("Task K","Body K","Completed"));
//        taskList.add(new TaskItem("Task L","Body L"," In Progres"));
//        taskList.add(new TaskItem("Task M","Body M","New"));

//       viewAdapter = new ViewAdapter(taskList, new ViewAdapter.OnTaskItemClickListener() {
//           @Override
//           public void onTaskClicked(int position) {
//               Intent detailsPage= new Intent(getApplicationContext(),TaskDetailPage.class);
//               detailsPage.putExtra(TITLE , taskList.get(position).getTitle());
//               detailsPage.putExtra(BODY , taskList.get(position).getBody());
//               detailsPage.putExtra(STATE , taskList.get(position).getState());
//               startActivity(detailsPage);
//
//           }
//       });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        taskRecycleView.setLayoutManager(linearLayoutManager);
        taskRecycleView.setAdapter(viewAdapter);
    }





    }


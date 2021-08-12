package com.android.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
    public static final String TASK = "task-container";

    private TaskDao taskDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        AppDB database = Room.databaseBuilder(getApplicationContext(), AppDB.class, TASK)
                .allowMainThreadQueries().build();
        taskDao = database.taskDao();

        Button addTaskBtn = AddTask.this.findViewById(R.id.addTaskBtn);
        addTaskBtn.setOnClickListener(v -> {
            EditText taskTitle = AddTask.this.findViewById(R.id.task_title_input);
            EditText taskDesc = AddTask.this.findViewById(R.id.task_desc);
            String title = taskTitle.getText().toString();
            String body = taskDesc.getText().toString();
            if (!taskTitle.getText().toString().equals("") && !taskDesc.getText().toString().equals("")) {

                TaskItem taskItem = new TaskItem(title,body);
//                taskItem.setState(state);
                taskDao.insertOneTask(taskItem);
//                taskItem.setState(state);

                Toast.makeText(AddTask.this, "Submitted!!", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(AddTask.this, "Please fill the form", Toast.LENGTH_LONG).show();
            }
        });
        Button goHome = AddTask.this.findViewById(R.id.goHome);
        goHome.setOnClickListener(v -> {
            Intent intent = new Intent(AddTask.this,MainActivity.class);
            startActivity(intent);
        });
    }
}
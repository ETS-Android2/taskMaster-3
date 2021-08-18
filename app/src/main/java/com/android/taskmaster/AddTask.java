package com.android.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.DataStoreException;

public class AddTask extends AppCompatActivity {
    public static final String TASK = "task-container";

    private TaskDao taskDao;
    String taskState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


//        configureAmplify();
//
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException failure) {
            Log.e("Tutorial", "Could not initialize Amplify", failure);
        }


        AppDB database = Room.databaseBuilder(getApplicationContext(), AppDB.class, TASK)
                .allowMainThreadQueries().build();
        taskDao = database.taskDao();

        Button addTaskBtn = AddTask.this.findViewById(R.id.addTaskBtn);

        addTaskBtn.setOnClickListener(v -> {
            Spinner spinner = findViewById(R.id.spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.task_state_menu, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    taskState = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    String taskState = (String) parent.getItemAtPosition(0);

                }
            });


            EditText taskTitle = AddTask.this.findViewById(R.id.task_title_input);
            EditText taskDesc = AddTask.this.findViewById(R.id.task_desc);
            String title = taskTitle.getText().toString();
            String body = taskDesc.getText().toString();
            if (!taskTitle.getText().toString().equals("") && !taskDesc.getText().toString().equals("")) {

                /**
                 * save to Room
                 */
                TaskItem taskItem = new TaskItem(title, body);
                taskItem.setState(taskState);
                taskDao.insertOneTask(taskItem);

                /**
                 * Gathering to save  to DynamoDB
                 */
                com.amplifyframework.datastore.generated.model.TaskItem taskItem1 = com.amplifyframework.datastore.generated.model.TaskItem.builder()
                        .title(title)
                        .body(body)
                        .state(taskState)
                        .build();

                /**
                 * Send to Api to Save
                 */
                Amplify.API.mutate(ModelMutation.create(taskItem1),
                        response -> Log.i("MyAmplify", "Added" + response.getData()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );

                Toast.makeText(AddTask.this, "Submitted!!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AddTask.this, "Please fill the form", Toast.LENGTH_LONG).show();
            }
        });

        Button goHome = AddTask.this.findViewById(R.id.goHome);
        goHome.setOnClickListener(v -> {
            Intent intent = new Intent(AddTask.this, MainActivity.class);
            startActivity(intent);
        });


    }
//    private void configureAmplify(){
//        try {
//            Amplify.addPlugin(new AWSApiPlugin());
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.configure(getApplicationContext());
//            Log.i("Tutorial", "Initialized Amplify");
//        } catch (AmplifyException failure) {
//            Log.e("Tutorial", "Could not initialize Amplify", failure);
//        }
//
//    }
}
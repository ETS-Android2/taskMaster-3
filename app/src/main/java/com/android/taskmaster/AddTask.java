package com.android.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTask extends AppCompatActivity {
    public static final String TASK = "task-container";
    private static final int REQUEST_FOR_FILE = 188;
    private static final String TAG = "AddTask";

    private TaskDao taskDao;
    String taskState;
    String teamName;
    String fileType;
    String exctension;
    EditText taskTitle;
    String title;
    Team teamType;
    List<Team> TeamMembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
//        configureAmplify();
//
//        try {
//            Amplify.addPlugin(new AWSApiPlugin());
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.configure(getApplicationContext());
//            Log.i("Tutorial", "Initialized Amplify");
//        } catch (AmplifyException failure) {
//            Log.e("Tutorial", "Could not initialize Amplify", failure);
//        }

        AppDB database = Room.databaseBuilder(getApplicationContext(), AppDB.class, TASK)
                .allowMainThreadQueries().build();
        taskDao = database.taskDao();

        TeamMembers = new ArrayList<>();
                        Amplify.API.query(ModelQuery.list(Team.class),
                response -> {
                    Log.i(TAG, "onCreate: Queeeeeery"+response.getData());
                    for (Team team: response.getData()) {
                        TeamMembers.add(team);
                    }
                },
                        error -> Log.e(TAG, "onCreate: ERRRRRRRR"+error.toString())
                        );


        Spinner spinner = findViewById(R.id.spinner);
        Spinner spinnerTeam = findViewById(R.id.spinnerTeam);
        Spinner spinnerFileType = findViewById(R.id.spinnerFileType);
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


            }
        });
///////////////////
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.team_menu, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerTeam.setAdapter(adapter1);
        spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                 teamName = (String) parent.getItemAtPosition(0);

            }
        });
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.file_attached, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerFileType.setAdapter(adapter2);
        spinnerFileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fileType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fileType = (String) parent.getItemAtPosition(0);

            }
        });

        Button addTaskBtn = AddTask.this.findViewById(R.id.addTaskBtn);
        addTaskBtn.setOnClickListener(v -> {

             taskTitle = AddTask.this.findViewById(R.id.task_title_input);
            EditText taskDesc = AddTask.this.findViewById(R.id.task_desc);
             title = taskTitle.getText().toString();
            String body = taskDesc.getText().toString();
//            if (!taskTitle.getText().toString().equals("") && !taskDesc.getText().toString().equals("")) {

                TaskItem taskItem = new TaskItem(title, body);
                taskItem.setState(taskState);

                taskDao.insertOneTask(taskItem);

            Log.i(TAG, "onCreate: BBBBBBBEEEFFFFFFOOOOOOOOOORRRR query");
            for (Team teamTests: TeamMembers
                 ) {
                if(teamTests.getName().equals(teamName)){
                    Log.i(TAG, "onCreate: teeeeeeeeeeeea"+teamName);
                    teamType = teamTests;
                }

            }

                com.amplifyframework.datastore.generated.model.TaskItem taskItem1 = com.amplifyframework.datastore.generated.model.TaskItem.builder()
                        .title(title)
                        .body(body)
                        .team(teamType)
                        .state(taskState)
                        .build();

                Amplify.API.mutate(ModelMutation.create(taskItem1),
                        response -> Log.i("MyAmplify", "Added" + response.getData()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );

//                Amplify.API.mutate(ModelMutation.create(team),
//                        response -> Log.i("MyAmplify", "Added" + response.getData()),
//                        error -> Log.e("MyAmplifyApp", "Create failed", error)
//                );

                Toast.makeText(AddTask.this, "Submitted!!", Toast.LENGTH_SHORT).show();

//            } else {
//                Toast.makeText(AddTask.this, "Please fill the form", Toast.LENGTH_LONG).show();
//            }
        });

        Button goHome = AddTask.this.findViewById(R.id.goHome);
        goHome.setOnClickListener(v -> {
            Intent intent = new Intent(AddTask.this, MainActivity.class);
            startActivity(intent);
        });
        Log.i(TAG, "onCreate: File Type>>>>>>>>>>>>>"+fileType);

            Button uploadFile = findViewById(R.id.uploada_btn);
            uploadFile.setOnClickListener(v -> {
                taskTitle = AddTask.this.findViewById(R.id.task_title_input);
                title = taskTitle.getText().toString();
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile , "Choose File");
                startActivityForResult(chooseFile,REQUEST_FOR_FILE);
                prefEditor.putString("fileType",exctension);
                prefEditor.apply();
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
//@RequiresApi(api = Build.VERSION_CODES.Q)


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_FILE && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: returned from file explorer");
            Log.i(TAG, "onActivityResult: returned from file explorer"+data.getData().toString());
            Log.i(TAG, "onActivityResult: => " + data.getData().toString().substring(data.getData().toString().lastIndexOf(".")));
            exctension = data.getData().toString().substring(data.getData().toString().lastIndexOf("."));

            File uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
                Log.i(TAG, "onActivityResult: Excetnsion"+exctension);
            } catch (Exception exception) {
                Log.e(TAG, "onActivityResult: file upload failed" + exception.toString());
            }
if (exctension.contains("image")||exctension.contains("image")){
    exctension=".png";
}
            Amplify.Storage.uploadFile(
                    title+exctension,
                    uploadFile,
                    success -> Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey()),
                    error -> Log.e(TAG, "uploadFileToS3: failed " + error.toString())
            );
        }
    }

//    private void uploadFileToApiStorage(File uploadFile){
//        String key = taskTitle.toString().equals(null) ? "defualtTask.jpg" :taskTitle.getText().toString()+".jpg";
//        Amplify.Storage.uploadFile(
//                key,
//                uploadFile ,
//                success -> Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey()) ,
//                failure -> Log.e(TAG, "uploadFileToS3: failed " + failure.toString())
//        );
//    }


    //to get and save file -->


}
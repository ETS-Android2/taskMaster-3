package com.android.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.datastore.generated.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewAdapter viewAdapter;
//    private List<TaskItem> taskList;//Room Using
    private static List<com.amplifyframework.datastore.generated.model.TaskItem> taskLists = TaskManager.getInstance().getData();// never got a null refernce

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String STATE = "state";
    RecyclerView taskRecycleView;
    Handler handler;
    String teamName;


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

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                taskRecycleView.getAdapter().notifyDataSetChanged();

                return false;
            }
        });
//        Amplify.addPlugin(new AWSCognitoAuthPlugin());


        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(this);
            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException failure) {
            Log.e("Tutorial", "Could not initialize Amplify", failure);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        teamName = sharedPreferences.getString("TeamName","Team A");
//        try {
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.configure(getApplicationContext());
//
//            Log.i("Tutorial", "Initialized Amplify");
//        } catch (AmplifyException e) {
//            Log.e("Tutorial", "Could not initialize Amplify", e);
//        }

///**
// * choose the attribute that i want to save
// */
//        Todo item = Todo.builder().name("mahmood").build();
//
//        /**
//         * save In local Storage - like Room-
//         */
//        Amplify.DataStore.save(item,
//        success -> Log.i("Tutorial","Item Saved "+ success.item().getName()),
//        error -> Log.e("Tutorial","not Saved",error)
//        );
//
///**
// * get the data saved back
// */
//        Amplify.DataStore.query(Todo.class,
//                todos -> {
//                    while (todos.hasNext()) {
//                        Todo todo = todos.next();
//                        Log.i("Tutorial", "==== Todo ====");
//                        Log.i("Tutorial", "Name: " + todo.getName());
//                    }
//                },
//                failure -> Log.e("Tutorial", "Could not query DataStore", failure)
//        );



//       getTasksFromAPI();
//
            Amplify.API.query(ModelQuery.list(Team.class,Team.NAME.eq("Team A")),//,TaskItem.TEAM.contains("teamMember")
                    response ->{
                        Log.i("coming","on create : the item is =>"+teamName);
                        for(Team item : response.getData()){
                            handler.sendEmptyMessage(1);
//                            taskLists.add(item);
                            taskLists=item.getTaskitem();

                            Log.i("coming","on create : the item is =>"+item.getTaskitem());

                        }
                        runOnUiThread(() ->{
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                    this,
                                    LinearLayoutManager.VERTICAL,
                                    false);

                            taskRecycleView.setLayoutManager(linearLayoutManager);
                            taskRecycleView.setAdapter(viewAdapter);
                            dataSetChanged();
                        });
                    },
                    error -> Log.e("error","onCreate faild"+error.toString())
            );
//        Amplify.API.query(
//                ModelQuery.list(Team.class, Team),
//                response -> {
//                    for (Todo todo : response.getData()) {
//                        Log.i("MyAmplifyApp", todo.getName());
//                    }
//                },
//                error -> Log.e("MyAmplifyApp", "Query failure", error)
//        );

//        Amplify.API.query(ModelQuery.list(com.amplifyframework.datastore.generated.model.TaskItem.class,TaskItem.TEAM.eq(Team.ID)),//,TaskItem.TEAM.contains("teamMember")
//                response ->{
//                    Log.i("coming","on create : the item is =>"+teamName);
//                    for(com.amplifyframework.datastore.generated.model.TaskItem item : response.getData()){
//                        handler.sendEmptyMessage(1);
//                        taskLists.add(item);
//                        Log.i("coming","on create : the item is =>"+item);
//
//                    }
//                },
//                error -> Log.e("error","onCreate faild"+error.toString())
//        );


        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
//                taskRecycleView.getAdapter().notifyDataSetChanged();

                return false;
            }
        });
//        AppDB db = Room.databaseBuilder(getApplicationContext(),
//                AppDB.class, AddTask.TASK).allowMainThreadQueries().build();

//        TaskDao taskDao = db.taskDao();
//        taskList = taskDao.findAll();
         taskRecycleView = findViewById(R.id.list);
        viewAdapter = new ViewAdapter(taskLists, new ViewAdapter.OnTaskItemClickListener() {
            @Override
            public void onTaskClicked(int position) {
                Intent detailsPage = new Intent(getApplicationContext(), TaskDetailPage.class);
                detailsPage.putExtra(TITLE,taskLists.get(position).getTitle());
                detailsPage.putExtra(BODY,taskLists.get(position).getBody());
                detailsPage.putExtra(STATE,taskLists.get(position).getState());
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
    }



//    private void getTasksFromAPI(){
//    Amplify.API.query(ModelQuery.list(com.amplifyframework.datastore.generated.model.TaskItem.class),
//        response ->{
//
//            for(com.amplifyframework.datastore.generated.model.TaskItem item : response.getData()){
//                taskLists.add(item);
//                Log.i("coming","on create : the item is =>");
//                handler.sendEmptyMessage(1);
//            }
//        },
//    error -> Log.e("error","onCreate faild"+error.toString())
//    );
//}
    private void dataSetChanged(){viewAdapter.notifyDataSetChanged();}

//    public void queryFirstPage() {
//        query(ModelQuery.list(TaskItem.class, ModelPagination.limit(1_000)));
//    }
//
//    private static void query(GraphQLRequest<PaginatedResult<TaskItem>> request) {
//        Amplify.API.query(
//                request,
//                response -> {
//                    if (response.hasData()) {
//                        for (TaskItem item : response.getData()) {
//                            Log.d("MyAmplifyApp", item.getTitle());
//                            taskLists.add(item);
//                        }
//                        if (response.getData().hasNextResult()) {
//                            query(response.getData().getRequestForNextResult());
//                        }
//                    }
//                },
//                failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
//        );
//    }

    }


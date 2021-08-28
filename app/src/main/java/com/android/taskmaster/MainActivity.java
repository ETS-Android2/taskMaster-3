package com.android.taskmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static PinpointManager pinpointManager;
    ViewAdapter viewAdapter;
//    private List<TaskItem> taskList;//Room Using
    private static List<com.amplifyframework.datastore.generated.model.TaskItem> taskLists = TaskManager.getInstance().getData();// never got a null refernce

    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String STATE = "state";
    RecyclerView taskRecycleView;
    Handler handler;
    String teamName;

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }

    @Override
    protected void onResume (){
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString("Username","go set your info in setting !!");
        TextView usernameView = findViewById(R.id.Username_main);
        usernameView.setText(username);
        teamName = sharedPreferences.getString("TeamName","Team A");
        Amplify.API.query(ModelQuery.list(Team.class,Team.NAME.eq(teamName)),//,TaskItem.TEAM.contains("teamMember")
                response ->{
                    Log.i("comingQ1","on create : the item is =>"+teamName);
                    for(Team item : response.getData()){
                        handler.sendEmptyMessage(1);
//                            taskLists.add(item);
                        taskLists =item.getTaskitem();

                        Log.i("comingQ2","on create : the item is =>"+item.getTaskitem());

                    }
                    taskRecycleView = findViewById(R.id.list);
                    viewAdapter = new ViewAdapter(taskLists, position -> {
                        Intent detailsPage = new Intent(getApplicationContext(), TaskDetailPage.class);
                        detailsPage.putExtra(TITLE,taskLists.get(position).getTitle());
                        detailsPage.putExtra(BODY,taskLists.get(position).getBody());
                        detailsPage.putExtra(STATE,taskLists.get(position).getState());
                        doAnalytics();
                        startActivity(detailsPage);

                    });
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                            this,
                            LinearLayoutManager.VERTICAL,
                            false);

                    Log.i("viewAdapterCHange","on create : the item is =>"+taskLists);

                    runOnUiThread(() ->{
                        taskRecycleView.setLayoutManager(linearLayoutManager);
                        taskRecycleView.setAdapter(viewAdapter);
                        taskRecycleView.getAdapter().notifyDataSetChanged();
                    });
                },
                error -> Log.e("error","onCreate faild"+error.toString())
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize PinpointManager
        getPinpointManager(getApplicationContext());
        handler = new Handler(Looper.getMainLooper(), msg -> {
            Objects.requireNonNull(taskRecycleView.getAdapter()).notifyDataSetChanged();
            return false;
        });
//
//        try {
//            Amplify.addPlugin(new AWSApiPlugin());
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Amplify.addPlugin(new AWSS3StoragePlugin());
//            Amplify.configure(this);
//            Log.i("Tutorial", "Initialized Amplify");
//        } catch (AmplifyException failure) {
//            Log.e("Tutorial", "Could not initialize Amplify", failure);
//        }


//        getCurrentUser();
//        uploadFilesByS3();

//        Team teamA= Team.builder().name("Team A").build();
//        Team teamB= Team.builder().name("Team B").build();
//        Team teamC= Team.builder().name("Team C").build();
//                        Amplify.API.mutate(ModelMutation.create(teamA),
//                        response -> Log.i("MyAmplify", "Added" + response.getData()),
//                        error -> Log.e("MyAmplifyApp", "Create failed", error)
//                );
//                        Amplify.API.mutate(ModelMutation.create(teamB),
//                        response -> Log.i("MyAmplify", "Added" + response.getData()),
//                        error -> Log.e("MyAmplifyApp", "Create failed", error)
//                );
//                        Amplify.API.mutate(ModelMutation.create(teamC),
//                        response -> Log.i("MyAmplify", "Added" + response.getData()),
//                        error -> Log.e("MyAmplifyApp", "Create failed", error)
//                );
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
//
        //        AppDB db = Room.databaseBuilder(getApplicationContext(),
//                AppDB.class, AddTask.TASK).allowMainThreadQueries().build();

//        TaskDao taskDao = db.taskDao();
//        taskList = taskDao.findAll();


        handler = new Handler(Looper.getMainLooper(), msg -> {
//                taskRecycleView.getAdapter().notifyDataSetChanged();
            return false;
        }); //maybe it useless now
        ImageButton menuBtn = findViewById(R.id.imageButton);
        menuBtn.setOnClickListener(v -> {
            Intent menuIntent = new Intent(MainActivity.this,SettingPage.class);
            doAnalytics();
            startActivity(menuIntent);
        });
        Button allTaskBtn = MainActivity.this.findViewById(R.id.allTaskBtn);
        allTaskBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,AllTask.class);
            doAnalytics();
            MainActivity.this.startActivity(intent);
        });
        Button addTaskBtn = MainActivity.this.findViewById(R.id.addBtn);
        addTaskBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,AddTask.class);
            doAnalytics();
            MainActivity.this.startActivity(intent);
        });
        Button logOut = MainActivity.this.findViewById(R.id.log_out);
        logOut.setOnClickListener(v -> {
            Intent logout = new Intent(getApplicationContext(),SigninActivity.class);
            Amplify.Auth.signOut(
                    () -> {
                        Log.i("AuthQuickstart", "Signed out successfully");
                        doAnalytics();
                        MainActivity.this.startActivity(logout);
                    },
                    error -> Log.e("AuthQuickstart", error.toString())
            );

        });
    }
//void getCurrentUser(){
//    AuthUser authUser=Amplify.Auth.getCurrentUser();
//    Log.i(TAG, "getCurrentUser: "+authUser.toString());
//    Log.i(TAG, "getCurrentUser: username"+authUser.getUsername());
//    Log.i(TAG, "getCurrentUser: userId"+authUser.getUserId());
//}
//private void uploadFilesByS3(){
//        File targetFile = new File(getApplicationContext().getFilesDir(),"test");
//        try {
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetFile));
//            bufferedWriter.append("This is for lab 37");
//            bufferedWriter.close();
//        }catch (Exception e){
//            Log.e(TAG, "uploadFilesByS3: failed"+e.toString() );
//        }
//        Amplify.Storage.uploadFile(
//                "fileKey",
//                targetFile,
//                success -> Log.i(TAG,"uploadFilesByS3: success"+success.getKey()),
//                error -> Log.e(TAG, "uploadFilesByS3: failed"+error.toString() )
//        );
//}

// --Commented out by Inspection START (8/25/21, 3:56 PM):
////    private void getTasksFromAPI(){
////    Amplify.API.query(ModelQuery.list(com.amplifyframework.datastore.generated.model.TaskItem.class),
////        response ->{
////
////            for(com.amplifyframework.datastore.generated.model.TaskItem item : response.getData()){
////                taskLists.add(item);
////                Log.i("coming","on create : the item is =>");
////                handler.sendEmptyMessage(1);
////            }
////        },
////    error -> Log.e("error","onCreate faild"+error.toString())
////    );
////}
//    private void dataSetChanged(){viewAdapter.notifyDataSetChanged();}
// --Commented out by Inspection STOP (8/25/21, 3:56 PM)

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
private void doAnalytics (){
    AnalyticsEvent event = AnalyticsEvent.builder()
            .name("PasswordReset")
            .addProperty("Channel", "SMS")
            .addProperty("Successful", true)
            .addProperty("ProcessDuration", 792)
            .addProperty("UserAge", 120.3)
            .build();
    Amplify.Analytics.recordEvent(event);
}
    }


package com.android.taskmaster;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;
import java.util.Date;

public class TaskDetailPage extends AppCompatActivity {
    private static final String TAG = "TaskDetails";
    ImageView taskImage;
    private File downloadedImage ;
    String titleName;
    private Handler handleImageView ;
    String fileType;
    String exctension;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_page);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        exctension = sharedPreferences.getString("fileType",null);
        handleImageView = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                setTaskImage();
                return false;
            }
        });
        Intent comingIntent = getIntent();
        TextView title = findViewById(R.id.textView7);
        TextView body = findViewById(R.id.lorem_ipsum);
        TextView state = findViewById(R.id.state);
         titleName = comingIntent.getExtras().getString(MainActivity.TITLE);
        String bodytask = comingIntent.getExtras().getString(MainActivity.BODY);
        String statetask = comingIntent.getExtras().getString(MainActivity.STATE);
        title.setText(titleName);
        body.setText(bodytask);
        state.setText(statetask);
        taskImage = findViewById(R.id.download_btn);
        getFileFromApi();

    }
    private void setTaskImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // down sizing image as it throws OutOfMemory  Exception for larger images
        Bitmap bitmap = BitmapFactory.decodeFile(downloadedImage.getAbsolutePath(), options);
        Log.i(TAG, "setTaskImage: URL----->"+exctension);
        taskImage.setImageBitmap(bitmap);
    }


    private void getFileFromApi(){

if(exctension == "Image"){
    String fileFey =titleName+".png";
    Amplify.Storage.downloadFile(
        fileFey,
        new File(getApplicationContext().getFilesDir() + titleName+".png") ,
        success -> {
            Log.i(TAG, "getFileFromApi: successfully   ----> " + success.toString());
            downloadedImage = success.getFile();
            handleImageView.sendEmptyMessage(1);
        },
        failure -> Log.i(TAG, "getFileFromApi:  failed  ---> " + failure.toString())
) ;
}
    else {

    Amplify.Storage.getUrl(
            titleName,
            result -> Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl()),
            error -> Log.e("MyAmplifyApp", "URL generation failure", error)
    );
}

    }


}
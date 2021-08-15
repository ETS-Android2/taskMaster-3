package com.android.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        Button saveBtn = findViewById(R.id.save_username_btn);
        saveBtn.setOnClickListener(v -> {
            String username = ((EditText)findViewById(R.id.username_edit_txt)).getText().toString();

            prefEditor.putString("Username",username);
            prefEditor.apply();
            Toast.makeText(SettingPage.this,"username updated",Toast.LENGTH_LONG).show();
        });
        Button goHome = SettingPage.this.findViewById(R.id.goHome_btn);
        goHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingPage.this,MainActivity.class);
            startActivity(intent);
        });
    }
}
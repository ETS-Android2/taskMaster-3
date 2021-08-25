package com.android.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class SettingPage extends AppCompatActivity {
    String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();


        Spinner spinnerTeam = findViewById(R.id.spinnerTeamS);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.team_menu, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerTeam.setAdapter(adapter);
        spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamName = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        Button saveBtn = findViewById(R.id.save_username_btn);
        saveBtn.setOnClickListener(v -> {
            String username = ((EditText)findViewById(R.id.username_edit_txt)).getText().toString();

            prefEditor.putString("Username",username);
            prefEditor.putString("TeamName",teamName);
            prefEditor.apply();
            Toast.makeText(SettingPage.this,"username updated",Toast.LENGTH_LONG).show();
        });
        Button goHome = SettingPage.this.findViewById(R.id.goHome_btn);
        goHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingPage.this,MainActivity.class);
            startActivity(intent);
        });
        Button logOut = SettingPage.this.findViewById(R.id.log_out);
        logOut.setOnClickListener(v -> {
            Intent logout = new Intent(getApplicationContext(),SigninActivity.class);
            Amplify.Auth.signOut(
                    () -> {
                        Log.i("AuthQuickstart", "Signed out successfully");
                        SettingPage.this.startActivity(logout);
                    },
                    error -> Log.e("AuthQuickstart", error.toString())
            );

        });
    }
}
package com.android.taskmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signUp = findViewById(R.id.btnlogin);
        EditText username = findViewById(R.id.eteusername);
        EditText email = findViewById(R.id.etemail);
        EditText password = findViewById(R.id.mypass);

        signUp.setOnClickListener(view -> signUp(
                username.getText().toString(),
                email.getText().toString(),
                password.getText().toString()));
    }

    void signUp(String username, String email, String password) {
        Amplify.Auth.signUp(
                username,
                password,
                AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(), email)
                        .build(),
                success -> {
                    Log.i(TAG, "signUp successful: " + success.toString());
                    Intent goToVerification = new Intent(SignupActivity.this, VarificationActivity.class);
                    goToVerification.putExtra("username", username);
                    goToVerification.putExtra("password", password);
                    startActivity(goToVerification);
                },
                error -> Log.e(TAG, "signUp failed: " + error.toString()));
    }
}
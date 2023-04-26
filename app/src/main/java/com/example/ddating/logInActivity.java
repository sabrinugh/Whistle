package com.example.ddating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class logInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Variable
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();

        // Variable


        // Log IN with Email and Password
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (txt_email.isEmpty() || txt_password.isEmpty()) {
                    Toast.makeText(logInActivity.this, "Can't not missing any input !", Toast.LENGTH_SHORT).show();
                } else {
                    logInUser(txt_email, txt_password);
                }

            }
        });
    }


    // Function -> Log In with Email and Password
    private void logInUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(logInActivity.this, "Log In sucessfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(logInActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(logInActivity.this, "Authentication Failed ! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.example.ddating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class startActivity extends AppCompatActivity {

    private Button register;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Variable
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        // Variable


        // Register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(startActivity.this, registerActivity.class));
            }
        });


        // Log IN
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(startActivity.this, logInActivity.class));
            }
        });
    }

    // Check User Log IN or NOT
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentUser.reload();
            // Toast.makeText(startActivity.this, currentUser.getUid(), Toast.LENGTH_SHORT).show();

            startActivity(new Intent(startActivity.this, MainActivity.class));
            finish();
        }

    }
}
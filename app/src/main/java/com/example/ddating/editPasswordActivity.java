package com.example.ddating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class editPasswordActivity extends AppCompatActivity {

    private TextView title;
    private EditText newPassword1;
    private Button submitPassword;

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        // Variable
        title = findViewById(R.id.title);
        newPassword1 = findViewById(R.id.newPassword1);
        submitPassword = findViewById(R.id.submitPassword);
        back = findViewById(R.id.back);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Variable

        // go back to previous activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(editPasswordActivity.this, settinPage.class));
            }
        });

        // Change password
        submitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get new Password
                String txt_newPassword1 = newPassword1.getText().toString();

                // Update new Password
                currentUser.updatePassword(txt_newPassword1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(editPasswordActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editPasswordActivity.this, settinPage.class));
                            finish();
                        } else {
                            Toast.makeText(editPasswordActivity.this, "Failed to change", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editPasswordActivity.this, "Unable to connect to Database", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", e.toString());
                    }
                });

            }
        });
        // End Change Password


    }
}
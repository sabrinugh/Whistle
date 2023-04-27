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
    private Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        // Variable
        title = findViewById(R.id.title);
        backbtn = findViewById(R.id.backbtn);
        newPassword1 = findViewById(R.id.newPassword1);
        submitPassword = findViewById(R.id.submitPassword);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Variable
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Change password
        submitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get new Password
                String txt_newPassword1 = newPassword1.getText().toString();


                if (txt_newPassword1.isEmpty()) {
                    Toast.makeText(editPasswordActivity.this, "Can't enter empty password", Toast.LENGTH_SHORT).show();
                } else if (txt_newPassword1.length() < 6) {
                    Toast.makeText(editPasswordActivity.this, "New password is too short", Toast.LENGTH_SHORT).show();
                } else {
                    // Update new Password
                    currentUser.updatePassword(txt_newPassword1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(editPasswordActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(editPasswordActivity.this, settinPage.class));
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

            }
        });
        // End Change Password

    }
}
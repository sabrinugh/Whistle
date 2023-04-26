package com.example.ddating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.io.IOException;

public class editUserActivity extends AppCompatActivity {

    private EditText editUserName;
    private EditText editUserGender;
    private EditText editUserAge;
    private Button editUserBotton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eidt_user);

        // Variable
        editUserName = findViewById(R.id.editUserName);
        editUserGender = findViewById(R.id.editUserGender);
        editUserAge = findViewById(R.id.editUserAge);
        editUserBotton = findViewById(R.id.editUserButton);

        // Variable

        // Get User profile
        GetUserProfile();

        // Submit new User Profile
        editUserBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDateUser();
            }
        });

    }

    private void upDateUser() {
        String txt_newUserName = editUserName.getText().toString();
        String txt_newUserGender = editUserGender.getText().toString();
        String txt_newUserAge = editUserAge.getText().toString();

        boolean upDateUser_b = true;


        if (txt_newUserName.isEmpty()) {
            Toast.makeText(editUserActivity.this, "Missing User Name !", Toast.LENGTH_SHORT).show();
            upDateUser_b = false;
        }

        if (txt_newUserGender.isEmpty()) {
            Toast.makeText(editUserActivity.this, "Missing User Gender !", Toast.LENGTH_SHORT).show();
            upDateUser_b = false;
        }

        if (txt_newUserAge.isEmpty()) {
            Toast.makeText(editUserActivity.this, "Missing User Age !", Toast.LENGTH_SHORT).show();
            upDateUser_b = false;
        }

        if (upDateUser_b) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            WriteBatch batch = db.batch();

            DocumentReference newUserRef = db.collection("Users").document(currentUser.getUid());
            batch.update(newUserRef, "userName", txt_newUserName);
            batch.update(newUserRef, "gender", txt_newUserGender);
            batch.update(newUserRef, "age", txt_newUserAge);

            // Upload Data
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(editUserActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                        Log.d("Message", "Data Uploaded");

                        // Back to Setting Activity
                        startActivity(new Intent(editUserActivity.this, settinPage.class));
                        finish();
                    } else {
                        Toast.makeText(editUserActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR","Not able to upload Data");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() { // Fail to connect
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(editUserActivity.this, "Not able to connect to DataBase", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", e.toString());
                }
            });
        }

    }

    private void GetUserProfile() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) { // Able to Get Data

                    Log.d("Message", "Data Get");

                    String txt_newUserName = task.getResult().getString("userName");
                    String txt_newUserGender = task.getResult().getString("gender");
                    String txt_newUserAge = task.getResult().getString("age");



                    editUserName.setText(txt_newUserName);
                    editUserGender.setText(txt_newUserGender);
                    editUserAge.setText(txt_newUserAge);

                } else { // Data not find
                    Toast.makeText(editUserActivity.this, "Data Not Find" , Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() { // Can"t Collect DataBase
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editUserActivity.this, "No able to Collect DataBase !", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", e.toString());
            }
        });

    }
}
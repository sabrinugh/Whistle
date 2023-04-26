package com.example.ddating;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ImageDecoderKt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class addUserProfileActivity extends AppCompatActivity {

    private EditText userName;
    private EditText gender;
    private EditText age;
    private Button addUser;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_profile);

        // Variable
        userName = findViewById(R.id.userName);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        addUser = findViewById(R.id.addUser);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Variable


        // Add User profile in Collection("Users)
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectionReference users = db.collection("Users");

                boolean addUser_b = true;

                String txt_userName = userName.getText().toString();
                String txt_gender = gender.getText().toString();
                String txt_age = age.getText().toString();
                // int int_age = Integer.parseInt(txt_age);

                if (txt_userName.isEmpty() || txt_age.isEmpty() || txt_gender.isEmpty()) {
                    Toast.makeText(addUserProfileActivity.this, "Can't missing any input !", Toast.LENGTH_SHORT).show();
                    addUser_b = false;
                } else {
                    try {
                        int number = Integer.parseInt(txt_age);

                        if (number < 18) {
                            Toast.makeText(addUserProfileActivity.this, "Age need to over 18", Toast.LENGTH_SHORT).show();
                            addUser_b = false;
                        } else if (number > 125) {
                            Toast.makeText(addUserProfileActivity.this, "How can you survive over 125 years", Toast.LENGTH_SHORT).show();
                            addUser_b = false;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        addUser_b = false;
                        Toast.makeText(addUserProfileActivity.this, "Integer Only in Age", Toast.LENGTH_SHORT).show();
                    }
                }



                if (addUser_b) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userName", txt_userName);
                    map.put("gender", txt_gender);
                    map.put("age", txt_age);

                    users.document(currentUser.getUid()).set(map);
                    map.clear();

                    Toast.makeText(addUserProfileActivity.this, "User Added !", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(addUserProfileActivity.this, addDogProfileActivity.class));
                    finish();
                }

            }
        });


    }
}
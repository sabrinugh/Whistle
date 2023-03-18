package com.example.ddating;

import androidx.appcompat.app.AppCompatActivity;

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

    private EditText firstName;
    private EditText lastName;
    private EditText gender;
    private EditText age;
    private Button addUser;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_profile);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        addUser = findViewById(R.id.addUser);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = currentUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();



        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectionReference users = db.collection("Users");

                String txt_firstName = firstName.getText().toString();
                String txt_lastName = lastName.getText().toString();
                String txt_gender = gender.getText().toString();
                String txt_age = age.getText().toString();
                int int_age = Integer.parseInt(txt_age);

                HashMap<String, Object> map = new HashMap<>();
                map.put("firstName", txt_firstName);
                map.put("lastName", txt_lastName);
                map.put("gender", txt_gender);
                map.put("age", int_age);

                users.document(currentUserID).set(map);

                Toast.makeText(addUserProfileActivity.this, "User Added !", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(addUserProfileActivity.this, MainActivity.class));
                finish();

            }
        });


    }
}
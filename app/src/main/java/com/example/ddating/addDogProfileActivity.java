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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class addDogProfileActivity extends AppCompatActivity {

    private EditText dogName;
    private EditText dogType;
    private EditText gender;
    private EditText age;
    private Button addDog;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dog_profile);

        // Variable
        dogName = findViewById(R.id.dogName);
        dogType = findViewById(R.id.dogType);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        addDog = findViewById(R.id.addDog);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = currentUser.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Variable


        // Add Dog Profile in Collection("Users") -> Document -> Collection("Dogs")
        addDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectionReference userDog = db.collection("Users");

                String txt_dogName = dogName.getText().toString();
                String txt_dogType = dogType.getText().toString();
                String txt_gender = gender.getText().toString();
                String txt_age = age.getText().toString();
                int int_age = Integer.parseInt(txt_age);

                HashMap<String, Object> map = new HashMap<>();
                map.put("dogName", txt_dogName);
                map.put("dogType", txt_dogType);
                map.put("gender", txt_gender);
                map.put("age", int_age);

                userDog.document(currentUser.getUid()).collection("Dogs").document().set(map);
                map.clear();

                Toast.makeText(addDogProfileActivity.this, "Dog Added !", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(addDogProfileActivity.this, MainActivity.class));
                finish();

            }
        });
    }
}
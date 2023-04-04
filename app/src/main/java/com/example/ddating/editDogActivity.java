package com.example.ddating;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class editDogActivity extends AppCompatActivity {

    private ImageView image;
    private Button editImage;
    private EditText dogName;
    private EditText dogType;
    private EditText gender;
    private EditText age;
    private Button editDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dog);

        // Vairble
        image = findViewById(R.id.image);
        editImage = findViewById(R.id.editImage);
        dogName = findViewById(R.id.dogName);
        dogType = findViewById(R.id.dogType);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        editDog = findViewById(R.id.editDog);

        // Variable




    }
}
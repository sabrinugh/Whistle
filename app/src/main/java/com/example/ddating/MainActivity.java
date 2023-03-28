package com.example.ddating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable
        setting = findViewById(R.id.setting);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Variable

        // Setting Page
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, settinPage.class));
            }
        });
    }

    //

    //

    // Check Profile Missing
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check User Profile
        DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Message", "User Collected");

                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Log.d("Message", "User Profile found !");

                        // Check Dog Profile
                        db.collection("Users").document(currentUser.getUid()).collection("Dogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Message", "Dog Collected");

                                    if (task.getResult().size() > 0) {
                                        Log.d("Message", "Dogs found");

                                    } else {
                                        Log.d("Message", "No Data");

                                        Toast.makeText(MainActivity.this, "Dog Profile Missing", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, addDogProfileActivity.class));
                                        finish();
                                    }
                                } else {
                                    Log.d("Message", "Dog Collection Error");
                                }
                            }
                        });
                    } else {
                        Log.d("Message", "No Data");

                        Toast.makeText(MainActivity.this, "User Profile Missing", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, addUserProfileActivity.class));
                        finish();
                    }
                } else {
                    Log.d("Message", "User Collection Error");
                }
            }
        });
    }
}
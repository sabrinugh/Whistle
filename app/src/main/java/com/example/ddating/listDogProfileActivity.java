package com.example.ddating;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class listDogProfileActivity extends AppCompatActivity {

    private Button addDog;
    private Button backbtn;

    private ListView listDog;
    private List<String> list_dogID = new ArrayList<String>();
    private List<String> list_dogName = new ArrayList<String>();
    private List<String> list_dogImageURI = new ArrayList<String>();
    private List<Bitmap> list_dogImage = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dog_profile);

        // Variable

        addDog = findViewById(R.id.addDog);
        backbtn = findViewById(R.id.backbtn);

        Boolean dataGet = false;
        listDog = findViewById(R.id.listDog);

        // Variable

        // Get Dog Data
        getDogProfiles();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Click the List View
        clickDogProfile();

        // Add New Dog
        addDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(listDogProfileActivity.this, addDogProfileActivity2.class));
//                finish();
            }
        });

    }

    //

    // Function

    private void clickDogProfile() {

        listDog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Log.d("Message", list_dogID.get(position));
                String dogID = list_dogID.get(position);
                String dogImageURI = list_dogImageURI.get(position);
                Intent intent = new Intent(listDogProfileActivity.this, editDogActivity.class);
                intent.putExtra("dogID", dogID);
                intent.putExtra("dogImageURI", dogImageURI);
                startActivity(intent);

            }
        });

    }

    //

    // Function
    private void getDogProfiles() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(currentUser.getUid()).collection("Dogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Message", "Connect Dogs collection");

                    final int[] position = {0};

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String txt_dogID = document.getId();
                        String txt_dogName = document.getString("dogName");
                        String txt_dogType = document.getString("dogType");
                        String txt_dogGender = document.getString("gender");
                        String txt_dogAge = document.getString("age");
                        String txt_dogImageURI = document.getString("DogImageURI");

//                        list_dogID.add(txt_dogID);
//                        list_dogName.add(txt_dogName);
//                        list_dogImageURI.add(txt_dogImageURI);

                        Log.d("Message", list_dogName.toString());

                        // Get Image
                        try {
                            File localFile = File.createTempFile("DogImage", "jpg");
                            FirebaseStorage.getInstance().getReference().child(txt_dogImageURI).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("Message", "Dog Image Found !");

                                    position[0]++;

                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                    list_dogID.add(txt_dogID);
                                    list_dogName.add(txt_dogName);
                                    list_dogImageURI.add(txt_dogImageURI);
                                    list_dogImage.add(bitmap);

                                    if (task.getResult().size() == position[0]) {
                                        String[] array_dogName = list_dogName.toArray(new String[0]);
                                        String[] array_dogImageURI = list_dogImageURI.toArray(new String[0]);
                                        Bitmap[] array_dogImage = list_dogImage.toArray(new Bitmap[0]);

                                        listDogAdapter listAdapter = new listDogAdapter(getApplicationContext(), array_dogName, array_dogImageURI, array_dogImage);

                                        listDog.setAdapter(listAdapter);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("ERROR", e.toString());
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // End get Image
                    } // End for

                    // Create List View
//                    String[] array_dogName = list_dogName.toArray(new String[0]);
//                    String[] array_dogImageURI = list_dogImageURI.toArray(new String[0]);

//                    listDogAdapter listAdapter = new listDogAdapter(getApplicationContext(), array_dogName, array_dogImageURI);

//                    listDog.setAdapter(listAdapter);

                } else {
                    Toast.makeText(listDogProfileActivity.this, "Data Not Found !", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", "Data Not Found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(listDogProfileActivity.this, "Not Able to Connect Database !", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", e.toString());
            }
        });

    }
}
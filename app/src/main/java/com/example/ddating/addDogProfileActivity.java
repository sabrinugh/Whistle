package com.example.ddating;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

public class addDogProfileActivity extends AppCompatActivity {

    private EditText dogName;
    private EditText dogType;
    private EditText gender;
    private EditText age;
    private Button addDog;
    private Button addImage;
    private ImageView image;
//    private Button backbtn;

    private Uri imageUri;

    private FirebaseUser currentUser;

    // private int dogImageStorageSize;

    private static final int IMAGE_REQUEST = 2;


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
        addImage = findViewById(R.id.addImage);
        image = findViewById(R.id.image);
//        backbtn = findViewById(R.id.backbtn);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = currentUser.getUid();

        // FirebaseFirestore db = FirebaseFirestore.getInstance();

        // irebaseStorage storage = FirebaseStorage.getInstance();

        // Variable
//        backbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        // Add Dog Profile in Collection("Users") -> Document -> Collection("Dogs")
        addDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Storage Image
                upLoadImageAndData();
            }
        });
    }

    // Function
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();

            InputStream inputStream = null;
            try {
                assert imageUri != null;
                inputStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.decodeStream(inputStream);
            image.setImageURI(imageUri);
        }

    }

    private void upLoadImageAndData() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("DogImages");


        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // int dogImageStorageSize = listResult.getItems().size();

                Date date = new Date(System.currentTimeMillis());

                StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("DogImages").child(date.toString());

                // Store Data

                if (imageUri == null) {
                    Toast.makeText(addDogProfileActivity.this, "Can't missing Image ! ", Toast.LENGTH_SHORT).show();
                } else {
                    if (upLoadData(date.toString())) {
                        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("Message", taskSnapshot.toString());

                                Toast.makeText(addDogProfileActivity.this, "Dog Added !", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(addDogProfileActivity.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ERROR", e.toString());
                            }
                        });
                    }
                }


            }
        });

    }

    private boolean upLoadData(String StorageSize) {
        CollectionReference userDog = FirebaseFirestore.getInstance().collection("Users");

        boolean upLoadData_b = true;

        // Get Data
        String txt_dogName = dogName.getText().toString();
        String txt_dogType = dogType.getText().toString();
        String txt_gender = gender.getText().toString();
        String txt_age = age.getText().toString();
        // int int_age = Integer.parseInt(txt_age);

        String txt_dogImageURI = "DogImages/" + StorageSize;

        if (txt_dogName.isEmpty() || txt_dogType.isEmpty() || txt_gender.isEmpty() || txt_age.isEmpty()) {
            Toast.makeText(addDogProfileActivity.this, "Can't missing any input !", Toast.LENGTH_SHORT).show();
            upLoadData_b = false;
            return false;
        }  else {
            try {
                int number = Integer.parseInt(txt_age);

                if (number < 0) {
                    Toast.makeText(addDogProfileActivity.this, "Age at least 0", Toast.LENGTH_SHORT).show();
                    upLoadData_b = false;
                    return false;
                } else if (number > 50) {
                    Toast.makeText(addDogProfileActivity.this, "How can your dog survive over 50 years", Toast.LENGTH_SHORT).show();
                    upLoadData_b = false;
                    return false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(addDogProfileActivity.this, "Integer Only in Age", Toast.LENGTH_SHORT).show();
                upLoadData_b = false;
                return false;
            }
        }


        if (upLoadData_b) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("dogName", txt_dogName);
            map.put("dogType", txt_dogType);
            map.put("gender", txt_gender);
            map.put("age", txt_age);
            map.put("DogImageURI", txt_dogImageURI);

            userDog.document(currentUser.getUid()).collection("Dogs").document().set(map);
            map.clear();
        }

        return true;
    }
}
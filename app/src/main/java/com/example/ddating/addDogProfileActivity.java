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
import java.util.HashMap;

public class addDogProfileActivity extends AppCompatActivity {

    private EditText dogName;
    private EditText dogType;
    private EditText gender;
    private EditText age;
    private Button addDog;
    private Button addImage;
    private ImageView image;

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


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = currentUser.getUid();

        // FirebaseFirestore db = FirebaseFirestore.getInstance();

        // irebaseStorage storage = FirebaseStorage.getInstance();

        // Variable

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

                Toast.makeText(addDogProfileActivity.this, "Dog Added !", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(addDogProfileActivity.this, MainActivity.class));
                finish();

            }
        });
    }

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
                int dogImageStorageSize = listResult.getItems().size();

                StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("DogImages").child(Integer.toString(dogImageStorageSize + 1));

                // Store Data
                upLoadData(dogImageStorageSize + 1);

                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Message", taskSnapshot.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ERROR", e.toString());
                    }
                });
            }
        });

    }

    private void upLoadData(int StorageSize) {
        CollectionReference userDog = FirebaseFirestore.getInstance().collection("Users");

        // Get Data
        String txt_dogName = dogName.getText().toString();
        String txt_dogType = dogType.getText().toString();
        String txt_gender = gender.getText().toString();
        String txt_age = age.getText().toString();
        // int int_age = Integer.parseInt(txt_age);

        String txt_dogImageURI = "DogImages/" + Integer.toString(StorageSize);

        HashMap<String, Object> map = new HashMap<>();
        map.put("dogName", txt_dogName);
        map.put("dogType", txt_dogType);
        map.put("gender", txt_gender);
        map.put("age", txt_age);
        map.put("DogImageURI", txt_dogImageURI);

        userDog.document(currentUser.getUid()).collection("Dogs").document().set(map);
        map.clear();

    }
}
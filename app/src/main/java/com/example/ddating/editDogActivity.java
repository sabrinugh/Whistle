package com.example.ddating;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class editDogActivity extends AppCompatActivity {

    private EditText dogName;
    private EditText dogType;
    private EditText gender;
    private EditText age;

    private ImageView image;

    private Button editDog;
    private Button editImage;
    private Button deleteDog;

    private Button back;

    private Uri imageUri;
    private static final int IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dog);

        // Vairble

        dogName = findViewById(R.id.dogName);
        dogType = findViewById(R.id.dogType);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);

        image = findViewById(R.id.image);

        editImage = findViewById(R.id.editImage);
        editDog = findViewById(R.id.editDog);
        deleteDog = findViewById(R.id.deleteDog);

        back = findViewById(R.id.back);

        Bundle extras = getIntent().getExtras();
        String currentDogID = extras.getString("dogID");
        String dogImageURI = extras.getString("dogImageURI");

//        if (extras != null) {
//            String txt_dogID = extras.getString("dogID");
//            Log.d("Message", currentDogID);
//        }

        // Variable

        // Main()

        // Get Dog Data
        getDogData(currentDogID);

        // Image Click
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });


        // Edit Click
        editDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadNewData(currentDogID, dogImageURI);
            }
        });

        // Delete Clcik
        deleteDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDogProfile(currentDogID, dogImageURI);
            }
        });

        // go back to previous activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(editDogActivity.this, settinPage.class));
            }
        });

    }

    // Delete Dog Profile Functions
    private void deleteDogProfile(String currentDogID, String dogImageURI) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        StorageReference dogImageRef = FirebaseStorage.getInstance().getReference().child(dogImageURI);

        // Remove Image URI and Data
        // Remove Image
        dogImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message", "Image delete Success !");

                // Remove Data
                DocumentReference currentDogProfile = db.collection("Users").document(currentUser.getUid()).collection("Dogs").document(currentDogID);
                currentDogProfile.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Message", "Dog Profile delete Success !");

                        // All Dog Profile Remove
                        // Back To Main Activity
                        Toast.makeText(editDogActivity.this, "All Dog Profile Removed !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editDogActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editDogActivity.this, "Fail to Remove Data", Toast.LENGTH_SHORT).show();
                        Log.d("Message", "Dog Profile delete Failed !");
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editDogActivity.this, "Fail to Remove Image", Toast.LENGTH_SHORT).show();
                Log.d("Message", e.toString());
            }
        });

    }
    // Delete Dog Profile Functions


    // Upload Data Functions
    private void upLoadNewData(String currentDogID, String dogImageURI) {

        String txt_dogName = dogName.getText().toString();
        String txt_dogType = dogType.getText().toString();
        String txt_dogGender = gender.getText().toString();
        String txt_dogAge = age.getText().toString();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        WriteBatch batch = db.batch();

        DocumentReference newDogRef = db.collection("Users").document(currentUser.getUid()).collection("Dogs").document(currentDogID);
        batch.update(newDogRef, "dogName", txt_dogName);
        batch.update(newDogRef, "dogType", txt_dogType);
        batch.update(newDogRef, "gender", txt_dogGender);
        batch.update(newDogRef, "age", txt_dogAge);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(editDogActivity.this, "Dog Profile Updated", Toast.LENGTH_SHORT).show();
                    Log.d("Message", "Data Uploaded");

                    // Upload Image
                    if (imageUri != null) {
                        upLoadImage(dogImageURI);
                    }

                    // Back To Main Activity
                    startActivity(new Intent(editDogActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(editDogActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    Log.d("ERROR", "Not able to upload Data");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR", e.toString());
            }
        });

    }

    // Upload Image Functions
    private void upLoadImage(String dogImageURI) {

        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(dogImageURI);

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

    // Upload Image Functions
    // Upload Data Funcionst

    // Edit Image Functions
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
    // Edit Image Functions

    // Get Dog Data Funtions
    private void getDogData(String currentDogID) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Connect DataBase
        db.collection("Users").document(currentUser.getUid()).collection("Dogs").document(currentDogID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    Log.d("Message", "Data Get");

                    // Read Data
                    String txt_dogName = task.getResult().getString("dogName");
                    String txt_dogType = task.getResult().getString("dogType");
                    String txt_dogGender = task.getResult().getString("gender");
                    String txt_dogAge = task.getResult().getString("age");
                    String txt_dogImageURI = task.getResult().getString("DogImageURI");

                    // Display Data
                    dogName.setText(txt_dogName);
                    dogType.setText(txt_dogType);
                    gender.setText(txt_dogGender);
                    age.setText(txt_dogAge);

                    // Get Image
                    getImage(txt_dogImageURI);


                } else {
                    Toast.makeText(editDogActivity.this, "Data Not Found ! ", Toast.LENGTH_SHORT).show();
                }

            }

        }).addOnFailureListener(new OnFailureListener() { // Fail Connection
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR", e.toString());
            }
        });


    }

    // Get Dog Image Function
    private void getImage(String txt_dogImageURI) {

        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(txt_dogImageURI);

        try {
            File localFile = File.createTempFile("DogImage", "jpg");

            fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Message", "Dog Image Found !");

                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    image.setImageBitmap(bitmap);
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

    }
    // Get Dog Image Function
    // Get Dog Data Funtions

}
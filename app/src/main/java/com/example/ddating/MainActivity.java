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

    private Button logout;
    private Button delete;
    private Button setting;
    private ImageView dogImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable
        logout = findViewById(R.id.logout);
        delete = findViewById(R.id.delete);
        setting = findViewById(R.id.setting);
        dogImage = findViewById(R.id.dogImage);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Variable

        // Setting Page
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, settinPage.class));
            }
        });


        // Get Image
        // getImage();

        // Back to Main Activity and logout
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, startActivity.class));
//                finish();
//            }
//        });

        // Delete all User profile
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the User Email
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                // Sign out
                FirebaseAuth.getInstance().signOut();

                // Collect DataBase
                CollectionReference currentProfile = db.collection("Users");

                // Delete all Data in Collection -> Document -> Collection(Dogs)
                db.collection("Users").document(currentUser.getUid()).collection("Dogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String txt_dogUri = document.getString("DogImageURI");
                                StorageReference desertRef = FirebaseStorage.getInstance().getReference().child(txt_dogUri);

                                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("Message", "Image delete Success !");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Message", "Image delete Failed !");
                                    }
                                });

                                currentProfile.document(currentUser.getUid()).collection("Dogs").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("Message", "Dog Profile delete Success !");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Message", "Dog Profile delete Failed !");
                                    }
                                });
                            }

                            // Delete all Data
                            currentProfile.document(currentUser.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Message", "User Profile delete Success !");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Message", "Dog Profile delete Failed !");
                                }
                            });

                            // Delete User Email and Password
                            currentUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Message", "Account delete Success !");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Message", "Dog Profile delete Failed !");
                                }
                            });

                            Toast.makeText(MainActivity.this, "Account Deleted !", Toast.LENGTH_SHORT).show();

                            // Back to StartActivity
                            startActivity(new Intent(MainActivity.this, startActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Error Delete Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void getImage() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("Users").document(currentUser.getUid()).collection("Dogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("Message", "DOG : " + document.getString("DogImageURI"));

                    String txt_dogUri = document.getString("DogImageURI");
                    if (txt_dogUri != null) {
                        Log.d("Message", txt_dogUri);

                        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(txt_dogUri);

                        try {
                            File localFile = File.createTempFile("DogImage", "jpg");

                            fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("Message", "Dog Image Found !");

                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    dogImage.setImageBitmap(bitmap);
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
                    } else {
                        Log.d("Message", "No Image");
                    }
                }
            }
        });


        // StorageReference pathRef = FirebaseStorage.getInstance().getReference();
    }


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
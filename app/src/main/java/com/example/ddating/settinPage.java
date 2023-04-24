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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class settinPage extends AppCompatActivity {

    private ImageView userImage;
    private ImageView dogImage;
    private TextView userID;
    private Button editPassword;
    private Button editUserPassword;
    private Button editDogProfile;

    private Button logOut;
    private Button deleteAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settin_page);

        // Variable
        // userImage = findViewById(R.id.userImage);
        // dogImage = findViewById(R.id.dogImage);
        userID = findViewById(R.id.userID);
        editPassword = findViewById(R.id.editPassword);
        editUserPassword = findViewById(R.id.editUserProfile);
        editDogProfile = findViewById(R.id.editDogProfile);

        logOut = findViewById(R.id.logOut);
        deleteAccount = findViewById(R.id.deleteAccount);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Variabel


        // Get User Name
        getUserName();

        // Get Image
        // getImage();

        // editPasswordActivity Activity
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(settinPage.this, "Edit Password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(settinPage.this, editPasswordActivity.class));
            }
        });

        // EditUserProfile Activity
        editUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settinPage.this, editUserActivity.class));
            }
        });

        // EditDogProfile Activity
        editDogProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(settinPage.this, listDogProfileActivity.class));
            }
        });

        // Log Out Account
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(settinPage.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(settinPage.this, startActivity.class));
                finish();
            }
        });

        // Delete Account
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the User Email
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                // Sign out
                FirebaseAuth.getInstance().signOut();
                // Collect DataBase
                CollectionReference currentProfile = db.collection("Users");

                currentProfile.document(currentUser.getUid()).collection("Dogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // Check is there have Data or able to collect the Database
                        if (task.isSuccessful()) {

                            // Get all Dog Profile Data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String txt_dogUri = document.getString("DogImageURI");
                                StorageReference desertRef = FirebaseStorage.getInstance().getReference().child(txt_dogUri);

                                // Delete Image URI
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

                                // Delete Dog Profiles in the User Profile
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

                            // Delete the User Profile Data
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

                            // Delete the User Account ( Email and Password )
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

                            Toast.makeText(settinPage.this, "Account Deleted !", Toast.LENGTH_SHORT).show();
                            // Back to startActivity
                            startActivity(new Intent(settinPage.this, startActivity.class));
                            finish();
                        } // End If statement
                        else {
                            Toast.makeText(settinPage.this, "Error Delete Data", Toast.LENGTH_SHORT).show();
                        }
                    } // End OnComplete
                }); // End addOnComplete
            }
        }); // End delete Function


    }

    private void getUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String txt_newUserName = task.getResult().getString("userName");

                userID.setText(txt_newUserName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR", e.toString());
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
    }
}
package com.example.ddating;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;

import java.util.ArrayList;

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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // on below line we are creating variable
    // for our array list and swipe deck.
    private SwipeDeck cardStack;
    private ArrayList<CourseModal> courseModalArrayList;

    private Button setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable
        setting = findViewById(R.id.setting);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Variable

        // Get Swipe Data
        getSwipeData();

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

    // Function

    private void getSwipeData() {
        // on below line we are initializing our array list and swipe deck.
        courseModalArrayList = new ArrayList<>();
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        // on below line we are adding data to our array list.
        courseModalArrayList.add(new CourseModal("", "", "", "", null));
//        courseModalArrayList.add(new CourseModal("Java", "30 days", "20 Tracks", "Java Self Paced Course", R.drawable.uh));
//        courseModalArrayList.add(new CourseModal("Python", "30 days", "20 Tracks", "Python Self Paced Course", R.drawable.patrick));
//        courseModalArrayList.add(new CourseModal("DSA", "30 days", "20 Tracks", "DSA Self Paced Course", R.drawable.zoran));
//

        // Get all Dog Data
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get all User
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> userTask) {
                if (userTask.isSuccessful()) {
                    for (QueryDocumentSnapshot userDocument : userTask.getResult()) {

                        // Skip the current User
                        if (userDocument.getId().equals(currentUser.getUid())) {
                            continue;
                        }

                        // Log.d("Testing", "User : " + userDocument.getId().toString() + " " + currentUser.getUid().toString());


                        // Get all dog in the User
                        db.collection("Users").document(userDocument.getId()).collection("Dogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> dogTask) {
                                if (dogTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot dogDocument : dogTask.getResult()) {

                                        String txt_dogID = dogDocument.getId();
                                        String txt_dogName = dogDocument.getString("dogName");
                                        String txt_dogType = dogDocument.getString("dogType");
                                        String txt_dogGender = dogDocument.getString("gender");
                                        String txt_dogAge = dogDocument.getString("age");
                                        String txt_dogImageURI = dogDocument.getString("DogImageURI");

                                        // Skip if the user not finish their dog profile

                                        if (txt_dogName.isEmpty()) {
                                            continue;
                                        }

                                        // Get Image
                                        try {
                                            File localFile = File.createTempFile("DogImage", "jpg");
                                            FirebaseStorage.getInstance().getReference().child(txt_dogImageURI).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                                    courseModalArrayList.add(new CourseModal(txt_dogName, txt_dogType, txt_dogGender, txt_dogAge, bitmap));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                        // courseModalArrayList.add(new CourseModal(txt_dogName, txt_dogType, txt_dogGender, txt_dogAge, null));
                                    } // End for
                                } // End if
                                else {
                                    Log.d("ERROR", "Swipe Data not able to get");
                                } // End else
                            }
                        });
                        // End get all dog in the User
                    }
                }
            }
        });

        // Get All Dog Data

        // on below line we are creating a variable for our adapter class and passing array list to it.
        final DeckAdapter adapter = new DeckAdapter(courseModalArrayList, this);

        // on below line we are setting adapter to our card stack.
        cardStack.setAdapter(adapter);

        // on below line we are setting event callback to our card stack.
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                // on card swipe left we are displaying a toast message.
                Toast.makeText(MainActivity.this, "Card Swiped Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardSwipedRight(int position) {
                // on card swiped to right we are displaying a toast message.
                Toast.makeText(MainActivity.this, "Card Swiped Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardsDepleted() {
                // this method is called when no card is present
                Toast.makeText(MainActivity.this, "No more courses present", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardActionDown() {
                // this method is called when card is swiped down.
                Log.i("TAG", "CARDS MOVED DOWN");
            }

            @Override
            public void cardActionUp() {
                // this method is called when card is moved up.
                Log.i("TAG", "CARDS MOVED UP");
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

                                    } else { // No Dog Data
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
                    } else { // No User Data
                        Log.d("Message", "No Data");

                        Toast.makeText(MainActivity.this, "User Profile Missing", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, addUserProfileActivity.class));
                        finish();
                    }
                } else { // Can't collection
                    Log.d("Message", "User Collection Error");
                }
            }
        });
    }
}

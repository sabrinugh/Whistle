package com.example.ddating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class settinPage extends AppCompatActivity {

    private ImageView userImage;
    private TextView userID;
    private Button editPassword;

    private Button logOut;
    private Button deleteAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settin_page);

        // Variable
        userImage = findViewById(R.id.userImage);
        userID = findViewById(R.id.userID);
        editPassword = findViewById(R.id.editPassword);

        logOut = findViewById(R.id.logOut);
        deleteAccount = findViewById(R.id.deleteAccount);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Variabel

        // editPassword Activity
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(settinPage.this, "Work In Progress", Toast.LENGTH_SHORT).show();
            }
        });

        // Log Out Account
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(settinPage.this, "Logged Out", Toast.LENGTH_SHORT).show();
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

                


            }
        });



    }
}
package com.example.ddating;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        delete = findViewById(R.id.delete);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, startActivity.class));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseAuth.getInstance().signOut();
                currentuser.delete();

                CollectionReference currentProfile = db.collection("Users");
                currentProfile.document(currentuser.getUid()).delete();

                Toast.makeText(MainActivity.this, "Account Deleted !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, startActivity.class));
                finish();
            }
        });


    }
}
package com.example.ddating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class listDogProfileActivity extends AppCompatActivity {


    private ListView listDog;
    private List<String> list_dogName = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_list_dog_profile);
        setContentView(R.layout.activity_list_dog_profile);

        // Variable

        Boolean dataGet = false;
        listDog = findViewById(R.id.listDog);

        // Variable

        // Get Dog Data
        getDogProfiles();


    }


    // Function
    private void getDogProfiles() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int taskSize;


        db.collection("Users").document(currentUser.getUid()).collection("Dogs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Message", "Connect Dogs collection");

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String txt_dogID = document.getId();
                        String txt_dogName = document.getString("dogName");
                        String txt_dogType = document.getString("dogType");
                        String txt_dogGender = document.getString("gender");
                        String txt_dogAge = document.getString("age");
                        String txt_dogImageURI = document.getString("DogImageURI");

                        list_dogName.add(txt_dogName);

                        Log.d("Message", list_dogName.toString());

                    }

                    String[] array_dogName = list_dogName.toArray(new String[0]);
                    listDogAdapter listAdapter = new listDogAdapter(getApplicationContext(), array_dogName);

                    listDog.setAdapter(listAdapter);


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
package com.example.ddating;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class listDogAdapter extends BaseAdapter {


    private Context context;
    private String list_dogName[];
    private String list_dogImageURI[];
    private LayoutInflater inflater;

    TextView txt_dogName;
    ImageView txt_dogImageURI;

    public listDogAdapter(Context applicationContext, String[] list_dogName, String[] list_dogImageURI) {
        this.context = context;
        this.list_dogName = list_dogName;
        this.list_dogImageURI = list_dogImageURI;
        this.inflater = (LayoutInflater.from(applicationContext)) ;
    }

    @Override
    public int getCount() {
        return list_dogName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.list_dog_item, null);
        txt_dogName = view.findViewById(R.id.listDog_item_name);
        txt_dogImageURI = view.findViewById(R.id.listDog_item_image);

        txt_dogName.setText(list_dogName[position]);

        getImage(list_dogImageURI[position]);

        return view;
    }

    private void getImage(String string_dogImageURI) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(string_dogImageURI);

        try {
            File localFile = File.createTempFile("DogImage", "jpg");

            fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Message", "Dog Image Found !");

                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    txt_dogImageURI.setImageBitmap(bitmap);
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
}

package com.example.ddating;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class listDogAdapter extends BaseAdapter {


    private Context context;
    private String list_dogName[];
    private LayoutInflater inflater;

    public listDogAdapter(Context applicationContext, String[] list_dogName) {
        this.context = context;
        this.list_dogName = list_dogName;
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
        TextView txt_dogName = view.findViewById(R.id.listDog_item_name);
        ImageView txt_dogImageURI = view.findViewById(R.id.listDog_item_image);

        txt_dogName.setText(list_dogName[position]);


        return view;
    }
}

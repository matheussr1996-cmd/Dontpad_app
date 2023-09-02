package com.example.appdontpad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.appdontpad.R;
import com.example.appdontpad.adapter.AdapterPhoto;
import com.example.appdontpad.fragments.Photos_Fragment;
import com.example.appdontpad.fragments.TextFragment;
import com.example.appdontpad.model.Photo;
import com.example.appdontpad.model.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Log";
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private Button buttonPhotos;
    private Button buttonText;
    private List<Text> lsText;
    private List<Photo> lsPhoto;

    private CollectionReference collTextRefence;



    public List<Photo> getLsPhoto() {
        return lsPhoto;
    }

    public List<Text> getLsText() {
        return lsText;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPhotos = findViewById(R.id.btnPhotos);
        buttonText = findViewById(R.id.btnText);

        if (savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TextFragment()).commit();
            buttonPhotos.setBackgroundColor(Color.WHITE);
            buttonPhotos.setTextColor(Color.parseColor("#2DBD65"));
            buttonText.setTextColor(Color.WHITE);
            buttonText.setBackgroundColor(Color.parseColor("#2DBD65"));


        }

        lsPhoto = new ArrayList<Photo>();
        lsText = new ArrayList<Text>();



        /*
        //Imagens teste para GridLayout Imagens
        lsPhoto.add(new Photo(R.drawable.image1,"Exemplo1"));
        lsPhoto.add(new Photo(R.drawable.image2,"Exemplo2"));
        lsPhoto.add(new Photo(R.drawable.a,"Exemplo1"));
        lsPhoto.add(new Photo(R.drawable.b,"Exemplo2"));
        lsPhoto.add(new Photo(R.drawable.c,"Exemplo1"));
        lsPhoto.add(new Photo(R.drawable.d,"Exemplo2"));
        lsPhoto.add(new Photo(R.drawable.e,"Exemplo2"));
        lsPhoto.add(new Photo(R.drawable.f,"Exemplo1"));
        lsPhoto.add(new Photo(R.drawable.g,"Exemplo2"));

        DatabaseReference dataPhoto= referencia.child("Photo");
        */





    }

    public void createFragmets() {



        buttonPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Photos_Fragment()).commit();
                buttonText.setBackgroundColor(Color.WHITE);
                buttonText.setTextColor(Color.parseColor("#2DBD65"));
                buttonPhotos.setTextColor(Color.WHITE);
                buttonPhotos.setBackgroundColor(Color.parseColor("#2DBD65"));



            }
        });

        buttonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TextFragment()).commit();
                buttonPhotos.setBackgroundColor(Color.WHITE);
                buttonPhotos.setTextColor(Color.parseColor("#2DBD65"));
                buttonText.setTextColor(Color.WHITE);
                buttonText.setBackgroundColor(Color.parseColor("#2DBD65"));
            }
        });
    }


    @Override
    protected void onStart() {
        createFragmets();
        super.onStart();
    }
}

package com.example.appdontpad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appdontpad.R;
import com.example.appdontpad.model.Photo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DeletePhotoActivity extends AppCompatActivity implements View.OnLongClickListener{
    private ImageView imageViewDelete;
    private Photo photo2 = new Photo();
    private static final String TAG = "Log";
    private String imageDurl;
    private String ImagePath;
    private String id;
    private DatabaseReference pPostReference;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_photo);
        imageViewDelete= findViewById(R.id.imageViewDelete);
        Intent intent = getIntent();
        if(intent !=null){
            imageDurl = intent.getStringExtra("imageDurl");
            ImagePath = intent.getStringExtra("ImagePath");
            id = intent.getStringExtra("id");
            Glide.with(getApplicationContext()).load(imageDurl).into(imageViewDelete);
            imageViewDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //Dialogo de confirmação

                    AlertDialog.Builder dialog = new AlertDialog.Builder(DeletePhotoActivity.this);
                    dialog.setTitle(R.string.delete);
                    dialog.setMessage(R.string.string_delete_ask);
                    dialog.setIcon(android.R.drawable.ic_dialog_alert);
                    dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            pPostReference = FirebaseDatabase.getInstance().getReference().child("uploads").child(id);
                            pPostReference.removeValue();
                            storageRef = FirebaseStorage.getInstance().getReference().child("uploads/" + ImagePath);
                            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    imageViewDelete.setImageResource(R.color.white);
                                    Toast.makeText(getApplicationContext(),
                                            "Sucesso ao Remover", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            "Erro ao Remover", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //finish();
                        }
                    });
                    dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Toast.makeText(getApplicationContext(),R.string.string_cancel,
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

                    //Criar e exibir Alert Dialog;
                    dialog.create();
                    dialog.show();

                    return false;
                }
            });





        }



    }

    @Override
    public boolean onLongClick(View v) {

        return false;
    }

}

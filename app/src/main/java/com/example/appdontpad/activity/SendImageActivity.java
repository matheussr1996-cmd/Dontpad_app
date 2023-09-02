package com.example.appdontpad.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appdontpad.R;
import com.example.appdontpad.model.Photo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class SendImageActivity extends AppCompatActivity {
    private static final int REQ_CODE_CAMERA = 1001;
    private TextInputEditText imageTagSend;
    private ImageButton buttonPhoto2;
    private ImageView imageViewSend;
    private Button buttonSendImage;
    private ProgressBar progressBar;
    private Bitmap picture;

    private Uri imageUri;

    private StorageReference pStorageref;
    private DatabaseReference pDatabaseRef;

    private StorageTask pUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);
        imageTagSend = findViewById(R.id.imageTagSend);
        buttonPhoto2 = findViewById(R.id.buttonPhoto2);
        imageViewSend = findViewById(R.id.imageViewSend);
        buttonSendImage = findViewById(R.id.buttonSendImage);
        progressBar = findViewById(R.id.progressBar);
        pStorageref = FirebaseStorage.getInstance().getReference("uploads");
        pDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        buttonPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });



        buttonSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pUploadTask !=null && pUploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),
                            R.string.string_upProgress,Toast.LENGTH_SHORT).show();
                }
                uploadFile();
            }
        });



    }

    private void tirarFoto() {
        if (imageTagSend.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_tag),
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQ_CODE_CAMERA);
            } else {
                Toast.makeText(this, getString(R.string.no_camera),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQ_CODE_CAMERA:
                if (requestCode == REQ_CODE_CAMERA && resultCode == Activity.RESULT_OK
                        && data != null && data.getData() != null) {
                    picture = (Bitmap) data.getExtras().get("data");
                    imageViewSend.setImageBitmap(picture);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//////////////////////////////////////////////////////////////////////////////////////
   /* private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }*/

    private void uploadFile() {
        if (picture != null) {
            final String imgId = System.currentTimeMillis()
                    + ".jpg";
            final StorageReference filereference = pStorageref.child(imgId);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
            byte[] bytes = baos.toByteArray();
            pUploadTask = filereference.putBytes(bytes)//upload acontece aqui
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        //Lida com sucessos ou fracasso da operação


                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), R.string.string_sucessful,
                                    Toast.LENGTH_SHORT).show();
                            //Cria Objeto Photo enviando ao construtor texto e Url da imagem

                                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = uri.toString();
                                            String uploadId = pDatabaseRef.push().getKey();
                                            Photo photo = new Photo(imageTagSend.getText().
                                                    toString().trim(), url,imgId,uploadId);
                                            pDatabaseRef.child(uploadId).setValue(photo);
                                            imageViewSend.setImageResource(R.color.white);
                                            imageTagSend.setText("");
                                        }
                                    });
                            //Atribui id no banco de dados e guarda id em uma string

                            //insere imagem no id (upload id)




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                            taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);

                }
            });
        } else {
            Toast.makeText(this, R.string.no_file, Toast.LENGTH_SHORT).show();
        }

    }
}

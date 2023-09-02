package com.example.appdontpad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appdontpad.R;
import com.example.appdontpad.model.Text;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertTextActivity extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private TextInputEditText textTag;
    private EditText textInsert;
    private Button buttonSend;
    String tag;
    String text;
    private int aux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_text);
        textTag = findViewById(R.id.textTag);
        textInsert = findViewById(R.id.textInsert);
        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = textTag.getText().toString();
                text = textInsert.getText().toString();
                text = text.trim();

                if (tag.equals(null) || tag.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.string_notsent, Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        DatabaseReference dataTexto = referencia.child("Text");
                        String uploadId =  dataTexto.push().getKey();
                        Text texto = new Text(text, tag, uploadId);
                        //envia objeto para firebase
                        dataTexto.child(uploadId).setValue(texto);
                        //
                        textTag.setText("");
                        textInsert.setText("");
                        Toast.makeText(getApplicationContext(),
                                R.string.string_sent, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                R.string.string_errorsent, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }
}

package com.example.appdontpad.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appdontpad.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTextActivity extends AppCompatActivity {
    private TextInputEditText contentNote;
    private Button button;
    private Button buttonDelete;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        contentNote = findViewById(R.id.note);
        buttonDelete = findViewById(R.id.buttonDelete);
        button = findViewById(R.id.button);
        Intent intent = getIntent();
        if(intent !=null){
            final String textId = intent.getStringExtra("textId");
            String note = intent.getStringExtra("note");
            contentNote.setText(note);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            referencia.child("Text").child(textId).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(EditTextActivity.this,
                                                    R.string.delete_text, Toast.LENGTH_SHORT).show();
                                            contentNote.setText("");
                                        }
                                    });
                        }

                }
            );

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    referencia.child("Text").child(textId)
                            .child("text")
                            .setValue(contentNote.getText().
                                    toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditTextActivity.this,
                                    R.string.string_edit_result,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });







        }

    }


}

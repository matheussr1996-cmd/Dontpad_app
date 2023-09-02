package com.example.appdontpad.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdontpad.R;
import com.example.appdontpad.RecyclerItemClickListener;
import com.example.appdontpad.activity.EditTextActivity;
import com.example.appdontpad.activity.InsertTextActivity;
import com.example.appdontpad.activity.MainActivity;
import com.example.appdontpad.adapter.AdapterText;
import com.example.appdontpad.model.Text;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TextFragment extends Fragment {
    private static final String TAG = "Log";
    private RecyclerView tRecyclerView;
    private RecyclerView.Adapter tAdapter;
    private List<Text> ltext;
    private Button buttonText;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private TextInputEditText textSearchTag;
    private String stringTag;






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_text,container,false);

        //Se a RecyclerView estiver dentro do layout do fragment, pegue ele direto da View v inflada
        try {
            tRecyclerView = view.findViewById(R.id.recyclerText);
            //Log.d(TAG, "pegou o recycle do layout");
            //Configuração da cor do botão
            buttonText = view.findViewById(R.id.buttonSend);
            textSearchTag = view.findViewById(R.id.textSearchTag);
            buttonText.setBackgroundColor(Color.parseColor("#2DBD65"));
            buttonText.setTextColor(Color.WHITE);

            //Configuração para pesquisar por Tag
            textListener();//método que contém o afterTextChanged

            //Setando Listener e iniciando outra Activity


        } catch (Exception e) {
            Log.d(TAG, "Erro ao pegar o recycle do layout " + e);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        buttonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InsertTextActivity.class);
                startActivity(intent);

            }
        });

        //Preenchimento da lista
        if(ltext!=null) {
            tRecyclerView.setHasFixedSize(true);
            //Log.d(TAG, "definiu setHasFixedSize como true");
        }
        // definindo o LinearLayoutManager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tRecyclerView.setLayoutManager(llm);
        //Log.d(TAG, "definiu LinearLayoutManager");

        //Salvamento da lista
        ltext= ((MainActivity) getActivity()).getLsText();
        //Log.d(TAG, "Salvou a lista no ltext");

        //Setando Adapter para o fragment

        try {

            if (tRecyclerView == null) {
                //Log.d(TAG, "Recycler nulo");
            } else{
                //Criação e Set de Adapter
                tAdapter = new AdapterText(getContext(),ltext);
                tRecyclerView.setAdapter(tAdapter);
                tAdapter.notifyDataSetChanged();
                tRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                        getContext(),
                        tRecyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Intent intent = new Intent(getContext(), EditTextActivity.class);
                                Text text = ltext.get(position);
                                intent.putExtra("textId",text.getId());
                                intent.putExtra("note",text.getText());
                                startActivity(intent);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                ));
                //Log.d(TAG, "Definiu o Adapter");





            }

        }catch (Exception e){
            Log.d(TAG, "Exception Lista nula");
        }



    }

    private void textListener(){
        textSearchTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                stringTag = s.toString();
               // Log.d(TAG, "alterou valor da string para: "+ stringTag);
                //Recuperando informações do database, adicionando a lista e notificando alterações
                final DatabaseReference refText = referencia.child("Text");
                refText.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ltext.clear();
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            Text text = dados.getValue(Text.class);
                            //Log.d(TAG, "valor da String: "+ stringTag);
                            if(text.getTag().equals(stringTag) ){
                                ltext.add(text);
                                //Log.d(TAG, "adicionou item: "+ text.getText());
                                tAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onStop() {
        ltext.clear();
        textSearchTag.setText(null);
        tAdapter.notifyDataSetChanged();
        super.onStop();
    }
}

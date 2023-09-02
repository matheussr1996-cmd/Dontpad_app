package com.example.appdontpad.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdontpad.R;
import com.example.appdontpad.RecyclerItemClickListener;
import com.example.appdontpad.activity.DeletePhotoActivity;
import com.example.appdontpad.activity.MainActivity;
import com.example.appdontpad.activity.SendImageActivity;
import com.example.appdontpad.adapter.AdapterPhoto;
import com.example.appdontpad.model.Photo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photos_Fragment extends Fragment {
    private static final String TAG = "Log";
    private RecyclerView pRecyclerView;
    private RecyclerView.Adapter pAdapter;
    private List<Photo> lphoto;
    private ImageButton buttonPhoto;
    private TextInputEditText imageTag;
    private String tag;

    private DatabaseReference pDatabaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        buttonPhoto = view.findViewById(R.id.buttonPhoto);
        imageTag = view.findViewById(R.id.imageTag);
        pDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        //Configuração para pesquisar por Tag
        textListener();//método que contém o afterTextChanged

        //Se a RecyclerView estiver dentro do layout do fragment, pegue ele direto da View v inflada
        try {
            pRecyclerView = view.findViewById(R.id.recyclerPhoto);
            Log.d(TAG, "pegou o recycle do layout");

        } catch (Exception e) {
            Log.d(TAG, "Erro ao pegar o recycle do layout " + e);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Preenchimento da lista
        pRecyclerView.setHasFixedSize(true);
        Log.d(TAG, "definiu setHasFixedSize como true");

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendImageActivity.class);
                startActivity(intent);
            }
        });

        // definindo o LinearLayoutManager
        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        pRecyclerView.setLayoutManager(llm);
        Log.d(TAG, "definiu GridLayoutManager");


        //Salvamento da lista
        lphoto = ((MainActivity) getActivity()).getLsPhoto();
        Log.d(TAG, "Salvou a lista no lphoto");

        //Setando Adapter para o fragment
        try {


            if (pRecyclerView == null) {
                Log.d(TAG, "Setou o Adapter");
                Log.d(TAG, "Recycler nulo");
            } else {
                //Criação e Set de Adapter
                pAdapter = new AdapterPhoto(getContext(), lphoto);
                pRecyclerView.setAdapter(pAdapter);
                pAdapter.notifyDataSetChanged();
                Log.d(TAG, "Definiu o Adapter");
                //evento de click para exclusão de imagem
                pRecyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(
                                getContext(),
                                pRecyclerView,
                                new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {
                                        Intent intent = new Intent(getContext(), DeletePhotoActivity.class);
                                        Photo photo = lphoto.get(position);
                                        intent.putExtra("imageDurl",photo.getImageUrl());
                                        intent.putExtra("ImagePath",photo.getImagepath());
                                        intent.putExtra("id",photo.getId());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    }
                                })
                );
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception Lista nula");
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void textListener(){
        imageTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tag = imageTag.getText().toString();
                pDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lphoto.clear();
                        for(DataSnapshot dados: dataSnapshot.getChildren()){
                            Photo photo = dados.getValue(Photo.class);
                            if(photo.getTag().equals(imageTag.getText().toString())) {
                                lphoto.add(photo);
                                Log.d(TAG, "adicionou foto: " + photo.getImageUrl());
                                pAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onStop() {
        lphoto.clear();
        imageTag.setText("");
        pAdapter.notifyDataSetChanged();
        super.onStop();
    }
}

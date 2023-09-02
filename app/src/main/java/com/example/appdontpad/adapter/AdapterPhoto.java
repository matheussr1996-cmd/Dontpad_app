package com.example.appdontpad.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdontpad.R;
import com.example.appdontpad.model.Photo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static android.widget.Toast.*;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.ViewHolderPhoto> {
    private Context pcontext;
    private static final String TAG = "Log";
    private List<Photo> lphoto;
    private Bitmap picture;

    public AdapterPhoto(Context context, List<Photo> lphoto) {
        this.pcontext = context;
        this.lphoto = lphoto;
    }

    @NonNull
    @Override
    public ViewHolderPhoto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(pcontext);
        view = layoutInflater.inflate(R.layout.cardview_photos,parent,false);
        return new ViewHolderPhoto(view);



    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPhoto holder, int position) {
        Photo photoCurrent = lphoto.get(position);
        final String photoReference = photoCurrent.getImageUrl();
        Log.d(TAG, "Url da imagem: "+ photoReference);
        Glide.with(pcontext).load(photoReference).into(holder.imagePhoto);

    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "tamanho da lista de fotos: "+ lphoto.size());
        return lphoto.size();

    }



    public static class ViewHolderPhoto extends RecyclerView.ViewHolder {

        ImageView imagePhoto;

        public ViewHolderPhoto(@NonNull View itemView) {
            super(itemView);

            imagePhoto = itemView.findViewById(R.id.imagephoto);
        }
    }
}

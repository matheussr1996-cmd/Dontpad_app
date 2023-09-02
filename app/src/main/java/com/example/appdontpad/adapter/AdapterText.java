package com.example.appdontpad.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdontpad.R;
import com.example.appdontpad.model.Text;

import java.util.List;

public class AdapterText extends RecyclerView.Adapter<AdapterText.ViewHolderText> {
    private Context tcontext;
    private List<Text> ltext;
    private static final String TAG = "Log";

    public AdapterText(Context tcontext, List<Text> ltext) {
        this.tcontext = tcontext;
        this.ltext = ltext;
    }

    @NonNull
    @Override
    public ViewHolderText onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(tcontext);
        view = layoutInflater.inflate(R.layout.cardview_text,parent,false);
        return new ViewHolderText(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderText holder, int position) {

        holder.tag.setText(ltext.get(position).getTag());
        holder.text.setText(ltext.get(position).getText());
        Log.d(TAG, "Texto no Adapter"+ltext.get(position).getText());


    }

    @Override
    public int getItemCount() {
        return ltext.size();
    }

    public class ViewHolderText extends RecyclerView.ViewHolder{
        private TextView tag;
        private TextView text;


            public ViewHolderText(@NonNull View itemView) {
                super(itemView);

                tag = itemView.findViewById(R.id.textViewTag);
                text = itemView.findViewById(R.id.textViewtext);

            }
        }

}

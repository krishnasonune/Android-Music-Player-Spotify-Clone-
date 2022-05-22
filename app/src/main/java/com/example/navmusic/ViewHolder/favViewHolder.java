package com.example.navmusic.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navmusic.Interface.itemClickListener;
import com.example.navmusic.R;

public class favViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView songNAme;
    public itemClickListener listeners;

    public favViewHolder(@NonNull View itemView) {
        super(itemView);
        songNAme = itemView.findViewById(R.id.favName);
    }

    public void setItemClickListener(itemClickListener listener){
        this.listeners = listener;
    }

    @Override
    public void onClick(View view) {
        listeners.onClick(view, getAdapterPosition() ,false);
    }
}

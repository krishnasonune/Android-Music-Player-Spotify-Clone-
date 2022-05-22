package com.example.navmusic.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navmusic.Interface.itemClickListener;
import com.example.navmusic.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView artist, name, url;
    public CircleImageView img;
    public itemClickListener listeners;

    public SongsViewHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.songName);
        artist = (TextView) itemView.findViewById(R.id.artist);
        img = (CircleImageView) itemView.findViewById(R.id.songImage);

    }

    public void setItemClickListener(itemClickListener listener){

        this.listeners = listener;
    }

    @Override
    public void onClick(View view) {
        listeners.onClick(view, getAdapterPosition(), false);
    }
}

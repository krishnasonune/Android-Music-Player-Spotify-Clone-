package com.example.navmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.navmusic.Models.Songs;
import com.example.navmusic.Models.favourites;
import com.example.navmusic.MusicManager.musicMAnager;
import com.example.navmusic.Prevalent.prevalent;
import com.example.navmusic.ViewHolder.SongsViewHolder;
import com.example.navmusic.ViewHolder.favViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class FavouritesActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        recyclerView = (RecyclerView) findViewById(R.id.favList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        ref = FirebaseDatabase.getInstance().getReference().child("Favourites").child(prevalent.Phone);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<favourites> options = new FirebaseRecyclerOptions.Builder<favourites>()
                .setQuery(ref, favourites.class).build();

        FirebaseRecyclerAdapter<favourites, favViewHolder> adapter = new FirebaseRecyclerAdapter<favourites, favViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull favViewHolder holder, int i, @NonNull favourites model) {
                holder.songNAme.setText(model.getSongname());
//                holder.artist.setText(model.getArtist());
//                Picasso.get().load(model.getCover()).into(holder.img);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (musicMAnager.player.isPlaying()) {
                            musicMAnager.player.reset();
                            Intent intent = new Intent(FavouritesActivity.this, PlayMusicActivity.class);
                            intent.putExtra("name", model.getSongname());
                            startActivity(intent);
                        }
                        else if(musicMAnager.bpause()){
                            musicMAnager.player.reset();
                            Intent intent = new Intent(FavouritesActivity.this, PlayMusicActivity.class);
                            intent.putExtra("name", model.getSongname());
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(FavouritesActivity.this, PlayMusicActivity.class);
                            intent.putExtra("name", model.getSongname());
                            startActivity(intent);
                        }
                    }
                });


            }

            @NonNull
            @Override
            public favViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite, parent, false);
                favViewHolder holder = new favViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
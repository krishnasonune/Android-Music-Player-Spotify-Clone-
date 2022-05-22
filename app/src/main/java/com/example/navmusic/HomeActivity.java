package com.example.navmusic;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.navmusic.Interface.itemClickListener;
import com.example.navmusic.Models.Songs;
import com.example.navmusic.MusicManager.musicMAnager;
import com.example.navmusic.Prevalent.prevalent;
import com.example.navmusic.ViewHolder.SongsViewHolder;
import com.example.navmusic.imageSlide.SliderAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navmusic.databinding.ActivityHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private DatabaseReference ref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private CardView cardView;
    private TextView songname, artistname, userName;
    private CircleImageView img;
    private ImageButton close, play, pause;
    AudioManager audioManager, audio;

    SliderView sliderView;
    int[] banners = {
            R.drawable.l1,
            R.drawable.l2,
            R.drawable.l3,
            R.drawable.l4,
            R.drawable.l5,
            R.drawable.l6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sliderView = (SliderView) findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(banners);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        cardView = (CardView) findViewById(R.id.card);
        songname = (TextView) findViewById(R.id.songNameHome);
        artistname = (TextView) findViewById(R.id.artistNameHome);
        userName = (TextView) findViewById(R.id.name_greet);
        img = (CircleImageView) findViewById(R.id.songImageHome);
        close = (ImageButton) findViewById(R.id.closebtn);
        play = (ImageButton) findViewById(R.id.playbtn);
        pause = (ImageButton) findViewById(R.id.pausebtn);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userName.setText("Hey " + prevalent.userName + "!");

        ref = FirebaseDatabase.getInstance().getReference().child("Songs");

        BottomNavigationView navView = findViewById(R.id.nav_view);

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.navigation_logout) {
                    Paper.book().destroy();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                }
                if (id == R.id.navigation_settings) {
                    Intent intent = new Intent(HomeActivity.this, FavouritesActivity.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });


        if(audioManager.isMusicActive()) {
            cardView.setVisibility(View.VISIBLE);
            songname.setText(getIntent().getStringExtra("name"));
            artistname.setText(getIntent().getStringExtra("artist"));
            String url = getIntent().getStringExtra("cover");
            Picasso.get().load(url).into(img);

        }
        else if(musicMAnager.bpause()){
            String name = getIntent().getStringExtra("name");
            String artist = getIntent().getStringExtra("artist");
            String url = getIntent().getStringExtra("cover");
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(artist) && !TextUtils.isEmpty(url)) {
                cardView.setVisibility(View.VISIBLE);
                songname.setText(name);
                artistname.setText(artist);
                Picasso.get().load(url).into(img);
            }
            else{
                cardView.setVisibility(View.INVISIBLE);
            }
        }
        else{
            cardView.setVisibility(View.INVISIBLE);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioManager.isMusicActive() || musicMAnager.bpause()) {
                    musicMAnager.player.reset();
                    cardView.setVisibility(View.INVISIBLE);
                }
                else{
                    musicMAnager.player.reset();
                    cardView.setVisibility(View.INVISIBLE);
                }
            }
        });

        adjustCardButton();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicMAnager.player.isPlaying()) {
                    musicMAnager.playPlayer();
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.INVISIBLE);
                }
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicMAnager.bplay()) {
                    musicMAnager.pausePlayer();
                    pause.setVisibility(View.INVISIBLE);
                    play.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void adjustCardButton() {
        if (musicMAnager.player.isPlaying()){
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
        }
        else if(!musicMAnager.player.isPlaying()){
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
        }
        else{
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Songs> options = new FirebaseRecyclerOptions.Builder<Songs>()
                .setQuery(ref, Songs.class).build();

        FirebaseRecyclerAdapter<Songs, SongsViewHolder> adapter = new FirebaseRecyclerAdapter<Songs, SongsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SongsViewHolder holder, int i, @NonNull Songs model) {
                holder.name.setText(model.getName());
                holder.artist.setText(model.getArtist());
                Picasso.get().load(model.getCover()).into(holder.img);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (musicMAnager.player.isPlaying()) {
                            musicMAnager.player.reset();
                            Intent intent = new Intent(HomeActivity.this, PlayMusicActivity.class);
                            intent.putExtra("name", model.getName());
                            startActivity(intent);
                        }
                        else if(musicMAnager.bpause()){
                            musicMAnager.player.reset();
                            Intent intent = new Intent(HomeActivity.this, PlayMusicActivity.class);
                            intent.putExtra("name", model.getName());
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(HomeActivity.this, PlayMusicActivity.class);
                            intent.putExtra("name", model.getName());
                            startActivity(intent);
                        }
                    }
                });

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }

            @NonNull
            @Override
            public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_layout, parent, false);
                SongsViewHolder holder = new SongsViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
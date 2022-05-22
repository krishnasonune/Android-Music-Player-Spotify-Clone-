package com.example.navmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.navmusic.MusicManager.musicMAnager;
import com.example.navmusic.Prevalent.prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PlayMusicActivity extends AppCompatActivity {

    private FloatingActionButton btn, sBwd, sFwd;
    private FloatingActionButton btn2;
    private ImageButton norm, like, back, addList, addCheckList;
    private ImageView CoverImage;
    private TextView Name, artName;
    private TextView time, gh;
    private DatabaseReference ref, refLike, refUnlike, refupdateLike, refaddtoFav, refunFav, refUpdateList;
    AudioManager audioManager;
    SeekBar seekBar;
    String name = "";
    String Cover = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        CoverImage = (ImageView) findViewById(R.id.cover);
        Name = (TextView) findViewById(R.id.name);
        artName = (TextView) findViewById(R.id.artname);
        time = (TextView) findViewById(R.id.timer);
        gh = (TextView) findViewById(R.id.vh);
        norm = (ImageButton) findViewById(R.id.normbtn);
        like = (ImageButton) findViewById(R.id.likebtn);
        back = (ImageButton) findViewById(R.id.back);
        addList = (ImageButton) findViewById(R.id.Addtofavbtn);
        addCheckList = (ImageButton) findViewById(R.id.Addedtolistbtn);
        Animation ranim = (Animation) AnimationUtils.loadAnimation(this, R.anim.anim);
        btn = (FloatingActionButton) findViewById(R.id.playe1);
        btn2 = (FloatingActionButton) findViewById(R.id.playe2);
        sFwd = (FloatingActionButton) findViewById(R.id.Sfwd10);
        sBwd = (FloatingActionButton) findViewById(R.id.Sbwd10);
        seekBar = (SeekBar) findViewById(R.id.seek);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            name = getIntent().getExtras().get("name").toString();

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicMAnager.bpause()) {
                    musicMAnager.playPlayer();
                    btn.setVisibility(View.INVISIBLE);
                    btn2.setVisibility(View.VISIBLE);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicMAnager.bplay()){
                    musicMAnager.pausePlayer();
                    btn2.setVisibility(View.INVISIBLE);
                    btn.setVisibility(View.VISIBLE);
                }
            }
        });


        norm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.animate().rotationYBy(180f).setDuration(600);
                like.setVisibility(View.VISIBLE);
                norm.setVisibility(View.INVISIBLE);
                addLike(name);
            }
        });


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                norm.animate().rotationYBy(-180f).setDuration(600);
                norm.setVisibility(View.VISIBLE);
                like.setVisibility(View.INVISIBLE);
                removeLike(name);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicMAnager.player.isPlaying()){
                    Intent intent = new Intent(PlayMusicActivity.this, HomeActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("cover", Cover);
                    intent.putExtra("artist", artName.getText().toString());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(PlayMusicActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });


        updateLike(name);

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCheckList.animate().rotationYBy(360f).setDuration(600);
                addCheckList.setVisibility(View.VISIBLE);
                addList.setVisibility(View.INVISIBLE);
                addToFav(name);
            }
        });

        addCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addList.animate().rotationYBy(360f).setDuration(600);
                addList.setVisibility(View.VISIBLE);
                addCheckList.setVisibility(View.INVISIBLE);
                removeFromFav(name);
            }
        });

        updateAddPlaylistButton(name);

    }

    private void updateAddPlaylistButton(String name) {
        refUpdateList = FirebaseDatabase.getInstance().getReference().child("Favourites").child(prevalent.Phone).child(name);
        refUpdateList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        addList.setVisibility(View.INVISIBLE);
                        addCheckList.setVisibility(View.VISIBLE);
                    }
                else{
                        addList.setVisibility(View.VISIBLE);
                        addCheckList.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void removeFromFav(String name) {
        refunFav = FirebaseDatabase.getInstance().getReference().child("Favourites").child(prevalent.Phone).child(name);
        refunFav.removeValue();
        Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
    }

    private void addToFav(String name) {
        refaddtoFav = FirebaseDatabase.getInstance().getReference().child("Favourites").child(prevalent.Phone).child(name);
        HashMap<String, Object> addtoList = new HashMap<>();
        addtoList.put("songname", name);
        refaddtoFav.updateChildren(addtoList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PlayMusicActivity.this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PlayMusicActivity.this, "error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLike(String name) {
        refupdateLike = FirebaseDatabase.getInstance().getReference().child("Likes").child(name).child(prevalent.Phone);
        refupdateLike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    like.setVisibility(View.VISIBLE);
                    norm.setVisibility(View.INVISIBLE);
                }else{
                    like.setVisibility(View.INVISIBLE);
                    norm.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void removeLike(String name) {
        refUnlike = FirebaseDatabase.getInstance().getReference().child("Likes").child(name).child(prevalent.Phone);
        refUnlike.removeValue();
    }

    private void addLike(String name) {
        refLike = FirebaseDatabase.getInstance().getReference().child("Likes").child(name).child(prevalent.Phone);
        String liked = "Like";
        HashMap<String, Object> LikeIt = new HashMap<>();
        LikeIt.put("like", liked);
        refLike.updateChildren(LikeIt).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PlayMusicActivity.this, "Music Liked", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PlayMusicActivity.this, "error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getAudioDetails();

    }

    private void getAudioDetails() {
        ref = FirebaseDatabase.getInstance().getReference().child("Songs").child(name);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.child("url").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                Cover = snapshot.child("cover").getValue().toString();
                String artist = snapshot.child("artist").getValue().toString();

                Picasso.get().load(Cover).into(CoverImage);
                Name.setText(name);
                artName.setText(artist);
                playAudio(url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void playAudio(String url) {

        musicMAnager.getPlayer(url);
        seekBar.setMax(musicMAnager.player.getDuration());
        time.setText(createTimerLabel(musicMAnager.getDuration()));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(musicMAnager.getCurrentPos());
                gh.setText(createTimerLabel(musicMAnager.getCurrentPos()));
            }}, 0, 100);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    musicMAnager.player.seekTo(i);
                    musicMAnager.player.start();
                }
                sBwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicMAnager.player.seekTo(i - 10000);
                    }
                });

                sFwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicMAnager.player.seekTo((i + 10000));
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                btn2.setVisibility(View.INVISIBLE);
                btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                btn.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.VISIBLE);
            }
        });

    }

    public String createTimerLabel (int duration) {
        String timerLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        timerLabel += min + ":";
        if (sec < 10) timerLabel += "0";
        timerLabel += sec;
        return timerLabel;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (musicMAnager.player.isPlaying()){
            Intent intent = new Intent(PlayMusicActivity.this, HomeActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("cover", Cover);
            intent.putExtra("artist", artName.getText().toString());
            startActivity(intent);
        }
        else if(musicMAnager.bpause()){
            Intent intent = new Intent(PlayMusicActivity.this, HomeActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("cover", Cover);
            intent.putExtra("artist", artName.getText().toString());
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(PlayMusicActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
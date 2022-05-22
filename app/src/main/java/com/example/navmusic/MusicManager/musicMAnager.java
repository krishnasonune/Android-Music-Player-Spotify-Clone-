package com.example.navmusic.MusicManager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.navmusic.PlayMusicActivity;

import java.io.IOException;

public class musicMAnager {
    public static MediaPlayer player = new MediaPlayer();

    public static MediaPlayer getPlayer(String url) {

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(url);
            player.prepare();
            player.start();
            player.getCurrentPosition();
            player.getDuration();
        }
        catch (IOException e) {

        }

        return player;
    }
    public static MediaPlayer stopPlayer(){
        if (player.isPlaying()) {
            player.stop();
        }
        return player;
    }

    public static MediaPlayer pausePlayer(){
        if (player.isPlaying()) {
            player.pause();
        }
        return player;
    }

    public static MediaPlayer playPlayer(){
        if (!player.isPlaying()) {
            player.start();
        }
        return player;
    }

    public static boolean bplay(){
        if(!player.isPlaying()){
            player.start();
        }
        return true;
    }

    public static Boolean bpause(){
        if(player.isPlaying()){
            player.pause();
        }
        return true;
    }

    public static int getCurrentPos(){
        return player.getCurrentPosition();

    }

    public static int getDuration(){
        return player.getDuration();

    }


}

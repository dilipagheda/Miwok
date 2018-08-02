package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class Helper {
    private static final String TAG = "Helper";

    public static void releaseMediaPlayer(MediaPlayer mediaPlayer){
            if(mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer=null;
            }
        }


}

package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;    private static final String TAG = "ColorsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);

        audioManager=this.getSystemService(AudioManager.class);
        audioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch(focusChange){
                    case AudioManager.AUDIOFOCUS_LOSS:
                        Log.d(TAG,"AudioFocus-Loss");
                        Helper.releaseMediaPlayer(mediaPlayer);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        Log.d(TAG,"AUDIOFOCUS_LOSS_TRANSIENT");
                        mediaPlayer.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        Log.d(TAG,"AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                        mediaPlayer.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN:
                        Log.d(TAG,"AUDIOFOCUS_GAIN");
                        mediaPlayer.start();
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        Log.d(TAG,"AUDIOFOCUS_GAIN_TRANSIENT");
                        mediaPlayer.start();
                        break;
                    default:
                        Log.d(TAG,"default - "+focusChange);
                        break;
                }
            }
        };
        mCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG,"Release Media Player");
                Helper.releaseMediaPlayer(mp);
                Log.d(TAG,"Abandon Audio Focus");
                audioManager.abandonAudioFocus(audioFocusChangeListener);
            }
        };
        //declare words array
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("weṭeṭṭi","red",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("chokokki","green",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("ṭakaakki","brown",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("ṭopoppi","gray",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("kululli","black",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("kelelli","white",R.drawable.color_white,R.raw.color_white));
        words.add(new Word("ṭopiisә","dusty yellow",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("chiwiiṭә","mustard yellow",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));

        WordsArrayAdapter itemsAdapter = new WordsArrayAdapter(this, R.layout.list_item, words,R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Helper.releaseMediaPlayer(mediaPlayer);

                int result = audioManager.requestAudioFocus(audioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    Log.d(TAG,"AUDIOFOCUS_REQUEST_GRANTED - start playing");
                    mediaPlayer = MediaPlayer.create(view.getContext(), words.get(position).getAudioResource());

                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }
    @Override
    protected void onStop(){
        super.onStop();
        Helper.releaseMediaPlayer(mediaPlayer);
    }
}

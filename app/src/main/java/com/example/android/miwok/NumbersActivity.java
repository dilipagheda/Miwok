package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.*;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private static final String TAG = "NumbersActivity";
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

        words.add(new Word("lutti","one",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("ottiko","two",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("tolookosu","three",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("oyyisa","four",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("massokka","five",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("temmokka","six",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("kenekaku","seven",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("kawinta","eight",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("wo'e","nine",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("na'aacha","ten",R.drawable.number_ten,R.raw.number_ten));

        WordsArrayAdapter itemsAdapter = new WordsArrayAdapter(this, R.layout.list_item, words,R.color.category_numbers);

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

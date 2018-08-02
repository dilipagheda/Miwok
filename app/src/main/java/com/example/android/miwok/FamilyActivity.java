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

public class FamilyActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;    private static final String TAG = "FamilyActivity";

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

        words.add(new Word("әpә","father",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("әṭa","mother",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("angsi","son",R.drawable.family_son,R.raw.family_son));
        words.add(new Word("tune","daughter",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("taachi","older brother",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("chalitti","younger brother",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("teṭe","older sister",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("kolliti","younger sister",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("ama","grandmother",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("paapa","grandfather",R.drawable.family_grandfather,R.raw.family_grandfather));


        WordsArrayAdapter itemsAdapter = new WordsArrayAdapter(this, R.layout.list_item, words,R.color.category_family);

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

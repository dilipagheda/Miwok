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

public class PhrasesActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private MediaPlayer.OnCompletionListener mCompletionListener;
    private static final String TAG = "PhrasesActivity";

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

        words.add(new Word("minto wuksus","Where are you going?",R.raw.phrase_where_are_you_going));
        words.add(new Word("tinnә oyaase'nә","What is your name?",R.raw.phrase_what_is_your_name));
        words.add(new Word("oyaaset...","My name is...",R.raw.phrase_my_name_is));
        words.add(new Word("michәksәs?","How are you feeling?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("kuchi achit","I’m feeling good.",R.raw.phrase_im_feeling_good));
        words.add(new Word("әәnәs'aa?","Are you coming?",R.raw.phrase_are_you_coming));
        words.add(new Word("hәә’ әәnәm","Yes, I’m coming.",R.raw.phrase_yes_im_coming));
        words.add(new Word("әәnәm","I’m coming.",R.raw.phrase_im_coming));
        words.add(new Word("yoowutis","Let’s go.",R.raw.phrase_lets_go));
        words.add(new Word("әnni'nem","Come here.",R.raw.phrase_come_here));


        WordsArrayAdapter itemsAdapter = new WordsArrayAdapter(this, R.layout.list_item, words,R.color.category_phrases);

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

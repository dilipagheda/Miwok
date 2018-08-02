package com.example.android.miwok;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsArrayAdapter extends ArrayAdapter<Word> {

    ArrayList<Word> words;
    int resource;
    Context mContext;
    int mColorResourceId;

    public WordsArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Word> objects, int mColorResourceId) {
        super(context, 0, objects);
        this.mColorResourceId = mColorResourceId;
        words = objects;
        this.mContext = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem==null){
            listItem = LayoutInflater.from(mContext).inflate(resource,parent,false);
        }
        //set background color of listItem
        LinearLayout textViewLayout = (LinearLayout) listItem.findViewById(R.id.textViewLayout);
        textViewLayout.setBackgroundColor(ContextCompat.getColor(getContext(), mColorResourceId));
        final Word w = words.get(position);
        TextView a = (TextView) listItem.findViewById(R.id.miwok_label);
        a.setText(w.getMivokTranslation());
        TextView b = (TextView) listItem.findViewById(R.id.english_label);
        b.setText(w.getDefaultTranslation());
        ImageView img = (ImageView) listItem.findViewById(R.id.image);
        ImageView playicon = (ImageView) listItem.findViewById(R.id.playicon);
        playicon.setBackgroundColor(ContextCompat.getColor(getContext(), mColorResourceId));


        if(!w.hasImage()){
            img.setVisibility(View.GONE);

        }else{
            img.setVisibility(View.VISIBLE);
            img.setImageResource(w.getImageResource());
        }


        return listItem;

    }
}

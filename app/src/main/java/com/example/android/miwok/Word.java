package com.example.android.miwok;

public class Word {
    private static final int NO_IMAGE_PROVIDED=-1;

    private String mivok_translation;
    private String default_translation;
    private int image_resource = NO_IMAGE_PROVIDED;
    private int audio_resource=0;

    Word(String mivok_translation, String default_translation,int audio_resource){
        this.mivok_translation = mivok_translation;
        this.default_translation = default_translation;
        this.audio_resource = audio_resource;

    }


    Word(String mivok_translation, String default_translation,int image_resource,int audio_resource){
        this(mivok_translation,default_translation,audio_resource);
        this.image_resource = image_resource;
    }

    public String getMivokTranslation() {
        return mivok_translation;
    }

    public String getDefaultTranslation() {
        return default_translation;
    }

    public int getImageResource() {
        return image_resource;
    }
    public boolean hasImage(){
        return image_resource!=NO_IMAGE_PROVIDED;
    }

    public int getAudioResource() {
        return audio_resource;
    }
}


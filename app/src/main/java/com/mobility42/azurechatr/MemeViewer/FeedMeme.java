package com.mobility42.azurechatr.MemeViewer;

import android.graphics.drawable.Drawable;

/**
 * Created by Francesca on 11/10/2015.
 */
public class FeedMeme {

    private Drawable meme1;
    private Drawable meme2;

    public FeedMeme(Drawable d1, Drawable d2) {
        meme1 = d1;
        meme2 = d2;
    }

    public Drawable getMeme1() {
        return meme1;
    }

    public void setMeme1(Drawable meme1) {
        this.meme1 = meme1;
    }

    public Drawable getMeme2() {
        return meme2;
    }

    public void setMeme2(Drawable meme2) {
        this.meme2 = meme2;
    }
}

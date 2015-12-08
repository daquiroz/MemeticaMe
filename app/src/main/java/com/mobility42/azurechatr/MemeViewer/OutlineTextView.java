package com.mobility42.azurechatr.MemeViewer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by alberto on 8/12/15.
 */
public class OutlineTextView extends EditText {

    public OutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public OutlineTextView(Context context) {
        super(context);

    }

    public OutlineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }
    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            super.draw(canvas);
        }
    }

}

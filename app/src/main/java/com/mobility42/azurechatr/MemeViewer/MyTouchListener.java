package com.mobility42.azurechatr.MemeViewer;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Francesca on 12/7/2015.
 */
public final class MyTouchListener implements View.OnTouchListener{

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}

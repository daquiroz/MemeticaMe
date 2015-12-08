package com.mobility42.azurechatr.MemeViewer;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Francesca on 12/7/2015.
 */
public class MyDragListener implements View.OnDragListener{

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                View view = (View) event.getLocalState();
                view.setX(event.getX());
                view.setY(event.getY());
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                DrawingView container = (DrawingView) v;
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            default:
                break;
        }
        return true;
    }
}

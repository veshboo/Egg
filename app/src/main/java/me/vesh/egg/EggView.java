package me.vesh.egg;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class EggView extends View {
    private static final int NUMBER_OF_EGGS = 7;

    private final Egg[] mEggs;

    public EggView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mEggs = new Egg[NUMBER_OF_EGGS];
        for (int i = 0; i < mEggs.length; i++) {
            mEggs[i] = new Egg(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Egg egg : mEggs) {
            egg.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        for (Egg egg : mEggs) {
            egg.changeViewSize(w, h);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (Egg egg : mEggs) {
            egg.onTouchEvent(event);
        }
        return true;
    }

    void resume() {
        for (Egg egg : mEggs) {
            egg.resume();
        }
    }

    void pause() {
        for (Egg egg : mEggs) {
            egg.pause();
        }
    }

}

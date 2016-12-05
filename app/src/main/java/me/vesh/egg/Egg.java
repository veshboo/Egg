package me.vesh.egg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

class Egg implements Runnable {

    // Hit radius ouch < slip < or miss
    // Based on Egg bitmap size (That is, Chicken state has same ouch and slip radius)
    private static final int OUCH_RADIUS = 60;
    private static final int SLIP_RADIUS = 120;

    // Ouch chicken lasting duration
    private static final long OUCH_DURATION = 7_000_000_000L; // unit ns

    // For the motion of egg
    private static final Random POSITION_RANDOM = new Random();
    private static final Handler POSITION_HANDLER = new Handler();

    // Container View
    private final View mView;
    private int mViewWidth;
    private int mViewHeight;

    // Egg bitmap
    private final Bitmap mEggBitmap;
    private final int mEggWidth;
    private final int mEggHeight;

    // Chicken bitmap
    private final Bitmap mChickBitmap;
    private final int mChickWidth;
    private final int mChickHeight;

    // Position and hit status
    private int mX = 0;
    private int mY = 0;
    private boolean mOuch = false; // true when Ouch and back to false after some duration
    private long mOuchStart = 0;

    @SuppressWarnings("deprecation") // getDrawable
    private Bitmap getBitmap(int id) {
        return ((BitmapDrawable) mView.getResources().getDrawable(id)).getBitmap();
    }

    Egg(View view) {
        this.mView = view; // view width and height are known in changeViewSize

        mEggBitmap = getBitmap(R.drawable.egg);
        mEggWidth = mEggBitmap.getWidth();
        mEggHeight = mEggBitmap.getHeight();

        mChickBitmap = getBitmap(R.drawable.chicken);
        mChickWidth = mChickBitmap.getWidth();
        mChickHeight = mChickBitmap.getHeight();
    }

    // Determines position near center of view not to put egg out of view
    void changeViewSize(int w, int h) {
        mViewWidth = w;
        mViewHeight = h;
        mX = POSITION_RANDOM.nextInt(mViewWidth - mEggWidth) + mEggWidth / 2;
        mY = POSITION_RANDOM.nextInt(mViewHeight - mEggHeight) + mEggHeight / 2;
    }

    void resume() {
        POSITION_HANDLER.postDelayed(this, 100);
    }

    void pause() {
        POSITION_HANDLER.removeCallbacks(this);
    }

    void draw(Canvas canvas) {
        if (mOuch) {
            canvas.drawBitmap(mChickBitmap, mX - mChickWidth / 2, mY - mChickHeight / 2, null);
        } else {
            canvas.drawBitmap(mEggBitmap, mX - mEggWidth / 2, mY - mEggHeight / 2, null);
        }
    }

    @Override
    public void run() {
        if (mOuch) {
            if (System.nanoTime() - mOuchStart > OUCH_DURATION) {
                mOuch = false;
                mOuchStart = 0;
            }
        }
        int dX = POSITION_RANDOM.nextInt(21) - 10;
        int dY = POSITION_RANDOM.nextInt(21) - 10;
        ensureBound(mX + dX, mY + dY);
        POSITION_HANDLER.postDelayed(this, 50);
    }

    boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN &&
                event.getPointerCount() == 1) {
            int x = (int) event.getX(0);
            int y = (int) event.getY(0);
            // distance egg's center and touch point
            int dist = (int) Math.sqrt((mX - x) * (mX - x) + (mY - y) * (mY - y));
            if (dist < OUCH_RADIUS) {
                mOuch = true;
                mOuchStart = System.nanoTime();
            } else {
                if (dist < SLIP_RADIUS) {
                    slip(x, y, dist);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // Slips away the egg from touched point
    private void slip(int x, int y, int dist) {
        float distRatio = (float) SLIP_RADIUS / dist;
        int newX = (int) ((mX - x) * distRatio * 1.5f) + x; // * 1.5f for slip some what far away
        int newY = (int) ((mY - y) * distRatio * 1.5f) + y;
        ensureBound(newX, newY);
    }

    private void ensureBound(int newX, int newY) {
        boolean changed = false;
        if (newX - mEggWidth / 2 >= 0 && newX + mEggWidth / 2 < mViewWidth) {
            mX = newX;
            changed = true;
        }
        if (newY - mEggHeight / 2 >= 0 && newY + mEggHeight / 2 < mViewHeight) {
            mY = newY;
            changed = true;
        }
        if (changed) {
            mView.postInvalidate();
        }
    }

}

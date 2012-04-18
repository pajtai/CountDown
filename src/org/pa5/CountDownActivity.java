package org.pa5;

import org.pa5.MotionHandler.CountdownListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * This activity implements a graphic count down timer.
 * The timer uses a Handler to calculate and time the motion of the count down numbers.
 * Each number is a TextView. The parent is a RelativeView.
 * 
 */
public class CountDownActivity extends Activity implements OnClickListener, CountdownListener
{
    private MotionHandler mHandler;
    private View mStart;
    public static final int TIME_FOR_ONE_NUMBER_MS = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mStart = findViewById(R.id.start);
        setStartButtonClickListener();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            // We have to create the motion handler here, since we need to be able to access the width of the parent
            createMotionHandler();
        }
    }

    private AnimationSet createAnimationSet()
    {
        View parent = findViewById(R.id.parent);
        // Movement to center
        Animation animation1 = new TranslateAnimation(0, parent.getWidth() / (5 * 2), 0, 0);
        animation1.setDuration(TIME_FOR_ONE_NUMBER_MS / 2);
        animation1.setStartOffset(0);
        // Scale to center
        Animation animation2 = new ScaleAnimation(1, 5, 1, 5);
        animation2.setDuration(TIME_FOR_ONE_NUMBER_MS / 2);
        animation2.setStartOffset(0);
        // Movement from center
        Animation animation3 = new TranslateAnimation(0, 1.5f * parent.getWidth(), 0, 0);
        animation3.setDuration(TIME_FOR_ONE_NUMBER_MS / 2);
        animation3.setStartOffset(TIME_FOR_ONE_NUMBER_MS / 2);
        Animation animation4 = new ScaleAnimation(1, 0.5f, 1, 0.5f);
        animation4.setDuration(TIME_FOR_ONE_NUMBER_MS / 2);
        animation4.setStartOffset(TIME_FOR_ONE_NUMBER_MS / 2);
        // Interpolator
        AccelerateDecelerateInterpolator ad = new AccelerateDecelerateInterpolator();
        // Set interpolators
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(ad);
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        set.addAnimation(animation3);
        set.addAnimation(animation4);
        set.setFillEnabled(true);
        set.setFillBefore(false);
        set.setFillAfter(true);
        return set;
    }

    private void createMotionHandler()
    {
        TextView theNumber = (TextView) findViewById(R.id.the_number);
        mHandler = new MotionHandler(theNumber, createAnimationSet(), TIME_FOR_ONE_NUMBER_MS, this);
    }

    private void setStartButtonClickListener()
    {
        mStart.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        view.setVisibility(View.GONE);
        mHandler.sendEmptyMessage(MotionHandler.MESSAGE_START_NUMBER);
    }

    public void countdownDone()
    {
        mStart.setVisibility(View.VISIBLE);
    }
}
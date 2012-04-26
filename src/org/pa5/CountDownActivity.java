package org.pa5;

import org.pa5.MotionHandler.CountdownListener;
import org.pa5.MotionHandler.HandlerTimes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

/**
 * This activity implements a graphic count down timer.
 * The timer uses a Handler to calculate and time the motion of the count down numbers.
 * Each number is a TextView. The parent is a RelativeView.
 * 
 */
public class CountDownActivity extends Activity implements OnClickListener, CountdownListener
{
    public static final int TIME_FOR_ONE_NUMBER_MS = 3000;
    public static final String COUNTDOWN_SAVE = "Countdown save";
    public static final String GOING = "Going";
    public static final String STOPPED_AT = "Stopped at";
    public static final String NEXT_AT = "Next at";
    public static final String CURRENT = "Current number";
    private MotionHandler mHandler;
    private View mStart;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mStart = findViewById(R.id.start);
        setStartButtonClickListener();
    }

    private void loadStateOfCountDown()
    {
        SharedPreferences timePrefs = getSharedPreferences(COUNTDOWN_SAVE, 0);
        if (timePrefs.getBoolean(GOING, false))
        {
            long stopped = timePrefs.getLong(STOPPED_AT, 0);
            long next = timePrefs.getLong(NEXT_AT, 0);
            if (0 != stopped && 0 != next)
                restartCountdown(stopped, next, timePrefs.getInt(CURRENT, 0));
        }
    }

    /**
     * Stopped and next are in milliseconds.
     */
    private void restartCountdown(long stopped, long next, int current)
    {
        // TODO 0 - restart at appropriate point in time / location on screen
        // for now let's restart from the left and don't worry about how long the state's been saved
        mHandler.setCurrentNumber(current);
        beginCountDown(mStart);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            // We have to create the motion handler here, since we need to be able to access the width of the parent
            createMotionHandler();
            loadStateOfCountDown();
        }
    }

    private AnimationSet createAnimationSet()
    {
        View parent = findViewById(R.id.parent);
        // Movement to center
        Animation animation1 = new NumberAnimation(parent.getWidth(), 5);
        animation1.setDuration(TIME_FOR_ONE_NUMBER_MS);
        animation1.setStartOffset(0);
        // Interpolator
        AccelerateDecelerateInterpolator ad = new AccelerateDecelerateInterpolator();
        // Set interpolators
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(ad);
        set.addAnimation(animation1);
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
        // If we're clicking on the count down we want to start from the top, not from any sort of saved state
        mHandler.setCurrentNumber(MotionHandler.DEFAULT_START_NUMBER);
        beginCountDown(view);
    }

    public void beginCountDown(View view)
    {
        view.setVisibility(View.GONE);
        mHandler.sendEmptyMessage(MotionHandler.MESSAGE_START_NUMBER);
    }

    public void countdownDone()
    {
        mStart.setVisibility(View.VISIBLE);
    }

    // This is the method that gets called before the activity is taken off line, so
    // we save everything here, so it can be restarted seamlessly
    @Override
    protected void onPause()
    {
        super.onPause();
        saveStateOfCountDown();
    }

    private void saveStateOfCountDown()
    {
        HandlerTimes times = mHandler.onPause();
        SharedPreferences timePrefs = getSharedPreferences(COUNTDOWN_SAVE, 0);
        SharedPreferences.Editor editor = timePrefs.edit();
        if (null == times)
        {
            editor.putBoolean(GOING, false);
        }
        else
        {
            // Save the state of the count down if it is going
            editor.putBoolean(GOING, true);
            editor.putLong(STOPPED_AT, times.stoppedAt);
            editor.putLong(NEXT_AT, times.nextAt);
            editor.putInt(CURRENT, times.currentNumber);
        }
        editor.commit();
    }
}
package org.pa5;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.TextView;

public class MotionHandler extends Handler
{
    public interface CountdownListener
    {
        public void countdownDone();
    }

    public static class HandlerTimes
    {
        public HandlerTimes(long stopTime, long nextTime, int current)
        {
            stoppedAt = stopTime;
            nextAt = nextTime;
            currentNumber = current;
        }

        public long stoppedAt;
        public long nextAt;
        public int currentNumber;
    }

    public static final int DEFAULT_START_NUMBER = 10;
    public static final int MINIMUM_NUMBER = 0;
    public static final int MESSAGE_START_NUMBER = 1;
    public static final int MESSAGE_UPDATE_NUMBER = 2;
    private TextView mNumberView;
    private AnimationSet mAnimationSet;
    private int mTimeForOneNumberMS;
    private int mCurrentNumber;
    private CountdownListener mListener;
    private Message mMessage;

    public MotionHandler(TextView theNumber, AnimationSet set, int timeForOneNumberMs, CountdownListener listener)
    {
        mNumberView = theNumber;
        mAnimationSet = set;
        mTimeForOneNumberMS = timeForOneNumberMs;
        mCurrentNumber = DEFAULT_START_NUMBER;
        mListener = listener;
    }

    public HandlerTimes onPause()
    {
        HandlerTimes times = null;
        if (null != mMessage)
        {
            // Messages use uptimeMillis as their timestamp
            times = new HandlerTimes(SystemClock.uptimeMillis(), mMessage.getWhen(), mCurrentNumber);
            this.removeMessages(MESSAGE_UPDATE_NUMBER);
        }
        return times;
    }

    /**
     * This method can be used to start the count down from a number other than the default,
     * or to set the number to a known value at any point, for example after the listener is fired.
     * 
     */
    public void setCurrentNumber(int currentNumber)
    {
        mCurrentNumber = currentNumber;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case MESSAGE_START_NUMBER:
                updateNumberView();
                makeNumberVisible();
                mNumberView.startAnimation(mAnimationSet);
                mMessage = msg = obtainMessage(MESSAGE_UPDATE_NUMBER);
                sendMessageDelayed(msg, mTimeForOneNumberMS);
                break;
            case MESSAGE_UPDATE_NUMBER:
                hideNumber();
                decrementNumber();
                if (mCurrentNumber < MINIMUM_NUMBER)
                {
                    setCurrentNumber(DEFAULT_START_NUMBER);
                    mListener.countdownDone();
                }
                else
                {
                    sendEmptyMessage(MESSAGE_START_NUMBER);
                }
        }
    }

    private void updateNumberView()
    {
        mNumberView.setText("" + mCurrentNumber);
    }

    private void decrementNumber()
    {
        --mCurrentNumber;
    }

    private void makeNumberVisible()
    {
        mNumberView.setVisibility(View.VISIBLE);
    }

    private void hideNumber()
    {
        mNumberView.setVisibility(View.GONE);
    }
}

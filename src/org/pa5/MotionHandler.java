package org.pa5;

import org.pa5.movements.MovementStrategy;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MotionHandler extends Handler
{
    public interface CountdownListener
    {
        public void countdownDone();
    }

    public static final int DEFAULT_START_NUMBER = 10;
    public static final int MINIMUM_NUMBER = 0;
    public static final int START_NUMBER = 1;
    public static final int UPDATE_NUMBER = 2;
    public static final int REFRESH_SPEED_MS = 250;
    private RelativeTextView mNumberView;
    private MovementStrategy mMotion;
    private RelativeLayout mParent;
    private int mCurrentNumber;
    private CountdownListener mListener;

    /**
     * The containing parent has to be a RelativeLayout. This is because it will make updating
     * the motion handler with new functionality in the future much easier.
     * 
     * The horizontal motion of the number is controlled using the left margin.
     */
    public MotionHandler(TextView theNumber, RelativeLayout containingParent, MovementStrategy movementStrategy, CountdownListener listener)
    {
        mNumberView = new RelativeTextView(theNumber);
        mCurrentNumber = DEFAULT_START_NUMBER;
        mMotion = movementStrategy;
        mParent = containingParent;
        mListener = listener;
    }

    /**
     * This method can be used to start the count down from a number other than the default,
     * or to set the number to a known value at any point.
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
            case START_NUMBER:
                updateNumberView();
                makeNumberVisible();
                moveNumberTo(0);
                msg = obtainMessage(UPDATE_NUMBER);
                sendMessageDelayed(msg, REFRESH_SPEED_MS);
                break;
            case UPDATE_NUMBER:
                int oldPosition = mNumberView.leftMargin();
                int newPosition = mMotion.newPosition(oldPosition);
                moveNumberTo(newPosition);
                Log.d("MOTION", "new position is: " + newPosition);
                if (numberStillVisible())
                {
                    msg = obtainMessage(UPDATE_NUMBER);
                    sendMessageDelayed(msg, REFRESH_SPEED_MS);
                }
                else
                {
                    decrementNumber();
                    if (mCurrentNumber < MINIMUM_NUMBER)
                    {
                        // Reset number for future use
                        mCurrentNumber = DEFAULT_START_NUMBER;
                        hideNumber();
                        mListener.countdownDone();
                    }
                    else
                    {
                        msg = obtainMessage(START_NUMBER);
                        sendMessageDelayed(msg, REFRESH_SPEED_MS);
                    }
                }
                break;
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

    public void moveNumberTo(int leftMargin)
    {
        mNumberView.setLeftMargin(leftMargin);
    }

    private void makeNumberVisible()
    {
        mNumberView.setVisibility(View.VISIBLE);
    }

    private void hideNumber()
    {
        mNumberView.setVisibility(View.GONE);
    }

    private boolean numberStillVisible()
    {
        return mNumberView.leftMargin() < mParent.getWidth();
    }
}

package org.pa5;

import org.pa5.movements.MovementStrategy;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MotionHandler extends Handler
{
    public static final int START_NUMBER = 1;
    public static final int UPDATE_NUMBER = 2;
    public static final int REFRESH_SPEED_MS = 250;
    private RelativeTextView mNumber;
    private MovementStrategy mMotion;
    private RelativeLayout mParent;

    /**
     * The containing parent has to be a RelativeLayout. This is because it will make updating
     * the motion handler with new functionality in the future much easier.
     * 
     * The horizontal motion of the number is controlled using the left margin.
     */
    public MotionHandler(TextView theNumber, RelativeLayout containingParent, MovementStrategy movementStrategy)
    {
        mNumber = new RelativeTextView(theNumber);
        mMotion = movementStrategy;
        mParent = containingParent;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case START_NUMBER:
                makeNumberVisible();
                msg = obtainMessage(UPDATE_NUMBER);
                sendMessageDelayed(msg, REFRESH_SPEED_MS);
                break;
            case UPDATE_NUMBER:
                if (numberStillVisible())
                {
                    msg = obtainMessage(UPDATE_NUMBER);
                    sendMessageDelayed(msg, REFRESH_SPEED_MS);
                }
                else
                {
                    msg = obtainMessage(START_NUMBER);
                    sendMessageDelayed(msg, REFRESH_SPEED_MS);
                }
                break;
        }
    }

    private void makeNumberVisible()
    {
        mNumber.setVisibility(View.VISIBLE);
    }

    private boolean numberStillVisible()
    {
        return mNumber.leftMargin() < mParent.getWidth();
    }
}

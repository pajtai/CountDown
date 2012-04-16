package org.pa5;

import org.pa5.MotionHandler.CountdownListener;
import org.pa5.movements.Linear;
import org.pa5.movements.MovementStrategy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This activity implements a graphic count down timer.
 * The timer uses a Handler to calculate and time the motion of the count down numbers.
 * The handler uses the MovementStrategy interface to make its movement calculations.
 * Each number is a TextView. The parent is a RelativeView.
 * 
 */
public class CountDownActivity extends Activity implements OnClickListener, CountdownListener
{
    public final static int SPEED = 20;
    private MotionHandler mHandler;
    private View mStart;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mStart = findViewById(R.id.start);
        createMotionHandler();
        setStartButtonClickListener();
    }

    private void createMotionHandler()
    {
        TextView theNumber = (TextView) findViewById(R.id.the_number);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.parent);
        MovementStrategy motion = new Linear(SPEED);
        mHandler = new MotionHandler(theNumber, parent, motion, this);
    }

    private void setStartButtonClickListener()
    {
        mStart.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        view.setVisibility(View.GONE);
        mHandler.sendEmptyMessage(MotionHandler.START_NUMBER);
    }

    public void countdownDone()
    {
        mStart.setVisibility(View.VISIBLE);
    }
}
package org.pa5;

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
public class CountDownActivity extends Activity implements OnClickListener
{
    public final static int SPEED = 5;
    private MotionHandler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createMotionHandler();
        setStartButtonClickListener();
    }

    private void createMotionHandler()
    {
        TextView theNumber = (TextView) findViewById(R.id.the_number);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.parent);
        MovementStrategy motion = new Linear(SPEED);
        mHandler = new MotionHandler(theNumber, parent, motion);
    }

    private void setStartButtonClickListener()
    {
        findViewById(R.id.start).setOnClickListener(this);
    }

    public void onClick(View view)
    {
        view.setVisibility(View.GONE);
    }
}
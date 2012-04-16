package org.pa5;

import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * A convenience class for dealing with TextViews inside of RelativeLayouts.
 * The main use for this class is easily accessing the layout parameter properties,
 * but all needed wrapper methods were thrown in as well.
 * 
 */
public class RelativeTextView
{
    private TextView mView;
    private RelativeLayout.LayoutParams mParams;

    public RelativeTextView(TextView view)
    {
        mView = view;
    }

    public void setVisibility(int visibility)
    {
        mView.setVisibility(visibility);
    }

    public int leftMargin()
    {
        return getParams().leftMargin;
    }

    private LayoutParams getParams()
    {
        return (null == mParams) ? (LayoutParams) mView.getLayoutParams() : mParams;
    }
}

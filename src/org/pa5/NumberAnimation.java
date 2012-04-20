package org.pa5;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * This class hasn't been attached to the project yet. It will be a custom animation that handles number movement and size.
 * 
 * @author Peter
 * 
 */
public class NumberAnimation extends Animation
{
    final public static float MINIMUM = 3;
    private int mHorizontal;
    private int mScaling;

    public NumberAnimation(int horizontalMovement, int scaling)
    {
        mHorizontal = horizontalMovement;
        mScaling = scaling;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        double time = 2 * Math.PI * interpolatedTime;
        float currentScale = (float) (mScaling * (1 - Math.cos(time)));
        currentScale = (currentScale < MINIMUM) ? MINIMUM : currentScale;
        Matrix matrix = t.getMatrix();
        matrix.preScale(currentScale, currentScale);
        matrix.postTranslate(mHorizontal * interpolatedTime, 0);
    }
}

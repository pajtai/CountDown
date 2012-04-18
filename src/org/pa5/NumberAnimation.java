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
    final public static float BEGINNING = 0f;
    final public static float ENDING = 1f;
    private int mHorizontal;
    private int mVertical;

    public NumberAnimation(int horizontalMovement, int verticalMovement)
    {
        mHorizontal = horizontalMovement;
        mVertical = verticalMovement;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        Matrix matrix = t.getMatrix();
        matrix.postTranslate(mHorizontal * interpolatedTime, mVertical * interpolatedTime);
    }
}

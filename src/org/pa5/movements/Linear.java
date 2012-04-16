package org.pa5.movements;

public class Linear implements MovementStrategy
{
    private int mSpeed;

    public Linear(int speed)
    {
        mSpeed = speed;
    }

    public int newPosition(int oldPosition)
    {
        return oldPosition + mSpeed;
    }
}

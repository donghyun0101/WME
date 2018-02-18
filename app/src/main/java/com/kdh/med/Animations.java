package com.kdh.med;

import android.view.animation.*;

/**
 * Created by KDH on 2018-02-18.
 */

public class Animations
{
    public TranslateAnimation LeftToRightSet()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -8.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f);

        ts.setDuration(1);
        ts.setFillAfter(true);

        return ts;
    }

    public TranslateAnimation LeftToRight()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, -8.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f);

        ts.setDuration(400);
        ts.setFillAfter(true);

        return ts;
    }

    public TranslateAnimation RightToLeftSet()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 8.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f);

        ts.setDuration(1);
        ts.setFillAfter(true);

        return ts;
    }

    public TranslateAnimation RightToLeft()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 8.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f);

        ts.setDuration(400);
        ts.setFillAfter(true);

        return ts;
    }

    public TranslateAnimation BottomToTopSet()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 8.0f);

        ts.setDuration(1);
        ts.setFillAfter(true);

        return ts;
    }

    public TranslateAnimation BottomToTop()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 8.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f);

        ts.setDuration(400);
        ts.setFillAfter(true);

        return ts;
    }

    public TranslateAnimation TopToBottomSet()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -8.0f);

        ts.setDuration(1);
        ts.setFillAfter(true);

        return ts;
    }

    public TranslateAnimation TopToBottom()
    {
        TranslateAnimation ts = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, -8.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f);

        ts.setDuration(400);
        ts.setFillAfter(true);

        return ts;
    }

    public ScaleAnimation centerSet()
    {
        ScaleAnimation sc = new ScaleAnimation(
                Animation.RELATIVE_TO_PARENT, -8.0f,
                Animation.RELATIVE_TO_PARENT, -8.0f,
                Animation.RELATIVE_TO_PARENT, -8.0f,
                Animation.RELATIVE_TO_PARENT, -8.0f);

        sc.setDuration(1);
        sc.setFillAfter(true);

        return sc;
    }

    public ScaleAnimation center()
    {
        ScaleAnimation sc = new ScaleAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f);

        sc.setDuration(400);
        sc.setFillAfter(true);

        return sc;
    }
}

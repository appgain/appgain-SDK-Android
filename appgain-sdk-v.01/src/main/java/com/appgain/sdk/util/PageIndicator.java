package com.appgain.sdk.util;


import android.support.v4.view.ViewPager;

/**
 * Created by sherin on 4/22/2015.
 * A PageIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.
 */

public interface PageIndicator extends ViewPager.OnPageChangeListener {
    /**
     * Bind the indicator to QuestionHeader ViewPager.
     *
     * @param view
     */
    void setViewPager(ViewPager view);

    /**
     * Bind the indicator to QuestionHeader ViewPager.
     *
     * @param view
     * @param initialPosition
     */
    void setViewPager(ViewPager view, int initialPosition);

    /**
     * <p>Set the current page of both the ViewPager and indicator.</p>
     *
     * <p>This <strong>must</strong> be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     *
     * @param item
     */
    void setCurrentItem(int item);

    /**
     * Set QuestionHeader page change listener which will receive forwarded events.
     *
     * @param listener
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    /**
     * Notify the indicator that the fragment list has changed.
     */
    void notifyDataSetChanged();
}
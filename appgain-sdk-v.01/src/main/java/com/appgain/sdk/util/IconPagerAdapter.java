package com.appgain.sdk.util;

/**
 * Created by sherin on 4/22/2015.
 */
public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}
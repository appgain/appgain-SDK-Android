package com.appgain.sdk.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Work on 29/06/2015.
 */
public class ViewId {

    private final static AtomicInteger seq = new AtomicInteger(Integer.MAX_VALUE);

    public static int getUniqueId() {
        return seq.decrementAndGet();
    }
}
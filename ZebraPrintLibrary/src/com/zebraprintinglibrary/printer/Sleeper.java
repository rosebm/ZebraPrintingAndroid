package com.zebraprintinglibrary.printer;

import android.util.Log;

public class Sleeper {

    private static final String TAG = "Sleeper";

    private Sleeper() {

    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}

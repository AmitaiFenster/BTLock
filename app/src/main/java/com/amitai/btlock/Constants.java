package com.amitai.btlock;

public class Constants {

    /**
     * SCAN_PERIOD to specify how many milliseconds to scan. for example, =5000 is to Stops
     * scanning after 5 seconds.
     */
    public static final long SCAN_PERIOD = 10000;
    /**
     * Use this Tag for all Log prints in the context of DevicesFragment.
     */
    public static final String TAG_DevicesFragment = "DevicesFragment";
    /**
     * Use this constant to ask for permission for the Location.
     */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 3;
    /**
     * Use this Tag as a request code to create an Activity that it's purpose is is to enable
     * Location.
     */
    public static final int REQUEST_ENABLE_LOCATION = 2;
}

package com.amitai.btlock;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * static class containing functions for setting up bluetooth permissions and turning on Bluetooth.
 */
public class BluetoothHelper {

    /**
     * Method to turn on Bluetooth.
     * <p/>
     * Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled, fire
     * an intent to display a dialog asking the user to grant permission to enable it.
     *
     * @param activity Context activity
     * @return true if Bluetooth was on and false otherwise.
     */
    public static boolean enableBluetooth(Activity activity) {
        grantLocationPermission(activity);

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        //Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }

    /**
     * @param activity
     * @return [@link BluetoothAdapter}
     */
    public static BluetoothAdapter getBluetoothAdapter(Activity activity) {
        return ((BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }


    /**
     * check if location permission is granted. if not, prompt the user to grant location permission.
     *
     * @param activity Context activity
     * @return true if location permission is granted, and false otherwise.
     */
    private static boolean grantLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission
                .ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_LOCATION);
            Toast.makeText(activity, "Please enable permission", Toast
                    .LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * check if location is turned on (enabled). if not, start an activity
     * to prompt the user to turn on location.
     *
     * @param activity Context activity
     * @return true if location is enabled, and false otherwise.
     */
    private static boolean enableLocation(Activity activity) {
        grantLocationPermission(activity);
        //Open location Settings for user to manually enable.
        LocationManager manager = (LocationManager) activity.getSystemService(Context
                .LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(enableLocationIntent,
                    Constants.REQUEST_ENABLE_LOCATION);
            return false;
        }
        return true;
    }

    /**
     * @param activity
     * @return true is location is enabled and false otherwise.
     */
    public static boolean isLocationEnabled(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return false;
        LocationManager manager = (LocationManager) activity.getSystemService(Context
                .LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return false;
        return true;
    }
}

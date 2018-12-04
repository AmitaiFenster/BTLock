package com.amitai.btlock;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 * Bluetooth Scanning process for discovering Bluetooth devices.
 */
public class BluetoothScan {

    private final Activity activity;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            mLeDeviceListAdapter.addDevice(device, result.getRssi(), result.getScanRecord()
                    .getBytes());
            mLeDeviceListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.i(Constants.TAG_DavicesScan, "Scan Faild!");
        }
    };
    //Components for Bluetooth scanning
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler = new Handler();
    private BluetoothLeScanner mBluetoothLeScanner;
    private Button buttonScan;

    public BluetoothScan(Activity activity) {
        mBluetoothAdapter = BluetoothHelper.getBluetoothAdapter(activity);
        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        mLeDeviceListAdapter = new LeDeviceListAdapter(activity);
        this.activity = activity;
    }

    public BluetoothScan(Activity activity, Button scanButton){
        this(activity);
        this.buttonScan = scanButton;
    }

    /**
     * @return {@link LeDeviceListAdapter}
     */
    public LeDeviceListAdapter getDeviceListAdapter() {
        return mLeDeviceListAdapter;
    }

    /**
     * @return true if bluetooth is scanning and false otherwise.
     */
    public boolean isScanning() {
        return mScanning;
    }

    /**
     * Toggle scanning. If bluetooth is scanning - scanning will stop.
     * If bluetooth is not scanning - scanning will start.
     */
    public void toggleScan() {
        scanLeDevice(!mScanning);
    }

    /**
     * @param enable true to start scan and stop after scan period,
     */
    public void scanLeDevice(final boolean enable) {
        if (mBluetoothLeScanner == null) {
            Toast.makeText(activity, "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        BleScan(false);
                        buttonScan.setText("Scan");
                    }
                }
            }, Constants.SCAN_PERIOD);
            BleScan(true);
            buttonScan.setText("Scanning");
        } else {
            BleScan(false);
            buttonScan.setText("Scan");
        }
    }

    /**
     * @param scan true to scan, and false to stop scanning for Bluetooth LE devices.
     */
    private void BleScan(boolean scan) {
        if (scan && !mScanning) {
            mScanning = true;
            mBluetoothLeScanner.startScan(mScanCallback);
            Log.i(Constants.TAG_DavicesScan, "Started LE scan");
        } else if (!scan && mScanning) {
            mScanning = false;
            mBluetoothLeScanner.stopScan(mScanCallback);
            Log.i(Constants.TAG_DavicesScan, "Stopped LE scan");
        }
    }


//    public interface InteractionListener {
//        void onScanRetult();
//    }
}

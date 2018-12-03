package com.amitai.btlock;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DevicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DevicesFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    View rootView;
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
            //TODO: set log
            Log.i(Constants.TAG_DevicesFragment, "Scan Faild!");
        }
    };
    private String mParam1;
    private ArrayList<String> devArrayList = new ArrayList<>();
    private ListView myList;
    //Components for Bluetooth scanning
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler = new Handler();
    private BluetoothLeScanner mBluetoothLeScanner;
    private Button buttonScan;

    public DevicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DevicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DevicesFragment newInstance(String param1) {
        DevicesFragment fragment = new DevicesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        mBluetoothAdapter = BluetoothHelper.setupBluetooth(getActivity());

        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_devices, container, false);
        viewSetup();
        return rootView;
    }

    private void viewSetup() {
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(getActivity());
        // Setting up scanned devices ListView
        myList = (ListView) rootView.findViewById(R.id.devicesList);
        myList.setAdapter(mLeDeviceListAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UnlockActivity.startUnlockActivity(getActivity(), nfcUID);
//                TODO: Run Bluetooth relay unlocking.
            }
        });

        buttonScan = (Button) rootView.findViewById(R.id.scanButton);
        buttonScan.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if (mScanning) {
                    scanLeDevice(false);
                } else {
                    mLeDeviceListAdapter.clear();
                    scanLeDevice(true);
                }
            }
        });
    }

    /**
     * @param enable true to start scan and stop after scan period,
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        BleScan(false);
                    }
                }
            }, Constants.SCAN_PERIOD);
            BleScan(true);
        } else {
            BleScan(false);
        }
    }

    /**
     * @param scan true to scan, and false to stop scanning for Bluetooth LE devices.
     */
    private void BleScan(boolean scan) {
        if (scan && !mScanning) {
            buttonScan.setText("Scanning");
            mScanning = true;
            mBluetoothLeScanner.startScan(mScanCallback);
            //TODO: set log
//            Log.i(Constants.TAG_UnlockService, "Started LE scan");
        } else if (!scan && mScanning) {
            buttonScan.setText("Scan");
            mScanning = false;
            mBluetoothLeScanner.stopScan(mScanCallback);
//            Log.i(Constants.TAG_UnlockService, "Stopped LE scan");
        }
    }


}

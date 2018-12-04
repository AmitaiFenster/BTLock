package com.amitai.btlock;


import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


// * Use the {@link DevicesFragment#newInstance} factory method to
//        * create an instance of this fragment.
/**
 * A {@link Fragment} holding a list of available Bluetooth devices.
 */
public class DevicesFragment extends Fragment {

    //TODO: Organize code, comment, divide into modules. only then - next stage. connecting.

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    View rootView;
    private LeDeviceListAdapter mLeDeviceListAdapter;

    private String mParam1;
    private ListView myList;
    //Components for Bluetooth scanning
    private Button buttonScan;
    private BluetoothScan mBluetoothScan;

    public DevicesFragment() {
        // Required empty public constructor
    }

    // use this method if arguments are needed.
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @return A new instance of fragment DevicesFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static DevicesFragment newInstance(String param1) {
//        DevicesFragment fragment = new DevicesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        BluetoothHelper.enableBluetooth(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_devices, container, false);
        viewSetup();
        return rootView;
    }

    /**
     * initialize the list view (that is used to show nearby devices) and the Scan button
     */
    private void viewSetup() {
        buttonScan = (Button) rootView.findViewById(R.id.scanButton);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!mBluetoothScan.isScanning())
                    mLeDeviceListAdapter.clear();
                mBluetoothScan.toggleScan();
            }
        });

        mBluetoothScan = new BluetoothScan(getActivity(), buttonScan);
        // Initializes list view adapter.
        mLeDeviceListAdapter = mBluetoothScan.getDeviceListAdapter();
        // Setting up scanned devices ListView
        myList = (ListView) rootView.findViewById(R.id.devicesList);
        myList.setAdapter(mLeDeviceListAdapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                if (device == null) return;
                if (mBluetoothScan.isScanning()) {
                    mBluetoothScan.scanLeDevice(false);
                }
//                UnlockActivity.startUnlockActivity(getActivity(), nfcUID);
//                TODO: Run Bluetooth relay unlocking.
            }
        });
    }


}

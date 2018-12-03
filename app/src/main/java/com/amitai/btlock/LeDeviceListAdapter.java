package com.amitai.btlock;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for holding Bluetooth devices found through scanning.
 */
public class LeDeviceListAdapter extends BaseAdapter {
    /**
     * {@link ArrayList} of the Bluetooth devices (each item is by the type {@link
     * BluetoothDevice}
     */
    private ArrayList<BluetoothDevice> mLeDevices;
    /**
     * {@link ArrayList} of the received signal strength indicator of the Bluetooth devices.
     */
    private ArrayList<Integer> rssis;
    /**
     * The records of each Bluetooth device. each record is a <code>array of bytes</code>.
     */
    private ArrayList<byte[]> bRecord;
    /**
     * {@link LayoutInflater} that is used to inflate the Views needed by this adapter.
     */
    private LayoutInflater mInflator;

    /**
     * Constructor that initializes the adapters arrayLists and initializes the {@link
     * LayoutInflater}
     * @param activity
     */
    public LeDeviceListAdapter(Activity activity) {
        super();
        mLeDevices = new ArrayList<BluetoothDevice>();
        rssis = new ArrayList<Integer>();
        bRecord = new ArrayList<byte[]>();
        mInflator = activity.getLayoutInflater();
    }

    /**
     * Adding a new device to the devices list in the adapter.
     *
     * @param device the new bluetooth device to be added.
     * @param rs     the received signal strength indicator on the new Bluetooth device.
     * @param record
     */
    public void addDevice(BluetoothDevice device, int rs, byte[] record) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
            rssis.add(rs);
            bRecord.add(record);
        }
    }

    /**
     * @param position position in which the user clicked.
     * @return {@link BluetoothDevice} - the bluetooth device corresponding to the item the
     * user clicked.
     */
    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    /**
     * Clear all the data in the adapter.
     */
    public void clear() {
        mLeDevices.clear();
        rssis.clear();
        bRecord.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.device_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.deviceName.setTextColor(Color.BLUE);
        BluetoothDevice device = mLeDevices.get(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0) {

            viewHolder.deviceName.setText(deviceName);
        } else
            viewHolder.deviceName.setText(R.string.unknown_device);
        viewHolder.deviceAddress.setText(device.getAddress() + "  RSSI:" + String.valueOf
                (rssis.get(i)));
        //viewHolder.deviceAddress.setText(ByteToString(bRecord.get(i)));

        return view;
    }

    /**
     * this ViewHolder class holds two TextViews.
     */
    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}


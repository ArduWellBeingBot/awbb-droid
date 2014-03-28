/**
 * AWBB Droid - Android manager for AWBB.
 * 
 * Copyright (c) 2014 Benoit Garrigues <bgarrigues@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package awbb.droid.bluno;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import awbb.droid.R;

/**
 * Device list adapter.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class DeviceListAdapter extends BaseAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceListAdapter.class);

    private ArrayList<BluetoothDevice> devices;
    private LayoutInflater inflator;

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    /**
     * Constructor.
     */
    public DeviceListAdapter(Context context) {
        devices = new ArrayList<BluetoothDevice>();
        inflator = ((Activity) context).getLayoutInflater();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return devices.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int i) {
        return devices.get(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = inflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            LOGGER.debug("mInflator.inflate  getView");
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BluetoothDevice device = devices.get(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
        } else {
            viewHolder.deviceName.setText(R.string.unknown_device);
        }
        viewHolder.deviceAddress.setText(device.getAddress());

        return view;
    }

    /**
     * 
     * @param device
     */
    public void addDevice(BluetoothDevice device) {
        if (!devices.contains(device)) {
            devices.add(device);
        }
    }

    /**
     * 
     * @param position
     * @return
     */
    public BluetoothDevice getDevice(int position) {
        return devices.get(position);
    }

    /**
     * 
     */
    public void clear() {
        devices.clear();
    }

}

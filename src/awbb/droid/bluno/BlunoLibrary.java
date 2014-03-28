package awbb.droid.bluno;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import awbb.droid.R;

/**
 *
 */
public class BlunoLibrary {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlunoLibrary.class);

    private Context context;
    private BlunoAdapter adapter;

    /**
     * Constructor.
     * 
     * @param theContext
     * @param theAdapter
     */
    public BlunoLibrary(Context theContext, BlunoAdapter theAdapter) {
        context = theContext;
        adapter = theAdapter;
    }

    /**
     * Send data.
     * 
     * @param theString
     */
    public void serialSend(String theString) {
        if (mConnectionState == ConnectionStateEnum.isConnected) {
            mSCharacteristic.setValue(theString);
            mBluetoothLeService.writeCharacteristic(mSCharacteristic);
        }
    }

    private int mBaudrate = 115200; // set the default baud rate to 115200

    byte[] mBaudrateBuffer = { 0x32, 0x00, (byte) (mBaudrate & 0xFF), (byte) ((mBaudrate >> 8) & 0xFF),
            (byte) ((mBaudrate >> 16) & 0xFF), 0x00 };;

    /**
     * Set connexion speed.
     * 
     * @param baud
     */
    public void serialBegin(int baud) {
        mBaudrate = baud;
        mBaudrateBuffer[2] = (byte) (mBaudrate & 0xFF);
        mBaudrateBuffer[3] = (byte) ((mBaudrate >> 8) & 0xFF);
        mBaudrateBuffer[4] = (byte) ((mBaudrate >> 16) & 0xFF);

    }

    private static BluetoothGattCharacteristic mSCharacteristic, mModelNumberCharacteristic, mSerialPortCharacteristic,
            mCommandCharacteristic;
    BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private DeviceListAdapter mLeDeviceListAdapter = null;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = false;
    AlertDialog mScanDeviceDialog;
    private String mDeviceName;
    private String mDeviceAddress;

    public enum ConnectionStateEnum {
        isNull, isScanning, isToScan, isConnecting, isConnected
    };

    public ConnectionStateEnum mConnectionState = ConnectionStateEnum.isNull;
    private static final int REQUEST_ENABLE_BT = 1;

    private Handler mHandler = new Handler();

    public boolean mConnected = false;

    private final static String TAG = BlunoLibrary.class.getSimpleName();

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (mConnectionState == ConnectionStateEnum.isConnecting)
                mConnectionState = ConnectionStateEnum.isToScan;
            adapter.onConectionStateChange(mConnectionState);
        }
    };

    public static final String SerialPortUUID = "0000dfb1-0000-1000-8000-00805f9b34fb";
    public static final String CommandUUID = "0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID = "00002a24-0000-1000-8000-00805f9b34fb";

    /**
     * 
     */
    public void onCreateProcess() {
        if (!initiate()) {
            Toast.makeText(context, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }

        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        // Initializes list view adapter.
        mLeDeviceListAdapter = new DeviceListAdapter(context);
        // Initializes and show the scan Device Dialog
        mScanDeviceDialog = new AlertDialog.Builder(context).setTitle("BLE Device Scan...")
                .setAdapter(mLeDeviceListAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
                        if (device == null)
                            return;
                        scanLeDevice(false);
                        LOGGER.debug("onListItemClick " + device.getName().toString());

                        LOGGER.debug("Device Name:" + device.getName() + "   " + "Device Name:" + device.getAddress());

                        mDeviceName = device.getName().toString();
                        mDeviceAddress = device.getAddress().toString();

                        if (mDeviceName.equals("No Device Available") && mDeviceAddress.equals("No Address Available")) {
                            mConnectionState = ConnectionStateEnum.isToScan;
                            adapter.onConectionStateChange(mConnectionState);
                        } else {
                            if (mBluetoothLeService.connect(mDeviceAddress)) {
                                Log.d(TAG, "Connect request success");
                                mConnectionState = ConnectionStateEnum.isConnecting;
                                adapter.onConectionStateChange(mConnectionState);
                                mHandler.postDelayed(mRunnable, 10000);
                            } else {
                                Log.d(TAG, "Connect request fail");
                                mConnectionState = ConnectionStateEnum.isToScan;
                                adapter.onConectionStateChange(mConnectionState);
                            }
                        }
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        LOGGER.debug("mBluetoothAdapter.stopLeScan");

                        mConnectionState = ConnectionStateEnum.isToScan;
                        adapter.onConectionStateChange(mConnectionState);
                        mScanDeviceDialog.dismiss();

                        scanLeDevice(false);
                    }
                }).create();
    }

    /**
     * 
     */
    public void onResumeProcess() {
        LOGGER.debug("BlUNOActivity onResume");
        // Ensures Bluetooth is enabled on the device. If Bluetooth is not
        // currently enabled,
        // fire an intent to display a dialog asking the user to grant
        // permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    /**
     * 
     */
    public void onPauseProcess() {
        LOGGER.debug("BLUNOActivity onPause");
        scanLeDevice(false);
        context.unregisterReceiver(mGattUpdateReceiver);
        mLeDeviceListAdapter.clear();
        mConnectionState = ConnectionStateEnum.isToScan;
        adapter.onConectionStateChange(mConnectionState);
        mScanDeviceDialog.dismiss();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
            // mBluetoothLeService.close();
        }
        mSCharacteristic = null;
    }

    /**
     * 
     */
    public void onStopProcess() {
        LOGGER.debug("MiUnoActivity onStop");
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
            // mBluetoothLeService.close();
        }
        mSCharacteristic = null;
    }

    /**
     * 
     */
    public void onDestroyProcess() {
        context.unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    /**
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResultProcess(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            ((Activity) context).finish();
            return;
        }
    }

    boolean initiate() {
        // Use this check to determine whether BLE is supported on the device.
        // Then you can
        // selectively disable BLE-related features.
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) context
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device. This can be a
    // result of read
    // or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            LOGGER.debug("mGattUpdateReceiver->onReceive->action=" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mHandler.removeCallbacks(mRunnable);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                mConnectionState = ConnectionStateEnum.isToScan;
                adapter.onConectionStateChange(mConnectionState);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the
                // user interface.
                for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
                    LOGGER.debug("ACTION_GATT_SERVICES_DISCOVERED  " + gattService.getUuid().toString());
                }
                getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (mSCharacteristic == mModelNumberCharacteristic) {
                    if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).startsWith("DF BLuno")) {
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, false);
                        mSCharacteristic = mCommandCharacteristic;
                        mSCharacteristic.setValue(mBaudrateBuffer);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic = mSerialPortCharacteristic;
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
                        mConnectionState = ConnectionStateEnum.isConnected;
                        adapter.onConectionStateChange(mConnectionState);

                    } else {
                        Toast.makeText(context, "Please select DFRobot devices", Toast.LENGTH_SHORT).show();
                        mConnectionState = ConnectionStateEnum.isToScan;
                        adapter.onConectionStateChange(mConnectionState);
                    }
                } else if (mSCharacteristic == mSerialPortCharacteristic) {
                    adapter.onSerialReceived(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }

                LOGGER.debug("displayData " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

                // mPlainProtocol.mReceivedframe.append(intent.getStringExtra(BluetoothLeService.EXTRA_DATA))
                // ;
                // LOGGER.debug("mPlainProtocol.mReceivedframe:");
                // LOGGER.debug(mPlainProtocol.mReceivedframe.toString());

            }
        }
    };

    public void buttonScanOnClickProcess() {
        if (mConnectionState == ConnectionStateEnum.isConnecting) {

        } else if (mConnectionState == ConnectionStateEnum.isToScan || mConnectionState == ConnectionStateEnum.isNull) {
            mConnectionState = ConnectionStateEnum.isScanning;
            adapter.onConectionStateChange(mConnectionState);
            scanLeDevice(true);
            mScanDeviceDialog.show();
        } else {
            mBluetoothLeService.disconnect();
            // mBluetoothLeService.close();
            mConnectionState = ConnectionStateEnum.isToScan;
            adapter.onConectionStateChange(mConnectionState);
        }
    }

    void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.

            LOGGER.debug("mBluetoothAdapter.startLeScan");

            if (mLeDeviceListAdapter != null) {
                mLeDeviceListAdapter.clear();
                mLeDeviceListAdapter.notifyDataSetChanged();
            }

            if (!mScanning) {
                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            if (mScanning) {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LOGGER.debug("mServiceConnection onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                ((Activity) context).finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LOGGER.debug("mServiceConnection onServiceDisconnected");
            mBluetoothLeService = null;
        }

    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LOGGER.debug("mLeScanCallback onLeScan run ");
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }

    };

    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        mModelNumberCharacteristic = null;
        mSerialPortCharacteristic = null;
        mCommandCharacteristic = null;
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            LOGGER.debug("displayGattServices + uuid=" + uuid);

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.equals(ModelNumberStringUUID)) {
                    mModelNumberCharacteristic = gattCharacteristic;
                    LOGGER.debug("mModelNumberCharacteristic  " + mModelNumberCharacteristic.getUuid().toString());
                } else if (uuid.equals(SerialPortUUID)) {
                    mSerialPortCharacteristic = gattCharacteristic;
                    LOGGER.debug("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                    // updateConnectionState(R.string.comm_establish);
                } else if (uuid.equals(CommandUUID)) {
                    mCommandCharacteristic = gattCharacteristic;
                    LOGGER.debug("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                    // updateConnectionState(R.string.comm_establish);
                }
            }
            mGattCharacteristics.add(charas);
        }

        if (mModelNumberCharacteristic == null || mSerialPortCharacteristic == null || mCommandCharacteristic == null) {
            Toast.makeText(context, "Please select DFRobot devices", Toast.LENGTH_SHORT).show();
            mConnectionState = ConnectionStateEnum.isToScan;
            adapter.onConectionStateChange(mConnectionState);
        } else {
            mSCharacteristic = mModelNumberCharacteristic;
            mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
            mBluetoothLeService.readCharacteristic(mSCharacteristic);
        }

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /**
     * @return the mDeviceName
     */
    public String getDeviceName() {
        return mDeviceName;
    }

}

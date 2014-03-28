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
package awbb.droid.robot;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import awbb.droid.R;
import awbb.droid.bluno.BlunoLibrary.ConnectionStateEnum;
import awbb.droid.bm.Location;
import awbb.droid.dao.DatabaseDataSource;
import awbb.droid.dao.LocationDao;

/**
 * Command activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RobotControlActivity extends Activity implements RobotListener {

    private TextView nameText;
    private TextView statusText;
    private TextView commandText;
    private TextView dataReceivedText;
    private Button connectButton;
    private Button disconnectButton;
    private Button startButton;
    private Button stopButton;
    private Spinner locationSpinner;
    private Button downloadButton;
    private Button downloadAllButton;

    private RobotManager robotManager;

    /**
     * Constructor.
     */
    public RobotControlActivity() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        robotManager = new RobotManager();

        // data source
        DatabaseDataSource.create(this);
        DatabaseDataSource.open();

        // bluno
        robotManager.onCreate(this);

        // load view
        setContentView(R.layout.activity_robot_control);

        nameText = (TextView) findViewById(R.id.robotNameText);
        statusText = (TextView) findViewById(R.id.robotStatusText);
        commandText = (TextView) findViewById(R.id.robotCommandText);
        dataReceivedText = (TextView) findViewById(R.id.robotDataReceivedText);
        connectButton = (Button) findViewById(R.id.robotConnectButton);
        disconnectButton = (Button) findViewById(R.id.robotDisconnectButton);
        startButton = (Button) findViewById(R.id.robotStartButton);
        stopButton = (Button) findViewById(R.id.robotStopButton);
        locationSpinner = (Spinner) findViewById(R.id.robotLocationSpinner);
        downloadButton = (Button) findViewById(R.id.robotDownloadButton);

        // location spinner
        List<Location> locations = LocationDao.getAll();
        ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(this, android.R.layout.simple_spinner_item,
                locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        // init state
        updateViewState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        // data source
        DatabaseDataSource.open();

        // bluno
        robotManager.addListener(this);
        robotManager.onResume();

        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        robotManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        // data source
        DatabaseDataSource.close();

        // bluno
        robotManager.onPause();
        robotManager.removeListener(this);

        super.onPause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop() {
        super.onStop();
        robotManager.onStop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        robotManager.onDestroy();
    }

    /**
     * 
     * @param v
     */
    public void onClickConnect(View v) {
        robotManager.sendCommand(RobotCommand.Connect);
    }

    /**
     * 
     * @param v
     */
    public void onClickDisconnect(View v) {
        robotManager.sendCommand(RobotCommand.Disconnect);
    }

    /**
     * 
     * @param v
     */
    public void onClickInit(View v) {
        robotManager.sendCommand(RobotCommand.Init);
    }

    /**
     * 
     * @param v
     */
    public void onClickCount(View v) {
        robotManager.sendCommand(RobotCommand.Count);
    }

    /**
     * 
     * @param v
     */
    public void onClickReinit(View v) {
        robotManager.sendCommand(RobotCommand.Reinit);
    }

    /**
     * 
     * @param v
     */
    public void onClickStart(View v) {
        robotManager.sendCommand(RobotCommand.Start);
    }

    /**
     * 
     * @param v
     */
    public void onClickAcquire(View v) {
        robotManager.sendCommand(RobotCommand.Acquire);
    }

    /**
     * 
     * @param v
     */
    public void onClickStop(View v) {
        robotManager.sendCommand(RobotCommand.Stop);
    }

    /**
     * 
     * @param v
     */
    public void onClickDownload(View v) {
        Location location = (Location) locationSpinner.getSelectedItem();

        if (location == null) {
            Toast.makeText(this, R.string.error_no_location_selected, Toast.LENGTH_SHORT).show();

            return;
        }

        robotManager.setLocation(location);
        robotManager.sendCommand(RobotCommand.Download);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateRobotName(String name) {
        nameText.setText(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateRobotStatus(ConnectionStateEnum connectionState) {
        switch (connectionState) {
        case isConnected:
            statusText.setText("Connected");
            break;

        case isConnecting:
            statusText.setText("Connecting");
            break;

        case isToScan:
            statusText.setText("Scan");
            break;

        case isScanning:
            statusText.setText("Scanning");
            break;

        case isNull:
        default:
            statusText.setText("");
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateRobotCommand(RobotCommand command) {
        commandText.setText(command.name());
        updateViewState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateRobotDataReceived(String str) {
        dataReceivedText.setText(str);
    }

    /**
     * Update view state.
     */
    private void updateViewState() {
        RobotState state = robotManager.getState();

        // FIXME
        // connectButton.setEnabled(state == null || state ==
        // RobotState.NotConnected);
        // disconnectButton.setEnabled(state != null && state !=
        // RobotState.NotConnected);
        // startButton.setEnabled(state == RobotState.Connected || state ==
        // RobotState.Initialized);
        // stopButton.setEnabled(state == RobotState.Alive);
        // downloadButton.setEnabled(state == RobotState.Connected || state ==
        // RobotState.Initialized);
        // downloadAllButton.setEnabled(state == RobotState.Connected || state
        // == RobotState.Initialized);
    }

}

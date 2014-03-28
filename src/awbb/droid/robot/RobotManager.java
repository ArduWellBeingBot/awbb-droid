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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import awbb.droid.R;
import awbb.droid.bluno.BlunoAdapter;
import awbb.droid.bluno.BlunoLibrary;
import awbb.droid.bluno.BlunoLibrary.ConnectionStateEnum;
import awbb.droid.bm.History;
import awbb.droid.bm.Location;
import awbb.droid.bm.SensorData;
import awbb.droid.dao.HistoryDao;
import awbb.droid.dao.SensorDataDao;
import awbb.droid.main.Settings;

/**
 * Robot management helper.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RobotManager implements BlunoAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RobotManager.class);

    private List<RobotListener> listeners;

    private Context context;

    /** The robot state. */
    private RobotState state;

    /** The last command. */
    private RobotCommand command;

    /** The bluno library. */
    private BlunoLibrary bluno;

    /** The received data buffer. */
    private String buffer;

    private Location location;
    private DownloadProgress progress;

    private boolean isOkReceived;
    private CountResponse countResponse;

    // begin / end date of download
    private History history;

    /**
     * Constructor.
     */
    public RobotManager() {
        listeners = new ArrayList<RobotListener>();
        command = RobotCommand.None;
        buffer = "";
    }

    /**
     * Initialize bluno communication.
     */
    public void onCreate(Context context) {
        this.context = context;

        bluno = new BlunoLibrary(context, this);
        bluno.onCreateProcess();
        bluno.serialBegin(Settings.getTransferRate(context));
    }

    /**
     * 
     */
    public void onResume() {
        bluno.onResumeProcess();
    }

    /**
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bluno.onActivityResultProcess(requestCode, resultCode, data);
    }

    /**
     * 
     */
    public void onPause() {
        bluno.onPauseProcess();
    }

    /**
     * 
     */
    public void onStop() {
        bluno.onStopProcess();
    }

    /**
     * 
     */
    public void onDestroy() {
        bluno.onDestroyProcess();
    }

    /**
     * Send the given command.
     * 
     * @param command the command
     */
    public void sendCommand(RobotCommand command) {
        setCommand(command);

        // the line to send
        String line = null;

        switch (command) {
        case Connect:
            bluno.buttonScanOnClickProcess();
            break;

        case Disconnect:
            bluno.onStopProcess();
            break;

        case Init: {
            long date = new Date().getTime() / 1000;
            line = command.getIdentifier() + String.valueOf(date);
        }
            break;

        case Start:
        case Acquire:
        case Stop:
        case Count:
        case Reinit:
            line = command.getIdentifier();
            break;

        case Download: {
            if (countResponse == null) {
                Toast.makeText(context, R.string.robot_download_invalid_count_message, Toast.LENGTH_LONG).show();
                setCommand(RobotCommand.None);
            } else if (countResponse.position >= countResponse.size) {
                Toast.makeText(context, R.string.robot_download_no_data_message, Toast.LENGTH_LONG).show();
                setCommand(RobotCommand.None);
            } else {
                DownloadTask downloadTask = new DownloadTask(context, this);
                downloadTask.execute();
            }
        }
            break;

        default:
            break;
        }

        if (line != null) {
            sendCommand(line);
        }
    }

    /**
     * Download acquired data.
     * 
     * @param progress
     */
    public void download(DownloadProgress progress) {
        this.progress = progress;

        setCommand(RobotCommand.Download);
        sendCommand(RobotCommand.Download.getIdentifier());

        while (command == RobotCommand.Download) {
            try {
                Thread.sleep(Settings.getTimeout(context));
            } catch (InterruptedException e) {
                LOGGER.warn("Timeout error: ", e);
            }
        }
    }

    /**
     * Send the given command.
     * 
     * @param command the command
     */
    private void sendCommand(String command) {
        LOGGER.debug("sendCommand=" + command);

        bluno.serialSend("$" + command + "\n");
    }

    /**
     * 
     */
    private void setCommand(RobotCommand command) {
        this.command = command;
        this.isOkReceived = false;

        if (command != RobotCommand.Count && command != RobotCommand.Reinit && command != RobotCommand.Download) {
            // do not send view events from an AsyncTask

            Iterator<RobotListener> it = listeners.iterator();
            while (it.hasNext()) {
                RobotListener listener = it.next();
                listener.onUpdateRobotCommand(command);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConectionStateChange(ConnectionStateEnum connectionState) {
        Iterator<RobotListener> it = listeners.iterator();
        while (it.hasNext()) {
            RobotListener listener = it.next();

            listener.onUpdateRobotName(bluno.getDeviceName());
            listener.onUpdateRobotStatus(connectionState);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * NOT called from the UI thread.
     */
    @Override
    public void onSerialReceived(String str) {
        LOGGER.debug("onSerialReceived command=" + command + " str=" + str);

        // add received string to the buffer
        buffer += str;

        // process lines in the buffer
        int index = buffer.indexOf("\r\n");
        while (index != -1) {
            // get first line in the buffer
            String line = buffer.substring(0, index);
            buffer = buffer.substring(index + 2);

            LOGGER.trace("onSerialReceived command=" + command + " line=" + line);

            // send line to listeners
            Iterator<RobotListener> it = listeners.iterator();
            while (it.hasNext()) {
                RobotListener listener = it.next();

                listener.onUpdateRobotDataReceived(line);
            }

            // process the line
            processLine(line);

            index = buffer.indexOf("\r\n");
        }
    }

    /**
     * 
     * @param line
     */
    private void processLine(String line) {

        // check line start
        line = checkReceivedLine(line);
        if (line == null) {
            LOGGER.warn("onSerialReceived command=" + command + " invalid line");
            return;
        }

        switch (command) {
        case Init:
        case Start:
        case Acquire:
        case Stop:
        case Reinit:
            // response= OK\r\n
            if (!isOkReceived && line.equals("OK")) {
                isOkReceived = true;

                setCommand(RobotCommand.None);
            }
            break;

        case Count:
            // response= OK\r\n
            if (!isOkReceived && line.equals("OK")) {
                isOkReceived = true;
            }

            // response= FILESIZE:<size>;<position>\r\n
            if (isOkReceived && line.startsWith("FILESIZE")) {
                countResponse = new CountResponse();
                countResponse.size = Long.parseLong(line.substring(line.indexOf(":") + 1, line.indexOf(";")));
                countResponse.position = Long.parseLong(line.substring(line.indexOf(";") + 1));

                LOGGER.debug("onSerialReceived command=" + command + " countResponse:" + countResponse);

                setCommand(RobotCommand.None);
            }
            break;

        case Download:
            // response= OK\r\n
            if (!isOkReceived && line.equals("OK")) {
                isOkReceived = true;

                // create a new history
                history = new History();
                history.setLocationId(location.getId());
                HistoryDao.add(history);
            }

            // response= <line>\r\n
            // send= O
            // response= EOF\r\n
            if (isOkReceived) {
                if (line.contains("EOF")) {
                    // end of download

                    setCommand(RobotCommand.None);
                } else {
                    // sensor data

                    processData(line);

                    if (progress != null) {
                        progress.updateProgress(line.length() + 2);
                    }

                    bluno.serialSend("O");
                }
            }
            break;

        case Disconnect:
        case None:
        default:
            break;
        }
    }

    /**
     * @param line
     * @return
     */
    private String checkReceivedLine(String line) {
        String newLine = null;

        if (line.contains("$")) {
            int index = line.indexOf("$");
            if (command.getIdentifier() != null && command.getIdentifier().charAt(0) == line.charAt(index + 1)) {
                newLine = line.substring(index + 2);
            }
        }

        return newLine;
    }

    /**
     * Parse the given sensor data line.
     * 
     * @param line a sensor data line
     */
    private void processData(String line) {
        String[] split = line.split(";");

        LOGGER.trace("processData split=" + split.length + " line=" + line);

        // format:
        // YYYY-MM-DD
        // HH:MM:SS.mmm;FIX;SAT;LAT;LONG;ALT;LIGHT;TEMP;HUMIDITY;CO2;SOUND;RATE

        if (split.length == 12) {
            SensorData data = new SensorData();

            try {
                // parse date
                String[] dateTimeStr = split[0].split("\\.")[0].split(" ");
                String millisStr = split[0].split("\\.")[1];
                String[] dateStr = dateTimeStr[0].split("-");
                String[] timeStr = dateTimeStr[1].split(":");

                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]), Integer.parseInt(dateStr[2]),
                        Integer.parseInt(timeStr[0]), Integer.parseInt(timeStr[1]), Integer.parseInt(timeStr[2]));
                cal.set(Calendar.MILLISECOND, Integer.parseInt(millisStr));
                Date date = cal.getTime();

                if (cal.get(Calendar.YEAR) == 200) {
                    LOGGER.warn("Invalid date");
                    return;
                }

                // set history begin/last date
                if (history.getBegin() == null) {
                    history.setBegin(date);
                }
                history.setEnd(date);
                HistoryDao.update(history);

                // parse data line
                data.setLocationId(location.getId());
                data.setDate(date);
                data.setGpsFix(split[1].equals("1"));
                data.setGpsNbSat(Integer.parseInt(split[2]));
                data.setLatitude(Double.parseDouble(split[3]));
                data.setLongitude(Double.parseDouble(split[4]));
                data.setAltitude(Double.parseDouble(split[5]));
                data.setLight(Double.parseDouble(split[6]));
                data.setTemperature(Double.parseDouble(split[7]));
                data.setHumidity(Double.parseDouble(split[8]));
                data.setCo2(Double.parseDouble(split[9]));
                data.setSound(Double.parseDouble(split[10]));
                data.setRate(Integer.parseInt(split[11]));
            } catch (NumberFormatException e) {
                LOGGER.warn("", e);
            }

            // insert data into database
            SensorDataDao.add(data);
        }
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Get the robot state.
     * 
     * @return the state
     */
    public RobotState getState() {
        return state;
    }

    /**
     * @return the command
     */
    public RobotCommand getCommand() {
        return command;
    }

    /**
     * @return the countResponse
     */
    public CountResponse getCountResponse() {
        return countResponse;
    }

    /**
     * @param listener
     * @return
     */
    public boolean addListener(RobotListener listener) {
        return listeners.add(listener);
    }

    /**
     * @param listener
     * @return
     */
    public boolean removeListener(Object listener) {
        return listeners.remove(listener);
    }

    /**
     * Count command response.
     */
    public static class CountResponse {
        public long size;
        public long position;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "size=" + size + " position=" + position;
        }

    }

    /**
     *
     */
    public static interface DownloadProgress {

        void updateProgress(int sizeRead);

    }

}

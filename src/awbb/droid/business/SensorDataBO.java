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
package awbb.droid.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import awbb.droid.bm.DeviceType;
import awbb.droid.bm.History;
import awbb.droid.bm.Location;
import awbb.droid.bm.SensorData;
import awbb.droid.dao.HistoryDao;
import awbb.droid.dao.LocationDao;
import awbb.droid.dao.SensorDataDao;
import awbb.droid.main.Settings;

/**
 * Sensor data business object.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class SensorDataBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataBO.class);

    /** The earth radius (km). */
    private static final double EARTH_RADIUS = 6371;

    private int timeDistance;
    private int locationDistance;

    private DeviceType deviceType;
    private String deviceName;

    private History history;
    private SensorData lastData;

    private List<Location> locations;

    /**
     * Constructor.
     * 
     * @param context
     * @param deviceName
     */
    public SensorDataBO(Context context, DeviceType deviceType, String deviceName) {
        this.deviceType = deviceType;
        this.deviceName = deviceName;

        timeDistance = Settings.getDataTimeDistance(context) * 1000;
        locationDistance = Settings.getDataLocationDistance(context);
    }

    /**
     * Upload the given data file.
     * 
     * @param file the file to upload
     */
    public void upload(File file) {
        LOGGER.debug("upload file=" + file.getName());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                processData(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Parse the given sensor data line.
     * 
     * @param line a sensor data line
     */
    public void processData(String line) {
        String[] split = line.split(";");

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
                cal.clear();
                cal.set(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]) - 1, Integer.parseInt(dateStr[2]),
                        Integer.parseInt(timeStr[0]), Integer.parseInt(timeStr[1]), Integer.parseInt(timeStr[2]));
                cal.set(Calendar.MILLISECOND, Integer.parseInt(millisStr));
                Date date = cal.getTime();

                // check date
                int year = cal.get(Calendar.YEAR);
                if (year < 2000 || year > 2100) {
                    LOGGER.warn("Invalid date");
                    return;
                }

                // parse latitude/longitude
                double lat = toDecimalDegree(split[3]);
                double lon = toDecimalDegree(split[4]);

                // check latitude/longitude
                if (lat == 0 || lon == 0) {
                    LOGGER.warn("Invalid coordinates");
                    return;
                }

                // parse data line
                data.setDate(date);
                data.setGpsFix(split[1].equals("1"));
                data.setGpsNbSat(Integer.parseInt(split[2]));
                data.setLatitude(lat);
                data.setLongitude(lon);
                data.setAltitude(Double.parseDouble(split[5]));
                data.setLight(Double.parseDouble(split[6]));
                data.setTemperature(Double.parseDouble(split[7]));
                data.setHumidity(Double.parseDouble(split[8]));
                data.setCo2(Double.parseDouble(split[9]));
                data.setSound(Double.parseDouble(split[10]));
                data.setRate(Integer.parseInt(split[11]));

                setHistory(data);
            } catch (NumberFormatException e) {
                LOGGER.warn("Error: " + e.getMessage(), e);
            }

            // insert data into database
            SensorDataDao.add(data);

            lastData = data;
        } else {
            LOGGER.warn("Invalid number of fields.");
        }
    }

    /**
     * Convert the given GPS value to decimal degree.
     * 
     * @param str the GPS value
     * @return the value in decimal degree
     */
    private double toDecimalDegree(String str) {
        if (str == null) {
            return 0;
        }

        String[] split = str.split("\\.");
        if (split.length != 2) {
            return 0;
        }

        String deg;
        String min;
        if (split[0].length() > 2) {
            deg = split[0].substring(0, split[0].length() - 2);
            min = split[0].substring(split[0].length() - 2) + "." + split[1];
        } else {
            deg = "0";
            min = split[0] + "." + split[1];
        }

        double ddd = Double.parseDouble(deg) + (Double.parseDouble(min) / 60);

        return ddd;
    }

    /**
     * Set the history for the given sensor data.
     * 
     * @param data the sensor data
     */
    private void setHistory(SensorData data) {
        boolean create = false;

        if (lastData == null) {
            create = true;
        } else {
            if (history == null) {
                create = true;
            } else {
                long lastTime = lastData.getDate().getTime();
                long dataTime = data.getDate().getTime();

                create = Math.abs(lastTime - dataTime) > timeDistance;
            }
        }

        if (create) {
            // create a new history
            history = new History();
            history.setLocationId(getLocation(data).getId());
            history.setDate(new Date());
            history.setDeviceType(deviceType);
            history.setDeviceName(deviceName);

            HistoryDao.add(history);
        }

        data.setHistoryId(history.getId());
    }

    /**
     * Get the location for the given data.
     * 
     * @param data the sensor data
     */
    private Location getLocation(SensorData data) {
        if (locations == null) {
            locations = LocationDao.getAll();
        }

        Location location = null;
        for (Location loc : locations) {
            double distance = getDistance(loc.getLatitude(), loc.getLongitude(), data.getLatitude(),
                    data.getLongitude());

            if (distance <= locationDistance) {
                location = loc;
                break;
            }
        }

        if (location == null) {
            location = new Location();
            location.setName("unknown");
            location.setLatitude(data.getLatitude());
            location.setLongitude(data.getLongitude());

            LocationDao.add(location);
            locations.add(location);
        }

        return location;
    }

    /**
     * Get the distance between two points.
     * 
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return the distance (m)
     */
    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1)
                * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS * c * 1000;

        return d;
    }

}

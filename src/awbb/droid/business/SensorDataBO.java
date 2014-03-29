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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import awbb.droid.bm.History;
import awbb.droid.bm.Location;
import awbb.droid.bm.SensorData;
import awbb.droid.dao.HistoryDao;
import awbb.droid.dao.SensorDataDao;

/**
 * Sensor data business object.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class SensorDataBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorDataBO.class);

    /**
     * Constructor.
     */
    private SensorDataBO() {
    }

    /**
     * Upload the given data file.
     * 
     * @param location a location
     * @param file the file to upload
     */
    public static void upload(Location location, File file) {
        LOGGER.debug("upload file=" + file.getName());

        // create a new history
        History history = new History();
        history.setDate(new Date());
        history.setLocationId(location.getId());
        HistoryDao.add(history);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                processData(history, line);
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
     * @param history a history
     * @param line a sensor data line
     */
    public static void processData(History history, String line) {
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
                cal.clear();
                cal.set(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]) - 1, Integer.parseInt(dateStr[2]),
                        Integer.parseInt(timeStr[0]), Integer.parseInt(timeStr[1]), Integer.parseInt(timeStr[2]));
                cal.set(Calendar.MILLISECOND, Integer.parseInt(millisStr));
                Date date = cal.getTime();

                if (cal.get(Calendar.YEAR) < 2000 && cal.get(Calendar.YEAR) > 2100) {
                    LOGGER.warn("Invalid date");
                    return;
                }

                // parse data line
                data.setHistoryId(history.getId());
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
        } else {
            LOGGER.warn("Invalid number of fields.");
        }
    }

}

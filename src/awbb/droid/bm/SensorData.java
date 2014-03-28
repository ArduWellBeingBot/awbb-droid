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
package awbb.droid.bm;

import java.util.Date;

/**
 * Sensors data.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class SensorData {

    private long id;
    private long locationId;
    private Date date;
    private boolean gpsFix;
    private int gpsNbSat;
    private double latitude;
    private double longitude;
    private double altitude;
    private double light;
    private double sound;
    private double temperature;
    private double humidity;
    private double co2;
    private int rate;

    /**
     * Constructor.
     */
    public SensorData() {
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the gpsFix
     */
    public boolean isGpsFix() {
        return gpsFix;
    }

    /**
     * @param gpsFix the gpsFix to set
     */
    public void setGpsFix(boolean gpsFix) {
        this.gpsFix = gpsFix;
    }

    /**
     * @return the gpsNbSat
     */
    public int getGpsNbSat() {
        return gpsNbSat;
    }

    /**
     * @param gpsNbSat the gpsNbSat to set
     */
    public void setGpsNbSat(int gpsNbSat) {
        this.gpsNbSat = gpsNbSat;
    }

    /**
     * @return the gpsLat
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param gpsLat the gpsLat to set
     */
    public void setLatitude(double gpsLat) {
        this.latitude = gpsLat;
    }

    /**
     * @return the gpsLong
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param gpsLong the gpsLong to set
     */
    public void setLongitude(double gpsLong) {
        this.longitude = gpsLong;
    }

    /**
     * @return the gpsAlt
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * @param gpsAlt the gpsAlt to set
     */
    public void setAltitude(double gpsAlt) {
        this.altitude = gpsAlt;
    }

    /**
     * @return the light
     */
    public double getLight() {
        return light;
    }

    /**
     * @param light the light to set
     */
    public void setLight(double light) {
        this.light = light;
    }

    /**
     * @return the sound
     */
    public double getSound() {
        return sound;
    }

    /**
     * @param sound the sound to set
     */
    public void setSound(double sound) {
        this.sound = sound;
    }

    /**
     * @return the temp
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemperature(double temp) {
        this.temperature = temp;
    }

    /**
     * @return the humidity
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * @param humidity the humidity to set
     */
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    /**
     * @return the co2
     */
    public double getCo2() {
        return co2;
    }

    /**
     * @param co2 the co2 to set
     */
    public void setCo2(double co2) {
        this.co2 = co2;
    }

    /**
     * @return the rate
     */
    public int getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

}

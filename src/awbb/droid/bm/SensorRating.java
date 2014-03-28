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

/**
 * Sensor rating.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class SensorRating {

    private long id;
    private long ratingId;
    private Sensor sensor;
    private double maxMax;
    private double max;
    private double min;
    private double minMin;
    private int weight;

    /**
     * Constructor.
     */
    public SensorRating() {
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
     * @return the ratingId
     */
    public long getRatingId() {
        return ratingId;
    }

    /**
     * @param ratingId the ratingId to set
     */
    public void setRatingId(long ratingId) {
        this.ratingId = ratingId;
    }

    /**
     * @return the sensor
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * @param sensor the sensor to set
     */
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * @return the maxMax
     */
    public double getMaxMax() {
        return maxMax;
    }

    /**
     * @param maxMax the maxMax to set
     */
    public void setMaxMax(double maxMax) {
        this.maxMax = maxMax;
    }

    /**
     * @return the max
     */
    public double getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * @return the min
     */
    public double getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * @return the minMin
     */
    public double getMinMin() {
        return minMin;
    }

    /**
     * @param minMin the minMin to set
     */
    public void setMinMin(double minMin) {
        this.minMin = minMin;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

}

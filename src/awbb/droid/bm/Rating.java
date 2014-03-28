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
 * Rating parameters.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class Rating {

    private long id;
    private String name;
    private SensorRating temp;
    private SensorRating hygro;
    private SensorRating co2;
    private SensorRating light;
    private SensorRating sound;

    /**
     * Constructor.
     */
    public Rating() {
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the temp
     */
    public SensorRating getTemp() {
        return temp;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemp(SensorRating temp) {
        this.temp = temp;
    }

    /**
     * @return the hygro
     */
    public SensorRating getHygro() {
        return hygro;
    }

    /**
     * @param hygro the hygro to set
     */
    public void setHygro(SensorRating hygro) {
        this.hygro = hygro;
    }

    /**
     * @return the co2
     */
    public SensorRating getCo2() {
        return co2;
    }

    /**
     * @param co2 the co2 to set
     */
    public void setCo2(SensorRating co2) {
        this.co2 = co2;
    }

    /**
     * @return the light
     */
    public SensorRating getLight() {
        return light;
    }

    /**
     * @param light the light to set
     */
    public void setLight(SensorRating light) {
        this.light = light;
    }

    /**
     * @return the sound
     */
    public SensorRating getSound() {
        return sound;
    }

    /**
     * @param sound the sound to set
     */
    public void setSound(SensorRating sound) {
        this.sound = sound;
    }

}

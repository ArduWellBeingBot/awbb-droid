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

import java.util.List;

import awbb.droid.bm.History;
import awbb.droid.bm.Location;
import awbb.droid.bm.Rating;
import awbb.droid.bm.SensorData;
import awbb.droid.dao.HistoryDao;
import awbb.droid.dao.LocationDao;
import awbb.droid.dao.SensorDataDao;

/**
 * Rating business object.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingBO {

    /**
     * Constructor.
     */
    private RatingBO() {
    }

    /**
     * Update rating of the given location and its hitories.
     * 
     * @param location a location
     * @param rating the rating
     */
    public static void update(Location location, Rating rating) {
        double temperatureL = 0;
        double humidityL = 0;
        double co2L = 0;
        double lightL = 0;
        double soundL = 0;
        int nbL = 0;

        List<History> histories = HistoryDao.get(location);
        for (History history : histories) {
            double temperatureH = 0;
            double humidityH = 0;
            double co2H = 0;
            double lightH = 0;
            double soundH = 0;

            List<SensorData> dataList = SensorDataDao.get(location, history.getBegin(), history.getEnd());
            for (SensorData data : dataList) {
                temperatureH += data.getTemperature();
                humidityH += data.getHumidity();
                co2H += data.getCo2();
                lightH += data.getLight();
                soundH += data.getSound();
            }

            int nbH = dataList.size();
            temperatureH = temperatureH / nbH;
            humidityH = humidityH / nbH;
            co2H = co2H / nbH;
            lightH = lightH / nbH;
            soundH = soundH / nbH;

            history.setRate(getRating(rating, temperatureH, humidityH, co2H, lightH, soundH));
            HistoryDao.update(history);

            temperatureL += temperatureH;
            humidityL += humidityH;
            co2L += co2H;
            lightL += lightH;
            soundL += soundH;
            nbL += nbH;
        }

        temperatureL = temperatureL / nbL;
        humidityL = humidityL / nbL;
        co2L = co2L / nbL;
        lightL = lightL / nbL;
        soundL = soundL / nbL;

        location.setRate(getRating(rating, temperatureL, humidityL, co2L, lightL, soundL));
        LocationDao.update(location);
    }

    /**
     * Process rating score.
     * 
     * @param rating
     * @param temperature
     * @param humidity
     * @param co2
     * @param light
     * @param sound
     * @return
     */
    private static int getRating(Rating rating, double temperature, double humidity, double co2, double light,
            double sound) {
        // TODO Auto-generated method stub
        return 0;
    }

}

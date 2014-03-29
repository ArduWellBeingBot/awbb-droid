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

import awbb.droid.bm.Rating;

/**
 * Rating business object.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingBO {

    public static final int MAX = 20;

    /**
     * Constructor.
     */
    private RatingBO() {
    }

    //
    // /**
    // * Update rating of the given location and its hitories.
    // *
    // * @param location a location
    // * @param rating the rating
    // */
    // public static void update(Location location) {
    // int rateLocation = 0;
    // int nbLocation = 0;
    //
    // List<History> histories = HistoryDao.get(location);
    // for (History history : histories) {
    // int rateHistory = 0;
    // int nbHistory = 0;
    //
    // List<SensorData> dataList = SensorDataDao.get(location,
    // history.getBegin(), history.getEnd());
    // for (SensorData data : dataList) {
    // rateHistory += data.getRate();
    // }
    // nbHistory = dataList.size();
    //
    // // update history
    // history.setRate(nbHistory == 0 ? 0 : rateHistory / nbHistory);
    // HistoryDao.update(history);
    //
    // rateLocation += rateHistory;
    // nbLocation += nbHistory;
    // }
    //
    // // update location
    // location.setRate(nbLocation == 0 ? 0 : rateLocation / nbLocation);
    // LocationDao.update(location);
    // }
    //
    // /**
    // * Get the rating of the given location and its hitories.
    // *
    // * @param location a location
    // * @param rating the rating
    // */
    // public static float getRate(Location location, Rating rating) {
    // double temperatureL = 0;
    // double humidityL = 0;
    // double co2L = 0;
    // double lightL = 0;
    // double soundL = 0;
    // int nbL = 0;
    //
    // List<History> histories = HistoryDao.get(location);
    // for (History history : histories) {
    // double temperatureH = 0;
    // double humidityH = 0;
    // double co2H = 0;
    // double lightH = 0;
    // double soundH = 0;
    //
    // List<SensorData> dataList = SensorDataDao.get(location,
    // history.getBegin(), history.getEnd());
    // for (SensorData data : dataList) {
    // temperatureH += data.getTemperature();
    // humidityH += data.getHumidity();
    // co2H += data.getCo2();
    // lightH += data.getLight();
    // soundH += data.getSound();
    // }
    //
    // // calculate history averages
    // int nbH = dataList.size();
    // temperatureH = temperatureH / nbH;
    // humidityH = humidityH / nbH;
    // co2H = co2H / nbH;
    // lightH = lightH / nbH;
    // soundH = soundH / nbH;
    //
    // // update history
    // history.setRate(getRating(rating, temperatureH, humidityH, co2H, lightH,
    // soundH));
    // HistoryDao.update(history);
    //
    // temperatureL += temperatureH;
    // humidityL += humidityH;
    // co2L += co2H;
    // lightL += lightH;
    // soundL += soundH;
    // nbL += nbH;
    // }
    //
    // // calculate location averages
    // temperatureL = temperatureL / nbL;
    // humidityL = humidityL / nbL;
    // co2L = co2L / nbL;
    // lightL = lightL / nbL;
    // soundL = soundL / nbL;
    //
    // // update location
    // location.setRate(getRating(rating, temperatureL, humidityL, co2L, lightL,
    // soundL));
    // LocationDao.update(location);
    // }

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
    public static int getRating(Rating rating, double temperature, double humidity, double co2, double light,
            double sound) {
        // TODO Auto-generated method stub
        return 0;
    }

}

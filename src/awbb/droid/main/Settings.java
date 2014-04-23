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
package awbb.droid.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Settings.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class Settings {

    public static enum Theme {
        Dark, Light;
    }

    public static final String ROBOT_TRANSFER_RATE = "pref_key_robot_transfer_rate";
    public static final String ROBOT_TIMEOUT = "pref_key_robot_timeout";
    public static final String DATA_TIME_DISTANCE = "pref_key_data_time_distance";
    public static final String DATA_LOCATION_DISTANCE = "pref_key_data_location_distance";
    public static final String SERVER_URL = "pref_key_server_url";
    public static final String THEME = "pref_key_theme";

    private static SharedPreferences preferences;

    /**
     * 
     * @param context
     */
    public static void setup(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Constructor.
     */
    private Settings() {
    }

    /**
     * 
     * @return
     */
    public static int getRobotTransferRate() {
        String str = preferences.getString(ROBOT_TRANSFER_RATE, "19200");
        return Integer.parseInt(str);
    }

    /**
     * 
     * @return
     */
    public static int getRobotTimeout() {
        String str = preferences.getString(ROBOT_TIMEOUT, "1000");
        return Integer.parseInt(str);
    }

    /**
     * 
     * @return
     */
    public static int getDataTimeDistance() {
        String str = preferences.getString(DATA_TIME_DISTANCE, "300");
        return Integer.parseInt(str);
    }

    /**
     * 
     * @return
     */
    public static int getDataLocationDistance() {
        String str = preferences.getString(DATA_LOCATION_DISTANCE, "100");
        return Integer.parseInt(str);
    }

    /**
     * 
     * @return
     */
    public static String getServerUrl() {
        return preferences.getString(SERVER_URL, "");
    }

    /**
     * 
     * @return
     */
    public static String getTheme() {
        return preferences.getString(THEME, Theme.Dark.name());
    }

}

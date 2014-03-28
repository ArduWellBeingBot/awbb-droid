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

    public static final String ROBOT_TRANSFER_RATE = "pref_key_robot_transfer_rate";
    public static final String ROBOT_TIMEOUT = "pref_key_robot_timeout";
    public static final String SERVER_URL = "pref_key_server_url";

    /**
     * Constructor.
     */
    private Settings() {
    }

    /**
     * 
     * @param context
     * @return
     */
    public static int getTransferRate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String str = prefs.getString(ROBOT_TRANSFER_RATE, "19200");
        return Integer.parseInt(str);
    }

    /**
     * 
     * @param context
     * @return
     */
    public static int getTimeout(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String str = prefs.getString(ROBOT_TIMEOUT, "1000");
        return Integer.parseInt(str);
    }

    /**
     * 
     * @param context
     * @return
     */
    public static String getServerUrl(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SERVER_URL, "");
    }

}

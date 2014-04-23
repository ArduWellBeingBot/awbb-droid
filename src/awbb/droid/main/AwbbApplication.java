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

import android.app.Activity;
import android.app.Application;
import awbb.droid.R;
import awbb.droid.main.Settings.Theme;

/**
 * AWBB application.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class AwbbApplication extends Application {

    /** The current theme. */
    private static Theme theme = Theme.Dark;

    /**
     * Constructor.
     */
    public AwbbApplication() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // init settings
        Settings.setup(this);
    }

    /**
     * Reload the application theme.
     */
    public void reloadTheme() {
        theme = Theme.valueOf(Settings.getTheme());
    }

    /**
     * Apply the theme to the given activity.
     * 
     * @param activity the activity
     */
    public void applyTheme(Activity activity) {
        switch (theme) {
        case Dark:
            activity.setTheme(R.style.AppThemeDark);
            return;

        case Light:
            activity.setTheme(R.style.AppThemeLight);
            return;
        }
    }

}

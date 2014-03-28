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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import awbb.droid.R;
import awbb.droid.data.LocationListActivity;
import awbb.droid.data.RatingListActivity;
import awbb.droid.data.viz.MapActivity;
import awbb.droid.robot.RobotControlActivity;

/**
 * Main menu data.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class MainMenu {

    private int icon;
    private int name;
    private Class<? extends Activity> activityClass;

    private static List<MainMenu> menu;

    /**
     * @return the menu
     */
    public static List<MainMenu> getMenu() {
        if (menu == null) {
            menu = new ArrayList<MainMenu>();
            menu.add(new MainMenu(R.drawable.ic_menu_robot, R.string.robot_control_title, RobotControlActivity.class));
            // menu.add(new MainMenu(R.drawable.ic_menu_robot,
            // R.string.robot_list_title, RobotListActivity.class));
            menu.add(new MainMenu(R.drawable.ic_menu_location, R.string.location_list_title, LocationListActivity.class));
            menu.add(new MainMenu(R.drawable.ic_menu_rating, R.string.rating_list_title, RatingListActivity.class));
            menu.add(new MainMenu(R.drawable.ic_menu_map, R.string.map_title, MapActivity.class));
        }
        return menu;
    }

    /**
     * Constructor.
     * 
     * @param icon
     * @param name
     * @param activityClass
     */
    private MainMenu(int icon, int name, Class<? extends Activity> activityClass) {
        super();
        this.icon = icon;
        this.name = name;
        this.activityClass = activityClass;
    }

    /**
     * @return the icon
     */
    public int getIcon() {
        return icon;
    }

    /**
     * @return the name
     */
    public int getName() {
        return name;
    }

    /**
     * @return the activityClass
     */
    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

}

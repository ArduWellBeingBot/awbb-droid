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
package awbb.droid.data.viz;

import android.graphics.Color;

/**
 * Graph serie.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public enum GraphData {

    Temperature(Color.BLUE), Humidity(Color.GREEN), Co2(Color.RED), Light(Color.YELLOW), Sound(Color.MAGENTA), All(
            Color.TRANSPARENT);

    private int color;

    /**
     * Constructor.
     * 
     * @param color
     */
    private GraphData(int color) {
        this.color = color;
    }

    /**
     * @return the color
     */
    public int getColor() {
        return color;
    }

}

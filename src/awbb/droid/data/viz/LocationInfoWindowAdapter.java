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

import java.util.Map;

import android.app.Activity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import awbb.droid.R;
import awbb.droid.bm.Location;
import awbb.droid.business.RatingBO;
import awbb.droid.dao.SensorDataDao;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

/**
 * Location info window adapter for google maps.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class LocationInfoWindowAdapter implements InfoWindowAdapter {

    private Activity activity;
    private Map<Marker, Location> locationsMap;

    /**
     * Constructor.
     * 
     * @param context
     * @param map
     */
    public LocationInfoWindowAdapter(Activity activity, Map<Marker, Location> map) {
        this.activity = activity;
        this.locationsMap = map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getInfoContents(Marker marker) {
        View view = activity.getLayoutInflater().inflate(R.layout.mapsinfowindow_location, null);

        Location location = locationsMap.get(marker);

        TextView name = (TextView) view.findViewById(R.id.mapsLocationName);
        name.setText(location.getName());

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.mapsLocationRatingBar);
        ratingBar.setRating(SensorDataDao.getRate(location) * ratingBar.getNumStars() / RatingBO.MAX);

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

}

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
import android.content.Intent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import awbb.droid.R;
import awbb.droid.bm.Location;
import awbb.droid.dao.SensorDataDao;
import awbb.droid.data.HistoryListActivity;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

/**
 * Location info window adapter for google maps.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class LocationInfoWindowAdapter implements InfoWindowAdapter {

    private Activity activity;
    private Map<Marker, Location> map;

    /**
     * Constructor.
     * 
     * @param context
     * @param map
     */
    public LocationInfoWindowAdapter(Activity activity, Map<Marker, Location> map) {
        this.activity = activity;
        this.map = map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getInfoContents(Marker marker) {
        View view = activity.getLayoutInflater().inflate(R.layout.mapsinfowindow_location, null);

        TextView name = (TextView) view.findViewById(R.id.mapsLocationName);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.mapsLocationRatingBar);

        final Location location = map.get(marker);
        name.setText(location.getName());
        ratingBar.setIsIndicator(true);
        ratingBar.setNumStars(5);
        ratingBar.setMax(20);
        ratingBar.setRating(SensorDataDao.getRate(location));

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HistoryListActivity.class);
                intent.putExtra(HistoryListActivity.EXTRA_LOCATION_ID, location.getId());
                activity.startActivity(intent);
            }

        });

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

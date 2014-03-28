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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import awbb.droid.R;
import awbb.droid.bm.Location;
import awbb.droid.dao.DatabaseDataSource;
import awbb.droid.dao.LocationDao;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Map activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class MapActivity extends FragmentActivity {

    private GoogleMap map;

    /**
     * Constructor.
     */
    public MapActivity() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load view
        setContentView(R.layout.activity_map);

        // get data from database
        DatabaseDataSource.create(this);
        List<Location> locations = LocationDao.getAll();
        DatabaseDataSource.close();

        // map settings
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // add data
        LatLng firstLocation = null;
        Map<Marker, Location> markers = new HashMap<Marker, Location>();
        for (Location location : locations) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            if (firstLocation == null) {
                firstLocation = latLng;
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(location.getName());

            Marker marker = map.addMarker(markerOptions);

            markers.put(marker, location);
        }

        // info window adapter
        LocationInfoWindowAdapter adapter = new LocationInfoWindowAdapter(this, markers);
        map.setInfoWindowAdapter(adapter);

        // move the camera instantly to user with a zoom of 15
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 15));

        // zoom in, animating the camera
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

}

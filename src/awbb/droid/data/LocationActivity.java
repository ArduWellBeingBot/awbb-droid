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
package awbb.droid.data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import awbb.droid.R;
import awbb.droid.bm.Location;
import awbb.droid.dao.DatabaseDataSource;
import awbb.droid.dao.LocationDao;

/**
 * Location activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class LocationActivity extends Activity {

    private static final String TAG = LocationActivity.class.getSimpleName();

    public static final String EXTRA_LOCATION_ID = "org.awbb.LOCATION_ID";

    private EditText nameText;
    private EditText addressText;

    private Location location;

    /**
     * Constructor.
     */
    public LocationActivity() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseDataSource.create(this);
        DatabaseDataSource.open();

        // load view
        setContentView(R.layout.activity_location);

        nameText = (EditText) findViewById(R.id.locationNameText);
        addressText = (EditText) findViewById(R.id.locationAddressText);

        // get data from intent
        Intent intent = getIntent();
        long id = intent.getLongExtra(EXTRA_LOCATION_ID, -1);

        Log.d(TAG, "onCreate: id=" + id);

        // init
        if (id == -1) {
            location = new Location();
        } else {
            location = LocationDao.get(id);

            nameText.setText(location.getName());
            addressText.setText(location.getAddress());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        DatabaseDataSource.open();
        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        DatabaseDataSource.close();
        super.onPause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_actions_location, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_save:
            location.setName(nameText.getText().toString());
            location.setAddress(addressText.getText().toString());
            LocationDao.save(location);
            finish();
            return true;

        case R.id.action_cancel:
            finish();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

}

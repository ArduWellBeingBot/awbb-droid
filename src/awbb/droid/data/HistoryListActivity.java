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

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import awbb.droid.bm.History;
import awbb.droid.bm.Location;
import awbb.droid.dao.DatabaseDataSource;
import awbb.droid.dao.HistoryDao;
import awbb.droid.dao.LocationDao;
import awbb.droid.data.viz.GraphActivity;

/**
 * History list activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class HistoryListActivity extends ListActivity {

    private static final String TAG = HistoryListActivity.class.getSimpleName();

    public static final String EXTRA_LOCATION_ID = "org.awbb.LOCATION_ID";

    private HistoryListAdapter adapter;

    /**
     * Constructor.
     */
    public HistoryListActivity() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get data from intent
        Intent intent = getIntent();
        long id = intent.getLongExtra(EXTRA_LOCATION_ID, -1);

        Log.d(TAG, "onCreate location id=" + id);

        // data source
        DatabaseDataSource.create(this);
        DatabaseDataSource.open();

        Location location = LocationDao.get(id);
        List<History> list = HistoryDao.get(location);

        // list adapter
        adapter = new HistoryListAdapter(this, list);
        setListAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        // data source
        DatabaseDataSource.open();

        super.onResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
        // data source
        DatabaseDataSource.close();

        super.onPause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        History history = (History) getListAdapter().getItem(position);

        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra(GraphActivity.EXTRA_HISTORY_ID, history.getId());
        startActivity(intent);
    }

}

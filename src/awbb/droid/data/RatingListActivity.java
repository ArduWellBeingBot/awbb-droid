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

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import awbb.droid.R;
import awbb.droid.bm.Rating;
import awbb.droid.dao.DatabaseDataSource;
import awbb.droid.dao.RatingDao;

/**
 * Rating list activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingListActivity extends ListActivity {

    private static final String TAG = RatingListActivity.class.getSimpleName();

    private RatingListAdapter adapter;
    private List<Rating> ratings;
    private Rating selected;

    /**
     * Constructor.
     */
    public RatingListActivity() {
        ratings = new ArrayList<Rating>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // data source
        DatabaseDataSource.create(this);
        DatabaseDataSource.open();

        // list attributes
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // list adapter
        adapter = new RatingListAdapter(this, ratings);
        setListAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        // data source
        DatabaseDataSource.open();

        // update data list
        ratings.clear();
        ratings.addAll(RatingDao.getAll());
        adapter.notifyDataSetChanged();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_actions_rating_list, menu);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_add: {
            // start activity
            Intent intent = new Intent(this, RatingActivity.class);
            startActivity(intent);
        }
            return true;

        case R.id.action_edit: {
            if (selected != null) {
                // start activity
                Intent intent = new Intent(this, RatingActivity.class);
                intent.putExtra(RatingActivity.EXTRA_RATING_ID, selected.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.error_no_selection, Toast.LENGTH_SHORT).show();
            }
        }
            return true;

        case R.id.action_delete: {
            if (selected != null) {
                // delete selected item
                RatingDao.delete(selected);

                // update list
                ratings.clear();
                ratings.addAll(RatingDao.getAll());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, R.string.error_no_selection, Toast.LENGTH_SHORT).show();
            }
        }
            return true;

        case R.id.action_refresh:
            // TODO action refresh
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 
     * @param position
     * @param rating
     */
    void onItemSelected(int position, Rating rating) {
        Log.d(TAG, "onItemSelected: rating id=" + (rating == null ? "null" : rating.getId()));

        selected = rating;

        for (int i = 0; i < adapter.getCount(); i++) {
            getListView().setItemChecked(i, position == i && rating != null);
        }
    }

}

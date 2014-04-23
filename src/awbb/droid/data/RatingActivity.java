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

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import awbb.droid.R;
import awbb.droid.bm.Rating;
import awbb.droid.dao.DatabaseDataSource;
import awbb.droid.dao.RatingDao;
import awbb.droid.main.AwbbApplication;

/**
 * Rating activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingActivity extends FragmentActivity {

    private static final String TAG = RatingActivity.class.getSimpleName();

    public static final String EXTRA_RATING_ID = "org.awbb.RATING_ID";

    private ViewPager viewPager;
    private RatingPagerAdapter adapter;

    private Rating rating;

    /**
     * Constructor.
     */
    public RatingActivity() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set theme
        ((AwbbApplication) getApplication()).applyTheme(this);

        // create activity
        super.onCreate(savedInstanceState);

        // data source
        DatabaseDataSource.create(this);
        DatabaseDataSource.open();

        // load view
        setContentView(R.layout.activity_rating);

        viewPager = (ViewPager) findViewById(R.id.ratingPager);

        // get data from intent
        Intent intent = getIntent();
        long id = intent.getLongExtra(EXTRA_RATING_ID, -1);

        // init
        if (id == -1) {
            rating = new Rating();
        } else {
            rating = RatingDao.get(id);
        }

        // adapter
        adapter = new RatingPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // action bar
        final ActionBar actionBar = getActionBar();
        // FIXME
        // http://developer.android.com/training/implementing-navigation/lateral.html#horizontal-paging
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        //
        // @Override
        // public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // @Override
        // public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // viewPager.setCurrentItem(tab.getPosition());
        // }
        //
        // @Override
        // public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // };

        // add tabs
        // for (int i = 0; i < Sensor.values().length + 1; i++) {
        // actionBar.addTab(actionBar.newTab().setText("tab" +
        // i).setTabListener(tabListener));
        // }

        // page swipe
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected position=" + position);
                actionBar.setSelectedNavigationItem(position);
            };

        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_actions_rating, menu);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_save:
            // FIXME
            // rating.setName(nameText.getText().toString());
            // RatingDao.save(rating);
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

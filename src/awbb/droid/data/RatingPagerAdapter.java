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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import awbb.droid.R;
import awbb.droid.bm.Sensor;

/**
 * Rating pager adapter.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = RatingPagerAdapter.class.getSimpleName();

    private Context context;

    /**
     * Constructor.
     * 
     * @param context
     * @param fm
     */
    public RatingPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        Log.d(TAG, "getItem position=" + position);

        switch (position) {
        case 0:
            fragment = new RatingNameFragment();
            break;

        default: {
            Bundle args = new Bundle();
            args.putString(RatingSensorFragment.ARG_SENSOR, Sensor.values()[position - 1].name());

            fragment = new RatingSensorFragment();
            fragment.setArguments(args);
        }
            break;
        }

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return Sensor.values().length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
        case 0:
            return context.getString(R.string.rating_title);

        case 1:
            return context.getString(R.string.rating_temp);

        case 2:
            return context.getString(R.string.rating_humidity);

        case 3:
            return context.getString(R.string.rating_co2);

        case 4:
            return context.getString(R.string.rating_light);

        case 5:
            return context.getString(R.string.rating_sound);

        default:
            return "";
        }
    }

}

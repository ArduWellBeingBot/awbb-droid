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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import awbb.droid.R;

/**
 * Rating sensor fragment.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingSensorFragment extends Fragment {

    private static final String TAG = RatingSensorFragment.class.getSimpleName();

    public static final String ARG_SENSOR = "org.awbb.SENSOR";

    /**
     * Constructor.
     */
    public RatingSensorFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_sensor, container, false);
        Bundle args = getArguments();
        // TODO

        Log.d(TAG, "onCreateView args=" + args.getString(ARG_SENSOR));

        return view;
    }

}

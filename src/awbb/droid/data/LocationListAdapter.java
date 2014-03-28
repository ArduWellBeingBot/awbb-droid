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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import awbb.droid.R;
import awbb.droid.bm.Location;

/**
 * Location list adapter.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class LocationListAdapter extends ArrayAdapter<Location> {

    private LayoutInflater inflator;
    private List<Location> locations;

    /**
     * The view holder.
     */
    static class ViewHolder {
        TextView name;
        RatingBar rate;
    }

    /**
     * Constructor.
     * 
     * @param context
     * @param locations
     */
    public LocationListAdapter(Context context, List<Location> locations) {
        super(context, R.layout.listitem_location, locations);

        this.inflator = ((Activity) context).getLayoutInflater();
        this.locations = locations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // reuse views
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.listitem_location, null);

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.itemLocationCheckBox);
            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean isChecked = ((CheckBox) v).isChecked();
                    if (isChecked) {
                        ((LocationListActivity) getContext()).onItemSelected(position, locations.get(position));
                    } else {
                        ((LocationListActivity) getContext()).onItemSelected(position, null);
                    }
                }
            });

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.itemLocationName);
            viewHolder.rate = (RatingBar) convertView.findViewById(R.id.itemLocationRate);
            viewHolder.rate.setIsIndicator(true);
            viewHolder.rate.setNumStars(5);
            viewHolder.rate.setMax(20);

            convertView.setTag(viewHolder);
        }

        // get data to display
        final Location location = locations.get(position);

        // set holder fields
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(location.getName());
        viewHolder.rate.setRating(location.getRate());

        return convertView;
    }

}

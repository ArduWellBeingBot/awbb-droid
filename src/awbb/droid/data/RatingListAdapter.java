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
import android.widget.TextView;
import awbb.droid.R;
import awbb.droid.bm.Rating;

/**
 * Rating list adapter.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingListAdapter extends ArrayAdapter<Rating> {

    private LayoutInflater inflator;
    private List<Rating> ratings;

    /**
     * The view holder.
     */
    static class ViewHolder {
        public TextView name;
    }

    /**
     * Constructor.
     * 
     * @param context
     * @param rating
     */
    public RatingListAdapter(Context context, List<Rating> ratings) {
        super(context, R.layout.listitem_rating, ratings);

        this.inflator = ((Activity) context).getLayoutInflater();
        this.ratings = ratings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // reuse views
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.listitem_rating, null);

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.itemRatingCheckBox);
            checkBox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean isChecked = ((CheckBox) v).isChecked();
                    if (isChecked) {
                        ((RatingListActivity) getContext()).onItemSelected(position, ratings.get(position));
                    } else {
                        ((RatingListActivity) getContext()).onItemSelected(position, null);
                    }
                }
            });

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.itemRatingName);
            convertView.setTag(viewHolder);
        }

        // get data to display
        Rating rating = ratings.get(position);

        // set holder fields
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(rating.getName());

        return convertView;
    }

}

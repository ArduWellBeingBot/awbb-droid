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

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import awbb.droid.R;
import awbb.droid.bm.History;
import awbb.droid.business.RatingBO;
import awbb.droid.dao.SensorDataDao;

/**
 * History list adapter.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class HistoryListAdapter extends ArrayAdapter<History> {

    private LayoutInflater inflator;
    private List<History> histories;

    /**
     * The view holder.
     */
    static class ViewHolder {
        TextView begin;
        RatingBar rate;
    }

    /**
     * Constructor.
     * 
     * @param context
     * @param list
     */
    public HistoryListAdapter(Context context, List<History> list) {
        super(context, R.layout.listitem_history, list);

        inflator = ((Activity) context).getLayoutInflater();
        this.histories = list;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // reuse views
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.listitem_history, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.begin = (TextView) convertView.findViewById(R.id.begin);
            viewHolder.rate = (RatingBar) convertView.findViewById(R.id.rate);
            convertView.setTag(viewHolder);
        }

        // get data to display
        History history = histories.get(position);

        // set holder fields
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.begin.setText(dateFormat.format(SensorDataDao.getBeginDate(history)));
        viewHolder.rate.setRating(SensorDataDao.getRate(history) * viewHolder.rate.getNumStars() / RatingBO.MAX);

        return convertView;
    }

}

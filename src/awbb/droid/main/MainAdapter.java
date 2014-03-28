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
package awbb.droid.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import awbb.droid.R;

/**
 * Main menu adapter.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class MainAdapter extends ArrayAdapter<MainMenu> {

    private LayoutInflater inflator;

    /**
     * The view holder.
     */
    static class ViewHolder {
        public ImageView icon;
        public TextView label;
    }

    /**
     * Constructor.
     * 
     * @param context
     */
    public MainAdapter(Context context) {
        super(context, R.layout.listitem_main, MainMenu.getMenu());

        inflator = ((Activity) context).getLayoutInflater();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // reuse views
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.listitem_main, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.label = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(viewHolder);
        }

        // get menu to display
        MainMenu menu = MainMenu.getMenu().get(position);

        // set holder fields
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.icon.setImageResource(menu.getIcon());
        viewHolder.label.setText(menu.getName());

        return convertView;
    }

}

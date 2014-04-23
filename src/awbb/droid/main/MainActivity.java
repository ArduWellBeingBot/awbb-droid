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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import awbb.droid.R;

/**
 * Main activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class MainActivity extends ListActivity {

    /**
     * Constructor.
     */
    public MainActivity() {
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

        setListAdapter(new MainAdapter(this));

        // set settings default values
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        MainMenu menu = (MainMenu) getListAdapter().getItem(position);

        Intent intent = new Intent(this, menu.getActivityClass());
        startActivity(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_actions_main, menu);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        case R.id.action_about:
            startActivity(new Intent(this, AboutActivity.class));
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

}

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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import awbb.droid.R;

/**
 * Settings fragment.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    /**
     * Constructor.
     */
    public SettingsFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            initPrefSummary(getPreferenceScreen().getPreference(i));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefSummary(findPreference(key));
    }

    /**
     * Init preference summary.
     * 
     * @param preference
     */
    private void initPrefSummary(Preference preference) {
        if (preference instanceof PreferenceCategory) {
            PreferenceCategory category = (PreferenceCategory) preference;
            for (int i = 0; i < category.getPreferenceCount(); i++) {
                initPrefSummary(category.getPreference(i));
            }
        } else {
            updatePrefSummary(preference);
        }
    }

    /**
     * Update preference summary.
     * 
     * @param preference
     */
    private void updatePrefSummary(Preference preference) {
        if (preference.getKey().equals(Settings.ROBOT_TRANSFER_RATE) && preference instanceof ListPreference) {
            preference.setSummary(((ListPreference) preference).getEntry() + " "
                    + getString(R.string.settings_transfer_rate_summary));

        } else if (preference.getKey().equals(Settings.ROBOT_TIMEOUT) && preference instanceof EditTextPreference) {
            preference.setSummary(((EditTextPreference) preference).getText() + " "
                    + getString(R.string.settings_timeout_summary));
        }
    }

}

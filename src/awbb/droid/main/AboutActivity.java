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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import awbb.droid.R;

/**
 * About activity.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class AboutActivity extends Activity {

    private static final Logger LOGGER = LoggerFactory.getLogger(AboutActivity.class);

    /**
     * Constructor.
     */
    public AboutActivity() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load view
        setContentView(R.layout.activity_about);

        // set version
        try {
            TextView version = (TextView) findViewById(R.id.aboutVersion);
            version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }

        // set URL clickable
        TextView blogUrl = (TextView) findViewById(R.id.aboutBlogUrl);
        blogUrl.setMovementMethod(LinkMovementMethod.getInstance());

        TextView codeUrl = (TextView) findViewById(R.id.aboutCodeUrl);
        codeUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }
}

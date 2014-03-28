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

        // set URL clickable
        TextView blogUrl = (TextView) findViewById(R.id.aboutBlogUrl);
        blogUrl.setMovementMethod(LinkMovementMethod.getInstance());

        TextView codeUrl = (TextView) findViewById(R.id.aboutCodeUrl);
        codeUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }

}

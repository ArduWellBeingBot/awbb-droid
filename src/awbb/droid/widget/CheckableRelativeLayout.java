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
package awbb.droid.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * Checkable relative layout.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {

    private CheckBox checkBox;

    /**
     * Constructor.
     * 
     * @param context
     */
    public CheckableRelativeLayout(Context context) {
        super(context);
    }

    /**
     * Constructor.
     * 
     * @param context
     * @param attrs
     */
    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor.
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // search CheckBox
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof CheckBox) {
                checkBox = (CheckBox) view;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChecked(boolean checked) {
        if (checkBox != null) {
            checkBox.setChecked(checked);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChecked() {
        return checkBox != null ? checkBox.isChecked() : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggle() {
        if (checkBox != null) {
            checkBox.toggle();
        }
    }

}

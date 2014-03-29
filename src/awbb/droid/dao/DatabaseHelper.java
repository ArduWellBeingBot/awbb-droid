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
package awbb.droid.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLite database helper.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DB_NAME = "awbb.sqlite";
    private static final int DB_VERSION = 2;

    /**
     * Constructor.
     * 
     * @param context the context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        Log.d(TAG, "database=" + context.getDatabasePath(DB_NAME));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        // create tables
        database.execSQL(LocationDao.CREATE_TABLE_LOCATION);
        database.execSQL(HistoryDao.CREATE_TABLE_HISTORY);
        database.execSQL(RatingDao.CREATE_TABLE_RATING);
        database.execSQL(RatingDao.CREATE_TABLE_SENSOR_RATING);
        database.execSQL(SensorDataDao.CREATE_TABLE_LOCATION);

        // init with default values
        RatingDao.addDefaultValues(database);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // drop old tables
        database.execSQL(LocationDao.DROP_TABLE_LOCATION);
        database.execSQL(HistoryDao.DROP_TABLE_HISTORY);
        database.execSQL(RatingDao.DROP_TABLE_RATING);
        database.execSQL(RatingDao.DROP_TABLE_SENSOR_RATING);
        database.execSQL(SensorDataDao.DROP_TABLE_LOCATION);

        // create tables
        onCreate(database);
    }

}

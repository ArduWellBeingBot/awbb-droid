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

/**
 * SQLite data source.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class DatabaseDataSource {

    private static DatabaseHelper helper;
    private static SQLiteDatabase database;

    /**
     * Constructor.
     */
    private DatabaseDataSource() {
    }

    /**
     * Create a data source.
     * 
     * @param context the context
     */
    public static void create(Context context) {
        helper = new DatabaseHelper(context);
    }

    /**
     * Open the database.
     */
    public static void open() {
        database = helper.getWritableDatabase();
    }

    /**
     * Close the database.
     */
    public static void close() {
        helper.close();
    };

    /**
     * Get the writable database.
     * 
     * @return the database
     */
    static SQLiteDatabase getDatabase() {
        return database;
    }

}

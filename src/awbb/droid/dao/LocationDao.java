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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import awbb.droid.bm.Location;

/**
 * Location DAO.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class LocationDao {

    private static final String TAG = LocationDao.class.getSimpleName();

    static final String CREATE_TABLE_LOCATION = "create table location (" + "_id integer primary key autoincrement,"
            + "name text not null," + "address text," + "rate integer," + "latitude number," + "longitude number" + ")";

    static final String DROP_TABLE_LOCATION = "drop table if exists location";

    /**
     * Constructor.
     */
    private LocationDao() {
    }

    /**
     * Add.
     * 
     * @param location
     * @return
     */
    public static long add(Location location) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        ContentValues values = toContentValues(location);
        long id = database.insert("location", null, values);

        Log.d(TAG, "Add location id=" + id);

        return id;
    }

    /**
     * Get.
     * 
     * @param id
     * @return
     */
    public static Location get(long id) {
        Location location = null;
        final String sql = "select * from location where _id=?";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, new String[] { String.valueOf(id) });
        if (cursor != null) {
            cursor.moveToFirst();
            location = toObject(cursor);
        }
        cursor.close();

        return location;
    }

    /**
     * Get.
     * 
     * @param name
     * @return
     */
    public static Location get(String name) {
        Location location = null;
        final String sql = "select * from location where name=?";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, new String[] { name });
        if (cursor != null) {
            cursor.moveToFirst();
            location = toObject(cursor);
        }
        cursor.close();

        return location;
    }

    /**
     * Get all.
     * 
     * @return
     */
    public static List<Location> getAll() {
        List<Location> locations = new ArrayList<Location>();
        final String sql = "select * from location order by name asc";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Location location = toObject(cursor);
                locations.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return locations;
    }

    /**
     * Update.
     * 
     * @param location
     */
    public static void update(Location location) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        Log.d(TAG, "Update location id=" + location.getId());

        ContentValues values = toContentValues(location);
        database.update("location", values, "_id = ?", new String[] { String.valueOf(location.getId()) });
    }

    /**
     * Delete.
     * 
     * @param location
     */
    public static void delete(Location location) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        Log.d(TAG, "Delete location id=" + location.getId());

        database.delete("location", "_id = ?", new String[] { String.valueOf(location.getId()) });
    }

    /**
     * Add or update the given location.
     * 
     * @param location
     */
    public static void save(Location location) {
        if (location.getId() == 0) {
            add(location);
        } else {
            update(location);
        }
    }

    /**
     * 
     */
    private static ContentValues toContentValues(Location object) {
        ContentValues values = new ContentValues();

        values.put("name", object.getName());
        values.put("address", object.getAddress());
        values.put("rate", object.getRate());

        return values;
    }

    /**
     * 
     */
    private static Location toObject(Cursor cursor) {
        Location data = new Location();

        data.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));
        data.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        data.setRate(cursor.getInt(cursor.getColumnIndex("rate")));

        return data;
    }

}

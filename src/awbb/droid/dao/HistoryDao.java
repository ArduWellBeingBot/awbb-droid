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
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import awbb.droid.bm.History;
import awbb.droid.bm.Location;

/**
 * History DAO.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class HistoryDao {

    private static final String TAG = HistoryDao.class.getSimpleName();

    static final String CREATE_TABLE_HISTORY = "create table history (" + "_id integer primary key autoincrement,"
            + "location_id integer not null," + "date integer not null" + ")";

    static final String DROP_TABLE_HISTORY = "drop table if exists history";

    /**
     * Constructor.
     */
    private HistoryDao() {
    }

    /**
     * Add.
     * 
     * @param history
     * @return
     */
    public static long add(History history) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        ContentValues values = toContentValues(history);
        long id = database.insert("history", null, values);

        Log.d(TAG, "Add history id=" + id);

        history.setId(id);

        return id;
    }

    /**
     * Get.
     * 
     * @param id
     * @return
     */
    public static History get(long id) {
        History history = null;
        final String sql = "select * from history where _id=?";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, new String[] { String.valueOf(id) });
        if (cursor != null) {
            cursor.moveToFirst();
            history = toObject(cursor);
        }
        cursor.close();

        return history;
    }

    /**
     * Get.
     * 
     * @param location
     * @return
     */
    public static List<History> get(Location location) {
        List<History> list = new ArrayList<History>();
        final String sql = "select * from history where location_id=? order by date asc";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, new String[] { String.valueOf(location.getId()) });
        if (cursor.moveToFirst()) {
            do {
                History history = toObject(cursor);
                list.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    /**
     * Update.
     * 
     * @param history
     */
    public static void update(History history) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        Log.d(TAG, "Update history id=" + history.getId());

        ContentValues values = toContentValues(history);
        database.update("history", values, "_id = ?", new String[] { String.valueOf(history.getId()) });
    }

    /**
     * Delete.
     * 
     * @param history
     */
    public static void delete(History history) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        Log.d(TAG, "Delete history id=" + history.getId());

        database.delete("history", "_id = ?", new String[] { String.valueOf(history.getId()) });
    }

    /**
     * 
     */
    private static ContentValues toContentValues(History object) {
        ContentValues values = new ContentValues();

        values.put("location_id", object.getLocationId());
        values.put("date", object.getDate().getTime());

        return values;
    }

    /**
     * 
     */
    private static History toObject(Cursor cursor) {
        History data = new History();

        data.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        data.setLocationId(cursor.getLong(cursor.getColumnIndex("location_id")));
        data.setDate(new Date(cursor.getLong(cursor.getColumnIndex("date"))));

        return data;
    }

}

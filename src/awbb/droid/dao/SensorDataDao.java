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
import awbb.droid.bm.Location;
import awbb.droid.bm.SensorData;

/**
 * Sensor data DAO.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class SensorDataDao {

    private static final String TAG = SensorDataDao.class.getSimpleName();

    static final String CREATE_TABLE_LOCATION = "create table sensor_data (" + "_id integer primary key autoincrement,"
            + "location_id integer not null," + "date integer not null," + "gps_fix integer," + "gps_sat integer,"
            + "latitude number," + "longitude number," + "altitude number," + "light number," + "sound number,"
            + "temperature number," + "humidity number," + "co2 number," + "rate integer" + ")";

    static final String DROP_TABLE_LOCATION = "drop table if exists sensor_data";

    /**
     * Constructor.
     */
    private SensorDataDao() {
    }

    /**
     * Add.
     * 
     * @param data
     * @return
     */
    public static long add(SensorData data) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        ContentValues values = toContentValues(data);
        long id = database.insert("sensor_data", null, values);

        Log.d(TAG, "Add sensorData id=" + id);

        return id;
    }

    /**
     * Delete.
     * 
     * @param data
     */
    public static void delete(SensorData data) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        Log.d(TAG, "Delete sensorData id=" + data.getId());

        database.delete("sensor_data", "_id=?", new String[] { String.valueOf(data.getId()) });
    }

    /**
     * Get.
     * 
     * @param id
     * @return
     */
    public static SensorData get(long id) {
        SensorData data = null;
        final String sql = "select * from sensor_data where _id=?";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, new String[] { String.valueOf(id) });
        if (cursor != null) {
            cursor.moveToFirst();
            data = toObject(cursor);
        }
        cursor.close();

        return data;
    }

    /**
     * Get all.
     * 
     * @return
     */
    public static List<SensorData> getAll() {
        List<SensorData> list = new ArrayList<SensorData>();
        final String sql = "select * from sensor_data order by location,date asc";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                SensorData data = toObject(cursor);
                list.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    /**
     * 
     * @param location
     * @return
     */
    public static List<SensorData> get(Location location) {
        List<SensorData> list = new ArrayList<SensorData>();
        final String sql = "select * from sensor_data where location_id=? order by date asc";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, new String[] { String.valueOf(location.getId()) });
        if (cursor.moveToFirst()) {
            do {
                SensorData data = toObject(cursor);
                list.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    /**
     * 
     * @param location
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<SensorData> get(Location location, Date startDate, Date endDate) {
        List<SensorData> list = new ArrayList<SensorData>();
        final String sql = "select * from sensor_data where location_id=? and date>=? and date<=? order by date asc";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(
                sql,
                new String[] { String.valueOf(location.getId()), String.valueOf(startDate.getTime()),
                        String.valueOf(endDate.getTime()) });
        if (cursor.moveToFirst()) {
            do {
                SensorData data = toObject(cursor);
                list.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    /**
     * 
     */
    private static ContentValues toContentValues(SensorData object) {
        ContentValues values = new ContentValues();

        values.put("location_id", object.getLocationId());
        values.put("date", object.getDate().getTime());
        values.put("gps_fix", object.isGpsFix());
        values.put("gps_sat", object.getGpsNbSat());
        values.put("latitude", object.getLatitude());
        values.put("longitude", object.getLongitude());
        values.put("altitude", object.getAltitude());
        values.put("light", object.getLight());
        values.put("sound", object.getSound());
        values.put("temperature", object.getTemperature());
        values.put("humidity", object.getHumidity());
        values.put("co2", object.getCo2());
        values.put("rate", object.getRate());

        return values;
    }

    /**
     * 
     */
    private static SensorData toObject(Cursor cursor) {
        SensorData data = new SensorData();

        data.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        data.setLocationId(cursor.getLong(cursor.getColumnIndex("location_id")));
        data.setDate(new Date(cursor.getLong(cursor.getColumnIndex("date"))));
        data.setGpsFix(cursor.getInt(cursor.getColumnIndex("gps_fix")) == 1);
        data.setGpsNbSat(cursor.getInt(cursor.getColumnIndex("gps_sat")));
        data.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
        data.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
        data.setAltitude(cursor.getDouble(cursor.getColumnIndex("altitude")));
        data.setLight(cursor.getDouble(cursor.getColumnIndex("light")));
        data.setSound(cursor.getDouble(cursor.getColumnIndex("sound")));
        data.setTemperature(cursor.getDouble(cursor.getColumnIndex("temperature")));
        data.setHumidity(cursor.getDouble(cursor.getColumnIndex("humidity")));
        data.setCo2(cursor.getDouble(cursor.getColumnIndex("co2")));
        data.setRate(cursor.getInt(cursor.getColumnIndex("rate")));

        return data;
    }

}

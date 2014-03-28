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
import awbb.droid.bm.Rating;
import awbb.droid.bm.Sensor;
import awbb.droid.bm.SensorRating;

/**
 * Rating DAO.
 * 
 * @author Benoit Garrigues <bgarrigues@gmail.com>
 */
public class RatingDao {

    private static final String TAG = RatingDao.class.getSimpleName();

    static final String CREATE_TABLE_RATING = "create table rating (" + "_id integer primary key autoincrement,"
            + "name text not null" + ")";

    static final String CREATE_TABLE_SENSOR_RATING = "create table sensor_rating ("
            + "_id integer primary key autoincrement," + "rating_id integer not null," + "sensor text not null,"
            + "max_max real," + "max real," + "min real," + "min_min real," + "weight integer" + ")";

    static final String DROP_TABLE_RATING = "drop table if exists rating";

    static final String DROP_TABLE_SENSOR_RATING = "drop table if exists sensor_rating";

    /**
     * Constructor.
     */
    private RatingDao() {
    }

    /**
     * Add default values.
     * 
     * @param database the database
     */
    static void addDefaultValues(SQLiteDatabase database) {
        Rating rating = new Rating();
        rating.setName("Default");

        rating.setTemp(new SensorRating());
        rating.getTemp().setSensor(Sensor.Temperature);
        rating.getTemp().setMaxMax(32);
        rating.getTemp().setMax(26);
        rating.getTemp().setMin(19);
        rating.getTemp().setMinMin(14);
        rating.getTemp().setWeight(1);

        rating.setHygro(new SensorRating());
        rating.getHygro().setSensor(Sensor.Humidity);
        rating.getHygro().setMaxMax(90);
        rating.getHygro().setMax(70);
        rating.getHygro().setMin(40);
        rating.getHygro().setMinMin(20);
        rating.getHygro().setWeight(1);

        rating.setCo2(new SensorRating());
        rating.getCo2().setSensor(Sensor.Co2);
        rating.getCo2().setMaxMax(1000);
        rating.getCo2().setMax(400);
        rating.getCo2().setWeight(2);

        rating.setLight(new SensorRating());
        rating.getLight().setSensor(Sensor.Light);
        rating.getLight().setMax(1000);
        rating.getLight().setMin(600);
        rating.getLight().setMinMin(250);
        rating.getLight().setWeight(1);

        rating.setSound(new SensorRating());
        rating.getSound().setSensor(Sensor.Temperature);
        rating.getSound().setMaxMax(3);
        rating.getSound().setMax(2);
        rating.getSound().setWeight(2);

        add(database, rating);
    }

    /**
     * Add.
     * 
     * @param rating
     * @return
     */
    public static long add(Rating rating) {
        return add(DatabaseDataSource.getDatabase(), rating);
    }

    /**
     * Add.
     * 
     * @param database the database
     * @param rating
     * @return
     */
    static long add(SQLiteDatabase database, Rating rating) {

        ContentValues values = toContentValues(rating);
        long id = database.insert("rating", null, values);

        Log.d(TAG, "Add rating _id=" + id);

        rating.getTemp().setRatingId(id);
        values = toContentValues(rating.getTemp());
        database.insert("sensor_rating", null, values);

        rating.getHygro().setRatingId(id);
        values = toContentValues(rating.getHygro());
        database.insert("sensor_rating", null, values);

        rating.getCo2().setRatingId(id);
        values = toContentValues(rating.getCo2());
        database.insert("sensor_rating", null, values);

        rating.getLight().setRatingId(id);
        values = toContentValues(rating.getLight());
        database.insert("sensor_rating", null, values);

        rating.getSound().setRatingId(id);
        values = toContentValues(rating.getSound());
        database.insert("sensor_rating", null, values);

        return id;
    }

    /**
     * Get.
     * 
     * @param id
     * @return
     */
    public static Rating get(long id) {
        Rating rating = null;
        final String sql = "select * from rating where _id=?";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, new String[] { String.valueOf(id) });
        if (cursor != null) {
            cursor.moveToFirst();
            rating = toRating(cursor);

            getSensorRating(database, rating);
        }
        cursor.close();

        return rating;
    }

    /**
     * Get all.
     * 
     * @return
     */
    public static List<Rating> getAll() {
        List<Rating> ratings = new ArrayList<Rating>();
        final String sql = "select * from rating order by name asc";

        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Rating rating = toRating(cursor);
                ratings.add(rating);

                getSensorRating(database, rating);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return ratings;
    }

    /**
     * Get sensor rating for the given rating.
     * 
     * @param database
     * @param rating
     */
    private static void getSensorRating(SQLiteDatabase database, Rating rating) {
        final String sql = "select * from sensor_rating where rating_id=?";

        Cursor cursor = database.rawQuery(sql, new String[] { String.valueOf(rating.getId()) });
        if (cursor.moveToFirst()) {
            do {
                SensorRating sensorRating = toSensorRating(cursor);
                switch (sensorRating.getSensor()) {
                case Temperature:
                    rating.setTemp(sensorRating);
                    break;

                case Humidity:
                    rating.setHygro(sensorRating);
                    break;

                case Co2:
                    rating.setCo2(sensorRating);
                    break;

                case Light:
                    rating.setLight(sensorRating);
                    break;

                case Sound:
                    rating.setSound(sensorRating);
                    break;

                default:
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * Update.
     * 
     * @param rating
     */
    public static void update(Rating rating) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();
        // TODO update
        Log.d(TAG, "Update rating _id=" + rating.getId());

        ContentValues values = toContentValues(rating);
        database.update("rating", values, "_id = ?", new String[] { String.valueOf(rating.getId()) });
    }

    /**
     * Delete.
     * 
     * @param rating
     */
    public static void delete(Rating rating) {
        SQLiteDatabase database = DatabaseDataSource.getDatabase();

        Log.d(TAG, "Delete rating id=" + rating.getId());

        database.delete("rating", "_id = ?", new String[] { String.valueOf(rating.getId()) });
        database.delete("sensor_rating", "rating_id = ?", new String[] { String.valueOf(rating.getId()) });
    }

    /**
     * Add or update the given rating.
     * 
     * @param rating
     */
    public static void save(Rating rating) {
        if (rating.getId() == 0) {
            add(rating);
        } else {
            update(rating);
        }
    }

    /**
     * 
     */
    private static ContentValues toContentValues(Rating object) {
        ContentValues values = new ContentValues();

        values.put("name", object.getName());

        return values;
    }

    /**
     * 
     * @param object
     * @return
     */
    private static ContentValues toContentValues(SensorRating object) {
        ContentValues values = new ContentValues();

        values.put("rating_id", object.getRatingId());
        values.put("sensor", object.getSensor().name());
        values.put("max_max", object.getMaxMax());
        values.put("max", object.getMax());
        values.put("min", object.getMin());
        values.put("min_min", object.getMinMin());
        values.put("weight", object.getWeight());

        return values;
    }

    /**
     * 
     * @param cursor
     * @return
     */
    private static Rating toRating(Cursor cursor) {
        Rating data = new Rating();

        data.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        data.setName(cursor.getString(cursor.getColumnIndex("name")));

        return data;
    }

    /**
     * 
     * @param cursor
     * @return
     */
    private static SensorRating toSensorRating(Cursor cursor) {
        SensorRating data = new SensorRating();

        data.setId(cursor.getLong(cursor.getColumnIndex("_id")));
        data.setRatingId(cursor.getLong(cursor.getColumnIndex("rating_id")));
        data.setSensor(Sensor.valueOf(cursor.getString(cursor.getColumnIndex("sensor"))));
        data.setMaxMax(cursor.getDouble(cursor.getColumnIndex("max_max")));
        data.setMax(cursor.getDouble(cursor.getColumnIndex("max")));
        data.setMin(cursor.getDouble(cursor.getColumnIndex("min")));
        data.setMinMin(cursor.getDouble(cursor.getColumnIndex("min_min")));
        data.setWeight(cursor.getInt(cursor.getColumnIndex("weight")));

        return data;
    }

}

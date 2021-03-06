package example.foodbuzz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 3/28/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "restaurantsManager";

    // Contacts table name
    private static final String TABLE_RESTAURANTS= "restaurants";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "describtion";
    private static final String KEY_PRICE = "price";
    private static final String KEY_RATING = "rating";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT," +KEY_PRICE + " TEXT,"+KEY_RATING+" TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);

        // Create tables again
        onCreate(db);
    }
    public void addRow(Restaurant restaurant){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, restaurant.getName());
        values.put(KEY_DESC, restaurant.getDescription());
        values.put(KEY_PRICE, restaurant.getPrice());
        values.put(KEY_RATING, restaurant.getRating());
        db.insert(TABLE_RESTAURANTS, null, values);
        db.close();

    }

    public List<Restaurant> getAllContacts() {
        List<Restaurant> restaurantList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RESTAURANTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Restaurant res = new Restaurant();
                res.setId(Integer.parseInt(cursor.getString(0)));
                res.setName(cursor.getString(1));
                res.setDescription(cursor.getString(2));
                res.setPrice(cursor.getString(3));
                res.setRating(cursor.getString(4));

                restaurantList.add(res);
            } while (cursor.moveToNext());
        }

        return restaurantList;
    }
}
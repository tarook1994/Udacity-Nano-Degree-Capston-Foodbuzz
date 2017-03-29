package example.foodbuzz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 3/29/2017.
 */
public class DBHandlerFav extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "FavrestaurantsManager";

    // Contacts table name
    public static final String TABLE_FAV= "fav";

    // Contacts Table Columns names
     public static final String KEY_ID = "id";
     public static final String KEY_NAME = "name";
     public static final String KEY_DESC = "describtion";
     public static final String KEY_PRICE = "price";
     public static final String KEY_RATING = "rating";
     public static final String KEY_ICON = "icon";


    public DBHandlerFav(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAV + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT," +KEY_PRICE + " TEXT,"+KEY_RATING+" TEXT,"+ KEY_ICON+" TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);

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
        values.put(KEY_ICON, restaurant.getIcon());
        db.insert(TABLE_FAV, null, values);
        db.close();

    }

    public List<Restaurant> getAllFavList() {
        List<Restaurant> restaurantList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAV;

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
                res.setIcon(cursor.getString(5));

                restaurantList.add(res);
            } while (cursor.moveToNext());
        }

        return restaurantList;
    }

    public long delete( String name ){
        //COLOMN_NAME+" like ?"
        String where = KEY_NAME+ "='" + name+"'";
        String whereArgs[] = null;
        SQLiteDatabase db = getWritableDatabase();
        long test = db.delete(TABLE_FAV,where, whereArgs);
        return test;

    }
}
package example.foodbuzz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 4/4/2017.
 */

public class DBCart extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CartManager";

    // Contacts table name
    private static final String TABLE_CART= "cart";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBEROFITEMS = "number";
    private static final String KEY_PRICE = "price";
    private static final String KEY_URL = "url";


    public DBCart(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_NUMBEROFITEMS + " TEXT," +KEY_PRICE + " TEXT,"+KEY_URL+" TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Create tables again
        onCreate(db);

    }

    public void addRow(OrderItem orderItem){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, orderItem.getName());
        values.put(KEY_NUMBEROFITEMS, orderItem.getNumber());
        values.put(KEY_PRICE, orderItem.getPrice());
        values.put(KEY_URL, orderItem.getUrl());
        db.insert(TABLE_CART, null, values);
        db.close();

    }

    public ArrayList<OrderItem> getAllOrder() {
        ArrayList<OrderItem> orderList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderItem orderItem = new OrderItem();
                orderItem.setID(Integer.parseInt(cursor.getString(0)));
                orderItem.setName(cursor.getString(1));
                orderItem.setNumber(cursor.getString(2));
                orderItem.setPrice(cursor.getString(3));
                orderItem.setUrl(cursor.getString(4));

                orderList.add(orderItem);
            } while (cursor.moveToNext());
        }

        return orderList;
    }

    public long delete( String name ){
        //COLOMN_NAME+" like ?"
        String where = KEY_NAME+ "='" + name+"'";
        String whereArgs[] = null;
        SQLiteDatabase db = getWritableDatabase();
        long test = db.delete(TABLE_CART,where, whereArgs);
        return test;

    }

    public long deleteOrder(){
        //COLOMN_NAME+" like ?"
        String where =null;
        String whereArgs[] = null;
        SQLiteDatabase db = getWritableDatabase();
        long test = db.delete(TABLE_CART,where, whereArgs);
        return test;

    }

    public int updateCart(OrderItem orderItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUMBEROFITEMS, orderItem.getNumber());

        // updating row
        String where = KEY_NAME+ "='" + orderItem.getName()+"'";
        return db.update(TABLE_CART, values, where,
                null);
    }
}

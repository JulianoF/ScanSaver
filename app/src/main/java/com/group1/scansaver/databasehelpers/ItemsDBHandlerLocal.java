package com.group1.scansaver.databasehelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.group1.scansaver.dataobjects.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsDBHandlerLocal extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "itemsDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVOURITES = "favorites";
    private static final String KEY_FAV_ID = "id";
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_UPC = "upc";
    private static final String KEY_ITEM_PRICE = "price";
    private static final String KEY_ITEM_STORE = "store";
    private static final String KEY_ITEM_IMAGEURL = "image";

    private static final String TABLE_COORDINATES = "coordinates";
    private static final String KEY_COORD_ID = "id";
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";


    public ItemsDBHandlerLocal(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_FAVOURITES +
                "(" +
                KEY_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ITEM_NAME + " TEXT," +
                KEY_ITEM_UPC + " TEXT," +
                KEY_ITEM_PRICE + " REAL," +
                KEY_ITEM_STORE + " TEXT," +
                KEY_ITEM_IMAGEURL + " TEXT" +
                ")";

        db.execSQL(CREATE_NOTES_TABLE);

        String CREATE_COORDINATES_TABLE = "CREATE TABLE " + TABLE_COORDINATES +
                "(" +
                KEY_COORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_ITEM_ID + " INTEGER," +
                KEY_LATITUDE + " REAL," +
                KEY_LONGITUDE + " REAL," +
                "FOREIGN KEY(" + KEY_ITEM_ID + ") REFERENCES " + TABLE_FAVOURITES + "(" + KEY_FAV_ID + ")" +
                ")";
        db.execSQL(CREATE_COORDINATES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
            onCreate(db);
        }
    }

    ////////////QUERY FUNCTIONS/////////////////

    public long addFavorite(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String name = item.getNAME();
        String upc = item.getUPC();
        double price = item.getPRICE();

        values.put(KEY_ITEM_NAME, name);
        values.put(KEY_ITEM_UPC, upc);
        values.put(KEY_ITEM_PRICE, price);

        long id = db.insert(TABLE_FAVOURITES, null, values);
        db.close();
        return id;
    }

    public int updateFavorite(int id ,Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String name = item.getNAME();
        String upc = item.getUPC();
        double price = item.getPRICE();
        values.put(KEY_ITEM_NAME, name);
        values.put(KEY_ITEM_UPC, upc);
        values.put(KEY_ITEM_PRICE, price);

        int rowsAffected = db.update(
                TABLE_FAVOURITES,
                values,
                KEY_FAV_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
        return rowsAffected;
    }

    public void removeFavorite(String upc){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITES, KEY_ITEM_UPC + " = ?", new String[]{String.valueOf(upc)});
    }

    public List<Item> getAllFavorites() {
        List<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select all rows from the favorites table
        String query = "SELECT * FROM " + TABLE_FAVOURITES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Create a new Item object
                Item item = new Item();

                //Populate the Item object with data from the cursor
                //item.setID(cursor.getInt(cursor.getColumnIndex(KEY_FAV_ID)));
                int nameIndex = cursor.getColumnIndex(KEY_ITEM_NAME);
                if (nameIndex != -1) {
                    item.setNAME(cursor.getString(nameIndex));
                }

                int upcIndex = cursor.getColumnIndex(KEY_ITEM_UPC);
                if (upcIndex != -1) {
                    item.setUPC(cursor.getString(upcIndex));
                }

                int priceIndex = cursor.getColumnIndex(KEY_ITEM_PRICE);
                if (priceIndex != -1) {
                    item.setPRICE(cursor.getDouble(priceIndex));
                }
                //item.setStore(cursor.getString(cursor.getColumnIndex(KEY_ITEM_STORE)));
                //item.setImageURL(cursor.getString(cursor.getColumnIndex(KEY_ITEM_IMAGEURL)));

                // Add the Item to the list
                itemList.add(item);
            }
            cursor.close();
        }

        db.close();
        return itemList;
    }
}

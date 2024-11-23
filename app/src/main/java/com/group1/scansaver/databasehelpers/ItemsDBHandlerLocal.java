package com.group1.scansaver.databasehelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.group1.scansaver.dataobjects.Item;

public class ItemsDBHandlerLocal extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "itemsDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVOURITES = "favorites";
    private static final String KEY_FAV_ID = "id";
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_UPC = "upc";
    private static final String KEY_ITEM_PRICE = "price";

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
                KEY_ITEM_PRICE + " REAL" +
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

    public void removeFavorite(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITES, KEY_FAV_ID + " = ?", new String[]{String.valueOf(id)});
    }
}

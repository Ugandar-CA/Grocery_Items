package com.example.groceryitems.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.DateFormat;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.groceryitems.model.Item;
import com.example.groceryitems.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DataBaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE = "CREATE TABLE " +Constants.DB_TABLE_NAME+ "("
                +Constants.KEY_ID+ " INTEGER PRIMARY KEY ,"
                +Constants.KEY_ITEM_NAME+ " TEXT ,"
                +Constants.KEY_ITEM_QUANTITY+ " TEXT,"
                +Constants.KEY_ITEM_SIZE+ " TEXT,"
                +Constants.KEY_ITEM_COLOR+ " TEXT,"
                +Constants.KEY_ITEM_NOTE+ " TEXT,"
                +Constants.KEY_ITEM_DATE+ " LONG);";

        sqLiteDatabase.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Constants.DB_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //CRUD OPERATION

    public void addItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM_NAME,item.getItemName());
        values.put(Constants.KEY_ITEM_QUANTITY,item.getItemQuantity());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());
        values.put(Constants.KEY_ITEM_COLOR,item.getItemColor());
        values.put(Constants.KEY_ITEM_NOTE,item.getItemNote());
        values.put(Constants.KEY_ITEM_DATE,java.lang.System.currentTimeMillis());

        //insert row
        db.insert(Constants.DB_TABLE_NAME,null,values);
    }

    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.DB_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_ITEM_NAME,
                        Constants.KEY_ITEM_QUANTITY,
                        Constants.KEY_ITEM_SIZE,
                        Constants.KEY_ITEM_COLOR,
                        Constants.KEY_ITEM_NOTE,
                        Constants.KEY_ITEM_DATE},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Item item = new Item();
        if(cursor != null){
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_NAME)));
            item.setItemQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_QUANTITY)));
            item.setItemSize(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));
            item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_COLOR)));
            item.setItemNote(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_NOTE)));

            //convert time stamp to something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_ITEM_DATE)))
                    .getTime());  // jun 3 2020

            item.setItemAddDate(formattedDate);

            Log.d("ugandarCA", "getItem: "+ formattedDate);

        }
        return item;
    }

    public List<Item> getAllItem(){

        SQLiteDatabase database = this.getReadableDatabase();
        List<Item> itemList = new ArrayList<>();

        Cursor cursor = database.query(Constants.DB_TABLE_NAME,
                new String[]{Constants.KEY_ID,
                Constants.KEY_ITEM_NAME,
                Constants.KEY_ITEM_QUANTITY,
                Constants.KEY_ITEM_SIZE,
                Constants.KEY_ITEM_COLOR,
                Constants.KEY_ITEM_NOTE,
                Constants.KEY_ITEM_DATE},null,null,null,null,
                Constants.KEY_ITEM_DATE + " DESC");

        if (cursor.moveToFirst()) {

            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_NAME)));
                item.setItemQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_QUANTITY)));
                item.setItemSize(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));
                item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_COLOR)));
                item.setItemNote(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_NOTE)));

//                DateFormat dateFormat = DateFormat.getDateInstance();
//                String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_ITEM_DATE)))
//                .getTime());
//                item.setItemAddDate(formatDate);

                //add to array list
                itemList.add(item);

            }while (cursor.moveToNext());
        }
        return itemList;
    }



    //Update Item
    public int updateItem(Item item){ 
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_ITEM_NAME,item.getItemName());
        values.put(Constants.KEY_ITEM_QUANTITY,item.getItemQuantity());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());
        values.put(Constants.KEY_ITEM_COLOR,item.getItemColor());
        values.put(Constants.KEY_ITEM_NOTE,item.getItemNote());
        values.put(Constants.KEY_ITEM_DATE,java.lang.System.currentTimeMillis());

        //update row
        return database.update(Constants.DB_TABLE_NAME,values,Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    //Delete Item
    public void deleteItem(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Constants.DB_TABLE_NAME,Constants.KEY_ID +"=?",new String[]{String.valueOf(id)});

        //close
        database.close();
    }

    //get count
    public int getItemCount(){
        String countQuery = "SELECT * FROM " + Constants.DB_TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);

        return cursor.getCount();
    }

}

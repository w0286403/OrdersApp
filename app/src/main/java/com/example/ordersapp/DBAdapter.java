package com.example.ordersapp;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

import java.util.Date;

public class DBAdapter {

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER ="number";
    public static final String KEY_ADDRESS ="address";
    public static final String KEY_SIZE = "size";
    public static final String KEY_TOP1 = "top1";
    public static final String KEY_TOP2 = "top2";
    public static final String KEY_TOP3 = "top3";
    public static final String KEY_CREATED_AT = "created_at";

    public static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "OrderDB";
    private static final String DATABASE_TABLE = "orders";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
            "create table orders(_id integer primary key autoincrement,"
                    + "name text not null,number text not null,address text not null,size int not null, top1 int not null, top2 int not null, top3 int not null, created_at datetime not null);";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }//end method onCreate

        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            Log.w(TAG,"Upgrade database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS orders");
            onCreate(db);
        }//end method onUpgrade
    }
    //open the database
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //close the database
    public void close()
    {
        DBHelper.close();
    }
    //insert a contact into the database
    public long insertOrder(String name,String number, String address, int size, int top1, int top2, int top3)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_NUMBER, number);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_SIZE, size);
        initialValues.put(KEY_TOP1, top1);
        initialValues.put(KEY_TOP2, top2);
        initialValues.put(KEY_TOP3, top3);
        initialValues.put(KEY_CREATED_AT, String.valueOf( new Date()));
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    //delete a particular contact
    public boolean deleteOrder(long rowId)
    {
        return db.delete(DATABASE_TABLE,KEY_ID + "=" + rowId,null) >0;
    }
    //retrieve all the contacts
    public Cursor getAllOrder()
    {
        return db.query(DATABASE_TABLE,new String[]{KEY_ID,KEY_NAME,
                KEY_NUMBER,KEY_ADDRESS,KEY_SIZE,KEY_TOP1,KEY_TOP2,KEY_TOP3,KEY_CREATED_AT},null,null,null,null,null);
    }

    //retrieve a single contact
    public Cursor getOrder(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                KEY_NAME,KEY_NUMBER,KEY_ADDRESS,KEY_SIZE,KEY_TOP1,KEY_TOP2,KEY_TOP3,KEY_CREATED_AT},KEY_ID + "=" + rowId,null,null,null,null,null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    //updates a contact
    public boolean updateOrder(long rowId,String name,String number,String address, int size, int top1, int top2, int top3)
    {
        ContentValues cval = new ContentValues();
        cval.put(KEY_NAME, name);
        cval.put(KEY_NUMBER, number);
        cval.put(KEY_ADDRESS, address);
        cval.put(KEY_SIZE, size);
        cval.put(KEY_TOP1, top1);
        cval.put(KEY_TOP2, top2);
        cval.put(KEY_TOP3, top3);
        return db.update(DATABASE_TABLE, cval, KEY_ID + "=" + rowId,null) >0;
    }
}//end class DBAdapter

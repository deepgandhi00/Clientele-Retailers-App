package com.clientele.retailers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Royal on 08-Mar-18.
 */

public class cart_sqlhelper extends SQLiteOpenHelper {
    public cart_sqlhelper(Context c, String dbname, int version){
        super(c,"retailer",null,98);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table cart(cart_id integer primary key AUTOINCREMENT,product_id integer ,qty integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists cart");
        onCreate(sqLiteDatabase);
    }
    public boolean insert_cart(int product_id,int product_qty){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("product_id",product_id);
        cv.put("qty",product_qty);
        long c=db.insert("cart",null,cv);
        Log.i("Insert c",c+"");
        if(c>0){
            return true;
        }
        return false;
    }
    public boolean update_pcart(int cart_id,int product_qty){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("qty",product_qty);
        String[] ids={cart_id+""};
        int c=db.update("cart",cv,"cart_id=?",ids);
        if(c>0)
            return true;
        return false;
    }
    public List<pojo_cart> getAllcart(){
        List<pojo_cart> lst=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String fields[]={"cart_id","product_id","qty"};
        Cursor c=db.query("cart",fields,null,null,null,null,null);
        Log.i("counter",c.getCount()+"");

        while(c.moveToNext()){

            int cart_id=c.getInt(c.getColumnIndex("cart_id"));
            int product_id=c.getInt(c.getColumnIndex("product_id"));
            int qty=c.getInt(c.getColumnIndex("qty"));
            pojo_cart pcart=new pojo_cart(cart_id,product_id,qty);
            lst.add(pcart);
        }
        return lst;
    }
    public boolean update_cart(int id,int product_id,int qty){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("product_id",product_id);
        cv.put("qty",qty);
        String[] fields={id+""};
        int count=db.update("cart",cv,"product_detail_id=?",fields);
        if(count==0)
            return false;
        return true;
    }
    public boolean delete_cart(int id){
        SQLiteDatabase db=getWritableDatabase();
        int count=db.delete("cart","cart_id=?",new String[]{id+""});
        if(count==0){
            return false;
        }
        return true;
    }
}

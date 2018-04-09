package com.example.android.inventoryappudacity.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryappudacity.DataBase.InventorContract.Inventor;

/**
 * Created by Justas on 4/9/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DbName = "Inventor";
    private static final int DbVersion = 3;

    public DatabaseHelper(Context context) {
        super(context, DbName, null, DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SqlQuery = "CREATE TABLE " + Inventor.TableName + " (" +
                Inventor.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Inventor.Title + " TEXT NOT NULL, " +
                Inventor.Description + " TEXT NOT NULL, " +
                Inventor.InStock + " INTEGER NOT NULL DEFAULT 0, " +
                Inventor.Price + " TEXT NOT NULL, " +
                Inventor.SupplierName + " TEXT NOT NULL, " +
                Inventor.SupplierPhone + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

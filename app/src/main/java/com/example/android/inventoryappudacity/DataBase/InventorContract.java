package com.example.android.inventoryappudacity.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Justas on 4/9/2018.
 */

public final class InventorContract {

    public static final class Inventor implements BaseColumns {
        public final static String TableName = "INVENTORTABLE";
        public final static String ID = BaseColumns._ID;
        public final static String Description = "DESCRIPTION";
        public final static String Title = "TITLE";
        public final static String InStock = "INSTOCK";
        public final static String SupplierName = "SUPPLIERNAME";
        public final static String SupplierPhone = "SUPPLIERPHONE";
        public final static String Price = "PRICE";
    }
}

package com.example.android.inventoryappudacity.DataBase;

/**
 * Created by Justas on 4/9/2018.
 */

public class DbObject {
    private String mDescription, mTitle, mSupplierName, mSupplierPhone;
    private int mInStock, mID, mPrice;

    public DbObject(String Title, String Description, int InStock, int id, int price, String SupplierName, String SupplierPhone) {
        mDescription = Description;
        mTitle = Title;
        mInStock = InStock;
        mID = id;
        mPrice = price;
        mSupplierName = SupplierName;
        mSupplierPhone = SupplierPhone;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmInStock() {
        return mInStock;
    }

    public int getmID() {
        return mID;
    }

    public int getmPrice() {
        return mPrice;
    }

    public String getmSupplierName() {
        return mSupplierName;
    }

    public String getmSupplierPhone() {
        return mSupplierPhone;
    }
}

package com.example.android.inventoryappudacity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.example.android.inventoryappudacity.DataBase.DatabaseHelper;
import com.example.android.inventoryappudacity.DataBase.DbObject;
import com.example.android.inventoryappudacity.DataBase.InventorContract.Inventor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<DbObject> Objects;
    SwipeMenuListView listView;
    String Order = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadFromDb();
        display();
        Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
                R.array.OrderArray, android.R.layout.simple_spinner_item);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapt);
        spinner.setOnItemSelectedListener(this);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(66, 200,
                        95)));
                openItem.setWidth(170);
                openItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        DbObject object = Objects.get(position);
                        Intent addIntent = new Intent(getApplicationContext(), Add.class);
                        addIntent.putExtra(Inventor.ID, object.getmID());
                        addIntent.putExtra(Inventor.Title, object.getmTitle());
                        addIntent.putExtra(Inventor.Description, object.getmDescription());
                        addIntent.putExtra(Inventor.InStock, object.getmInStock());
                        addIntent.putExtra(Inventor.SupplierName, object.getmSupplierName());
                        addIntent.putExtra(Inventor.SupplierPhone, object.getmSupplierPhone());
                        addIntent.putExtra(Inventor.Price, object.getmPrice());
                        getApplicationContext().startActivity(addIntent);
                        break;
                    case 1:
                        if (delete(position)) {
                            loadFromDb();
                            display();
                        } else
                            Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            Intent AddScreen = new Intent(this, Add.class);
            startActivity(AddScreen);
        } else if (id == R.id.clear) {
            DatabaseHelper helper = new DatabaseHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            db.delete(Inventor.TableName, null, null);
            loadFromDb();
            display();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFromDb() {
        Objects = new ArrayList<>();
        DatabaseHelper Helper = new DatabaseHelper(this);
        SQLiteDatabase db = Helper.getReadableDatabase();

        Cursor cursor = db.query(Inventor.TableName, null, null, null, null, null, Order);
        try {
            int nameIndex = cursor.getColumnIndex(Inventor.Title);
            int descriptionIndex = cursor.getColumnIndex(Inventor.Description);
            int inStockIndex = cursor.getColumnIndex(Inventor.InStock);
            int idIndex = cursor.getColumnIndex(Inventor._ID);
            int priceIndex = cursor.getColumnIndex(Inventor.Price);
            int SupplierNameIndex = cursor.getColumnIndex(Inventor.SupplierName);
            int SupplierPhoneIndex = cursor.getColumnIndex(Inventor.SupplierPhone);


            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                String Description = cursor.getString(descriptionIndex);
                int InStock = cursor.getInt(inStockIndex);
                int id = cursor.getInt(idIndex);
                int price = cursor.getInt(priceIndex);
                String SupplierName = cursor.getString(SupplierNameIndex);
                String SupplierPhone = cursor.getString(SupplierPhoneIndex);
                DbObject object = new DbObject(name, Description, InStock, id, price, SupplierName, SupplierPhone);
                Objects.add(object);
            }
        } finally {
            cursor.close();
        }
    }

    private void display() {
        listView = findViewById(R.id.listview);
        ListAdapter adapter = new ListAdapter(this, Objects);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromDb();
        display();
    }

    private boolean delete(int pos) {
        DatabaseHelper Helper = new DatabaseHelper(this);
        SQLiteDatabase db = Helper.getWritableDatabase();
        DbObject object = Objects.get(pos);
        return db.delete(Inventor.TableName, Inventor._ID + "=" + object.getmID(), null) > 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0)
            Order=Inventor.ID;
        if(i==1)
            Order=Inventor.Price;
        if(i==2)
            Order=Inventor.InStock;

        Toast.makeText(getApplicationContext(),Order,Toast.LENGTH_SHORT).show();

        loadFromDb();
        display();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    Toast.makeText(getApplicationContext(),"Nothing selected",Toast.LENGTH_SHORT).show();
    }
}

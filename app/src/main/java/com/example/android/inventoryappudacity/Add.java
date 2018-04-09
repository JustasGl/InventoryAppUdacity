package com.example.android.inventoryappudacity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryappudacity.DataBase.DatabaseHelper;
import com.example.android.inventoryappudacity.DataBase.InventorContract.Inventor;

public class Add extends AppCompatActivity {

    EditText TitleEdit,
            DescriptionEdit,
            PhoneEdit,
            SupplierEdit,
            QuantityEdit,
            PriceEdit;
    String Title,
            Description,
            Phone,
            Supplier,
            Quantity,
            Price;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TitleEdit = findViewById(R.id.itemNameEditText);
        DescriptionEdit = findViewById(R.id.itemDesciptionEditText);
        PhoneEdit = findViewById(R.id.SupplierPhone);
        SupplierEdit = findViewById(R.id.SupplierName);
        QuantityEdit = findViewById(R.id.QuantityEditText);
        PriceEdit = findViewById(R.id.PriceEditText);
        id = getIntent().getIntExtra(Inventor.ID, -1);
        if (id != -1) {
            edit();
        }
    }

    private void getInfo() {
        Title = TitleEdit.getText().toString().trim();
        Description = DescriptionEdit.getText().toString().trim();
        Phone = PhoneEdit.getText().toString().trim();
        Supplier = SupplierEdit.getText().toString().trim();
        Quantity = QuantityEdit.getText().toString().trim();
        Price = PriceEdit.getText().toString().trim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void edit() {
        TitleEdit.setText(getIntent().getStringExtra(Inventor.Title));
        DescriptionEdit.setText(getIntent().getStringExtra(Inventor.Description));
        PhoneEdit.setText(getIntent().getStringExtra(Inventor.SupplierPhone));
        SupplierEdit.setText(getIntent().getStringExtra(Inventor.SupplierName));
        QuantityEdit.setText(getIntent().getIntExtra(Inventor.InStock, 0)+"");
        PriceEdit.setText(getIntent().getIntExtra(Inventor.Price, 0)+"");
    }

    private void editData() {
        DatabaseHelper Helper = new DatabaseHelper(this);

        SQLiteDatabase db = Helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Inventor.Description, Description);
        values.put(Inventor.Title, Title);
        values.put(Inventor.InStock, Quantity);
        values.put(Inventor.SupplierPhone, Phone);
        values.put(Inventor.SupplierName, Supplier);
        values.put(Inventor.Price, Price);

        db.update(Inventor.TableName, values, Inventor._ID + "=" + id, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addAdd) {
            getInfo();
            if (Title.isEmpty() || Description.isEmpty())
                Toast.makeText(getApplicationContext(), R.string.FilIn, Toast.LENGTH_SHORT).show();
            else {
                id = getIntent().getIntExtra(Inventor.ID, -1);
                if (id == -1)
                    saveData();
                else
                    editData();
                finish();
            }
        } else if (id == R.id.clearAdd) {
            TitleEdit.setText("");
            DescriptionEdit.setText("");
            PhoneEdit.setText("");
            SupplierEdit.setText("");
            QuantityEdit.setText("");
            PriceEdit.setText("");
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        DatabaseHelper Helper = new DatabaseHelper(this);

        SQLiteDatabase db = Helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Inventor.Description, Description);
        values.put(Inventor.Title, Title);
        values.put(Inventor.InStock, Quantity);
        values.put(Inventor.SupplierPhone, Phone);
        values.put(Inventor.SupplierName, Supplier);
        values.put(Inventor.Price, Price);
        db.insert(Inventor.TableName, null, values);
    }
}

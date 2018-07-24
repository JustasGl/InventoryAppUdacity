package com.example.android.inventoryappudacity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryappudacity.DataBase.DatabaseHelper;
import com.example.android.inventoryappudacity.DataBase.InventorContract;
import com.example.android.inventoryappudacity.DataBase.InventorContract.Inventor;

public class Add extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_LOADER = 0;

    private boolean itemChanged=false;

    EditText TitleEdit,
            DescriptionEdit,
            PhoneEdit,
            SupplierEdit,
            QuantityEdit,
            PriceEdit;
    String Title,
            Description,
            Phone,
            Supplier;
    int Price,
            Quantity;
    Uri mCurrentItemUri;

    Button minus,
    plus,
    call;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            itemChanged = true;
            return false;
        }
    };

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
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        call = findViewById(R.id.phone);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PhoneEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.Enter_Supplier_phone_first, Toast.LENGTH_SHORT).show();
                return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", PhoneEdit.getText().toString(), null));
                startActivity(intent);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!QuantityEdit.getText().toString().isEmpty())
                    Quantity= Integer.parseInt(QuantityEdit.getText().toString());
                    Quantity++;
                    QuantityEdit.setText(Quantity+"");
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!QuantityEdit.getText().toString().isEmpty())
                    Quantity= Integer.parseInt(QuantityEdit.getText().toString());
                if(Quantity==0)
                    return;
                Quantity--;
                QuantityEdit.setText(Quantity+"");
            }
        });
        plus.setOnTouchListener(mTouchListener);
        minus.setOnTouchListener(mTouchListener);
        TitleEdit.setOnTouchListener(mTouchListener);
        DescriptionEdit.setOnTouchListener(mTouchListener);
        PhoneEdit.setOnTouchListener(mTouchListener);
        SupplierEdit.setOnTouchListener(mTouchListener);
        QuantityEdit.setOnTouchListener(mTouchListener);
        PriceEdit.setOnTouchListener(mTouchListener);

        mCurrentItemUri = getIntent().getData();

        if (mCurrentItemUri == null) {
            setTitle("New Item");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Item");
            getLoaderManager().initLoader(EXISTING_LOADER, null, this);
        }
    }

    private void getInfo() {
        Title = TitleEdit.getText().toString().trim();
        Description = DescriptionEdit.getText().toString().trim();
        Phone = PhoneEdit.getText().toString().trim();
        Supplier = SupplierEdit.getText().toString().trim();
        Quantity = Integer.parseInt(QuantityEdit.getText().toString().trim());
        Price = Integer.parseInt(PriceEdit.getText().toString().trim());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addAdd) {
            getInfo();
            if (Title.isEmpty() || Description.isEmpty() || Price<0||Phone.isEmpty()||Supplier.isEmpty()||Quantity <0) {
                Checkinfo();
                return false;
            }
            else {
                saveData();
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
        else if(id == R.id.del)
            showDeleteConfirmationDialog();
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {

        if(mCurrentItemUri==null&&Title.isEmpty()&&Description.isEmpty()&& Supplier.isEmpty()&& Phone.isEmpty())
            return;

        ContentValues values = new ContentValues();
        values.put(Inventor.Description, Description);
        values.put(Inventor.Title, Title);
        values.put(Inventor.InStock, Quantity);
        values.put(Inventor.SupplierPhone, Phone);
        values.put(Inventor.SupplierName, Supplier);
        values.put(Inventor.Price, Price);

        if(mCurrentItemUri==null)
        {
            Uri newUri = getContentResolver().insert(InventorContract.Inventor.CONTENT_URI, values);
        if(newUri==null)
            Toast.makeText(this, R.string.failed_to_upload,Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, R.string.success_inserting_item,Toast.LENGTH_SHORT).show();
        }
        else
        {
            int updated = getContentResolver().update(mCurrentItemUri,values,null,null);
            if(updated==0)
                Toast.makeText(this, R.string.failed_updating_item,Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.success_updating,Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public void onBackPressed() {
        if (!itemChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteitem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteitem()
    {
        if(mCurrentItemUri!=null)
        {
            int deleted = getContentResolver().delete(mCurrentItemUri,null,null);

            if(deleted==0)
            {
                Toast.makeText(this, R.string.error_deleting_tem,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.item_deleted_succesfully,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
    private void Checkinfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.checkinfo);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showUnsavedChangesDialog (DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String projection [] = { Inventor.ID,
                Inventor.Title,
                Inventor.Description,
                Inventor.InStock,
                Inventor.Price,
                Inventor.SupplierName,
                Inventor.SupplierPhone};
        return new CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor==null||cursor.getCount()<1)
            return;
        if(cursor.moveToFirst())
        {
            int nameIndex = cursor.getColumnIndex(Inventor.Title);
            int descriptionIndex = cursor.getColumnIndex(Inventor.Description);
            int inStockIndex = cursor.getColumnIndex(Inventor.InStock);
            int priceIndex = cursor.getColumnIndex(Inventor.Price);
            int SupplierNameIndex = cursor.getColumnIndex(Inventor.SupplierName);
            int SupplierPhoneIndex = cursor.getColumnIndex(Inventor.SupplierPhone);

            String name = cursor.getString(nameIndex);
            String description = cursor.getString(descriptionIndex);
            int quantity = cursor.getInt(inStockIndex);
            int price = cursor.getInt(priceIndex);
            String suplliername = cursor.getString(SupplierNameIndex);
            String suplierphone = cursor.getString(SupplierPhoneIndex);

            Quantity = quantity;

            TitleEdit.setText(name);
            SupplierEdit.setText(suplliername);
            QuantityEdit.setText(quantity+"");
            DescriptionEdit.setText(description);
            PriceEdit.setText(price+"");
            PhoneEdit.setText(suplierphone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

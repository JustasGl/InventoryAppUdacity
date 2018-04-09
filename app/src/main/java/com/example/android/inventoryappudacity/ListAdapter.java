package com.example.android.inventoryappudacity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryappudacity.DataBase.DbObject;

import java.util.ArrayList;

/**
 * Created by Justas on 4/9/2018.
 */

public class ListAdapter extends ArrayAdapter<DbObject> {
    public ListAdapter(Context context, ArrayList<DbObject> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);

        TextView Title = convertView.findViewById(R.id.Title), Description = convertView.findViewById(R.id.description);
        TextView Phone, Name, Price, Quantity;
        Quantity = convertView.findViewById(R.id.Quantity);
        Phone = convertView.findViewById(R.id.SupplierPhone);
        Name = convertView.findViewById(R.id.SupplierName);
        Price = convertView.findViewById(R.id.Price);

        final CheckBox InStock = convertView.findViewById(R.id.checkbox);
        final DbObject object = getItem(position);

        Title.setText(object.getmTitle());
        Description.setText(object.getmDescription());
        if (object.getmInStock() <= 0)
            InStock.setChecked(false);
        else if (object.getmInStock() > 0)
            InStock.setChecked(true);

        Quantity.setText("Quantity "+object.getmInStock() + "");
        Phone.setText(object.getmSupplierPhone());
        Name.setText(object.getmSupplierName());
        Price.setText("Price "+object.getmPrice());

        InStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), object.getmTitle(), Toast.LENGTH_SHORT).show();
                if (object.getmInStock() <= 0)
                    InStock.setChecked(false);
                else if (object.getmInStock() > 0)
                    InStock.setChecked(true);
            }
        });
        return convertView;
    }

}

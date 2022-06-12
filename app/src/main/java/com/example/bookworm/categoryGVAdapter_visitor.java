package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class categoryGVAdapter_visitor extends ArrayAdapter<Category> {
    public categoryGVAdapter_visitor(@NonNull Context context, ArrayList<Category> categoryModelArrayList) {
        super(context, 0, categoryModelArrayList);
    }

    public categoryGVAdapter_visitor(user_category_viewbooks context) {
        super(context,0);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        Category dataModal = getItem(position);

        TextView nameTV = listitemView.findViewById(R.id.idtxtcategory);
        ImageView courseIV = listitemView.findViewById(R.id.idIVcategory);

        nameTV.setText(dataModal.getCategory_name());


        Picasso.get().load(dataModal.getImgid()).into(courseIV);


        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objcategory=new Intent(getContext(),category_viewbooks.class);
                objcategory.putExtra("category_visitor",dataModal.getCategory_name());
                getContext().startActivity(objcategory);
                //  Toast.makeText(getContext(), "Item clicked is : " + dataModal.getCategory_name(), Toast.LENGTH_SHORT).show();
            }
        });
        return listitemView;
    }
}

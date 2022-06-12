package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class MybookAdapter extends  RecyclerView.Adapter<MybookAdapter.ViewHolder>{
    private List<Book> listData;

    public MybookAdapter(List<Book> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book ld=listData.get(position);
        holder.txtbname.setText(ld.getBookName());
        holder.txtbauthor.setText(ld.getBookAuthor());
        holder.txtcategory.setText(ld.getBookCategory());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtbname,txtbauthor,txtcategory;
        public ViewHolder(View itemView) {
            super(itemView);
            txtbname=(TextView)itemView.findViewById(R.id.txtbookname_item);
            txtbauthor=(TextView)itemView.findViewById(R.id.txtbookauthor_item);
            txtcategory=(TextView)itemView.findViewById(R.id.txtbookcateogry_item);
        }
    }
}

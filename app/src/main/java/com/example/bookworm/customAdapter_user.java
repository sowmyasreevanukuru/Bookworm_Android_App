package com.example.bookworm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class customAdapter_user extends BaseAdapter {
    private static ArrayList<Book> searchArrayList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int lastPosition;


    static class ViewHolder {
        TextView bookname;
        TextView bookauthor;
        TextView bookcategory;
        ImageView img_book;
    }

    public customAdapter_user(Context context, ArrayList<Book> results) {
        searchArrayList = results;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return searchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String bname = searchArrayList.get(position).getBookName();
        String bauthor = searchArrayList.get(position).getBookAuthor();
        String bcategory = searchArrayList.get(position).getBookCategory();
        String bimg=searchArrayList.get(position).getCoverimg();
        String bpdf=searchArrayList.get(position).getBookpdf();
        String bpublisher=searchArrayList.get(position).getBookPublisher();
        String bdescription=searchArrayList.get(position).getBookDescription();

        final View result;

        customAdapter_user.ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.bookitem, parent, false);
            holder = new customAdapter_user.ViewHolder();
            holder.bookname = (TextView) convertView.findViewById(R.id.txtbookname_item);
            holder.bookauthor = (TextView) convertView.findViewById(R.id.txtbookauthor_item);
            holder.bookcategory = (TextView) convertView.findViewById(R.id.txtbookcateogry_item);
            holder.img_book=(ImageView)convertView.findViewById(R.id.b_image);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (customAdapter_user.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.bookname.setText(bname);
        holder.bookauthor.setText(bauthor);
        holder.bookcategory.setText(bcategory);
        Picasso.get().load(bimg).into(holder.img_book);
        holder.img_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.img_book.getContext(),user_view_book_details.class);
                intent.putExtra("bookname",bname);
                intent.putExtra("authorname",bauthor);
                intent.putExtra("publisher",bpublisher);
                intent.putExtra("description",bdescription);
                intent.putExtra("bookimg",bimg);
                intent.putExtra("bookpdf",bpdf);

                // Toast.makeText(holder.img_book.getContext(), bpdf.toString(), Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img_book.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}

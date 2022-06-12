package com.example.bookworm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class bookAdapter extends ArrayAdapter {

    private Activity mcontext;
    List<Book> bookList;

    public bookAdapter(Activity mcontext,List<Book> bookList)
    {
        super(mcontext,R.layout.bookitem,bookList);
        this.mcontext=mcontext;
        this.bookList=bookList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=mcontext.getLayoutInflater();
        View listitemview=inflater.inflate(R.layout.bookitem,null,true);
        TextView txtbookname=listitemview.findViewById(R.id.txtbookname_item);
        TextView txtbookauthor=listitemview.findViewById(R.id.txtbookauthor_item);
        TextView txtcategory=listitemview.findViewById(R.id.txtbookcateogry_item);

        Book books=bookList.get(position);
        txtbookauthor.setText(books.getBookAuthor());
        txtbookname.setText(books.getBookName());
        txtcategory.setText(books.getBookCategory());
        return listitemview;
    }
}

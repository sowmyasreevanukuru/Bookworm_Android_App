package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.listener.SettingsClickListener;
import com.squareup.picasso.Picasso;

public class view_book_details extends AppCompatActivity {
    NavigationView view_bookk_nav;
    ActionBarDrawerToggle view_book_toggle;
    DrawerLayout view_book_drawerLayout;
    Toolbar view_book_toolbar;
    Button btnread;
    AppCompatTextView txtbookname,txtbookauthor,txtbookdescription,txtpublisher;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_details);

        btnread=findViewById(R.id.btnread);
        btnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view_book_details.this);
                builder.setMessage("Login to read this book!\nLogin to your account?");

                // Set Alert Title
                builder.setTitle("Alert !");

                // Set Cancelable false
                // for when the user clicks on the outside
                // the Dialog Box then it will remain show
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        Intent ilogin=new Intent(getApplicationContext(),login.class);
                        startActivity(ilogin);

                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dialog.cancel();
                    }
                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();
            }
        });
        //navigation menu
        view_book_toolbar=(Toolbar)findViewById(R.id.book_details_toolbar);
        setSupportActionBar(view_book_toolbar);

        view_bookk_nav=(NavigationView) findViewById(R.id.view_book_navmenu);
        view_book_drawerLayout=(DrawerLayout) findViewById(R.id.bookdetails_drawer);

        view_book_toggle=new ActionBarDrawerToggle(this,view_book_drawerLayout,view_book_toolbar,R.string.open,R.string.close);
        view_book_drawerLayout.addDrawerListener(view_book_toggle);
        view_book_toggle.syncState();

        view_bookk_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        Intent ihh=new Intent(getApplicationContext(),home.class);
                        startActivity(ihh);
                        //Toast.makeText(getApplicationContext(), "Home panel", Toast.LENGTH_SHORT).show();
                        view_book_drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_categories:
                        Intent ih=new Intent(getApplicationContext(),categories.class);
                        startActivity(ih);
                        view_book_drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_login:
                        Intent i=new Intent(getApplicationContext(),login.class);
                        startActivity(i);
                        view_book_drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

        txtbookauthor=findViewById(R.id.view_bookauthor);
        txtbookdescription=findViewById(R.id.view_bookdescription);
        txtpublisher=findViewById(R.id.view_boookpublisher);
        txtbookname=findViewById(R.id.view_bookname);
        img=findViewById(R.id.view_book_img);

        getdetails();

    }
    private void getdetails()
    {
        String bookname=getIntent().getStringExtra("bookname");
        String authorname=getIntent().getStringExtra("authorname");
        String publisher=getIntent().getStringExtra("publisher");
        String description=getIntent().getStringExtra("description");
        String bookimg=getIntent().getStringExtra("bookimg");

        Picasso.get().load(bookimg).into(img);

        txtbookname.setText(bookname);
        txtbookauthor.setText(authorname);
        txtpublisher.setText(publisher);
        txtbookdescription.setText(description);



    }
}
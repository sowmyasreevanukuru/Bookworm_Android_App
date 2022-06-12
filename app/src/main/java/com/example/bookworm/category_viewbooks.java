package com.example.bookworm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class category_viewbooks extends AppCompatActivity {
    NavigationView nav_categoryviewbooks;
    ActionBarDrawerToggle toggle_categoryviewbooks;
    DrawerLayout drawerLayout_categoryviewbooks;
    Toolbar toolbar_categoryviewbooks;
    int t;
    DatabaseReference reference_categorybooks;
    ArrayList<Book> bookslist_categorybooks=new ArrayList<>();
    ListView lstbooks_categorybooks;
    TextView txt;
    ProgressBar progressBar_categorybooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_viewbooks);

        //navigation menu
        toolbar_categoryviewbooks=(Toolbar)findViewById(R.id.toolbar_categorybooks);
        setSupportActionBar(toolbar_categoryviewbooks);

        nav_categoryviewbooks=(NavigationView) findViewById(R.id.navmenu_categorybooks);
        drawerLayout_categoryviewbooks=(DrawerLayout) findViewById(R.id.drawer_categorybooks);

        toggle_categoryviewbooks=new ActionBarDrawerToggle(this,drawerLayout_categoryviewbooks,toolbar_categoryviewbooks,R.string.open,R.string.close);
        drawerLayout_categoryviewbooks.addDrawerListener(toggle_categoryviewbooks);
        toggle_categoryviewbooks.syncState();

        nav_categoryviewbooks.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        Intent ihh=new Intent(getApplicationContext(),home.class);
                        startActivity(ihh);
                        //Toast.makeText(getApplicationContext(), "Home panel", Toast.LENGTH_SHORT).show();
                        drawerLayout_categoryviewbooks.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_categories:
                        Intent hv=new Intent(getApplicationContext(),categories.class);
                        startActivity(hv);
                        drawerLayout_categoryviewbooks.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_login:
                        Intent i=new Intent(getApplicationContext(),login.class);
                        startActivity(i);
                        drawerLayout_categoryviewbooks.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

        getbooks();
    }

    private void getbooks() {

        progressBar_categorybooks=findViewById(R.id.progressBar_categorybooks);
        lstbooks_categorybooks=findViewById(R.id.lstbooks_categorybooks);
        txt=findViewById(R.id.txtmessage_c);

        t=0;
        String cname= String.valueOf(getIntent().getExtras().getString("category_visitor"));
        reference_categorybooks= FirebaseDatabase.getInstance().getReference("Book");
        reference_categorybooks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //  Toast.makeText(getApplicationContext(), snapshot.child("bookAuthor").getValue().toString(), Toast.LENGTH_LONG).show();
                String bookname = snapshot.child("bookName").getValue().toString();
                String bookauthor = snapshot.child("bookAuthor").getValue().toString();
                String bookcategory = snapshot.child("bookCategory").getValue().toString();
                String bookimage=snapshot.child("coverimg").getValue().toString();
                String bookpdf=snapshot.child("bookpdf").getValue().toString();
                String bookpublisher=snapshot.child("bookPublisher").getValue().toString();
                String bookdescription=snapshot.child("bookDescription").getValue().toString();


                if(bookcategory.equals(cname)) {
                    t++;
                    Book searchModel = new Book(bookname, bookauthor, bookcategory, bookimage, bookpdf, bookpublisher, bookdescription);
                    bookslist_categorybooks.add(searchModel);

                    customAdapter customAdapter = new customAdapter(getApplicationContext(), bookslist_categorybooks);
                    lstbooks_categorybooks.setAdapter(customAdapter);

                    progressBar_categorybooks.setVisibility(View.GONE);
                   // temp++;

                }
                else
                {
                    if (t==0)
                    {
                       // txt.setText("No books availble under this category");
                    }
                    progressBar_categorybooks.setVisibility(View.GONE);
                    //temp=-1;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}